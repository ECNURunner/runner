package com.zjut.runner.view.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zjut.runner.Controller.AsyncTaskController;
import com.zjut.runner.Controller.CurrentSession;
import com.zjut.runner.Model.ActionType;
import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.Model.OrderModel;
import com.zjut.runner.Model.OrderStatus;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.widget.CircleImageView;
import com.zjut.runner.widget.DetailActionItemHolder;
import com.zjut.runner.widget.MaterialDialog;


/**
 * Created by Phuylai on 2016/10/31.
 */

public class RunInfoFragment extends RequestInfoFragment {

    //save model
    protected AsyncTask<Object,Void,Void> dbSaveOrder = null;

    @Override
    protected void loadHelpers() {
        AddViewHelperClick(orderModel.getOwnerModel().getCampusName());
    }

    @Override
    protected void AddViewHelperClick(String name) {
        detailActionItemHolder = new DetailActionItemHolder(activity,0,
                R.string.str_owner,name,ActionType.HELPER,true,this);
        View root = detailActionItemHolder.getRootView();
        addView(root);
        setMarginTop(GeneralUtils.getDimenPx(activity, R.dimen.margin_standard),root);
    }

    @Override
    protected void setView() {
        et_remarks.setText(orderModel.getRemark());
        tv_charge.setText(getString(R.string.helper_charge, StringUtil.convertIntegerToString(orderModel.getFinalCharge())));
        seekBar.setProgress(orderModel.getFinalCharge());
        et_title.setText(orderModel.getTitle());
        et_time.setText(orderModel.getOrderDate());
        et_deadline.setText(orderModel.getDeadline());
        et_dest.setText(orderModel.getDest());
    }

    @Override
    public void changeTitle() {
        activity.changeTitle(R.string.str_info);
    }

    @Override
    public void initMenu(Context context, Menu menu) {
        clearMenu(menu);
        if(orderModel.getStatus() == OrderStatus.PENDING){
            activity.getMenuInflater().inflate(R.menu.main,menu);
        }else if(orderModel.getStatus() == OrderStatus.GO){
            activity.getMenuInflater().inflate(R.menu.finished,menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                showCancellDialog();
                return true;
            case R.id.action_done:
                showDoneDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void showCancellDialog(){
        final MaterialDialog materialDialog = new MaterialDialog(activity)
                .setTitle(getString(R.string.reminder))
                .setMessage(getString(R.string.message_cancel))
                .setCanceledOnTouchOutside(false);
        materialDialog.setPositiveButton(getString(R.string.button_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                updateToCloud();
            }
        });
        materialDialog.setNegativeButton(getString(R.string.button_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
    }

    protected void showDoneDialog(){
        final MaterialDialog materialDialog = new MaterialDialog(activity)
                .setTitle(getString(R.string.reminder))
                .setMessage(getString(R.string.message_done))
                .setCanceledOnTouchOutside(false);
        materialDialog.setPositiveButton(getString(R.string.button_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                updateDoneToCloud();
            }
        });
        materialDialog.setNegativeButton(getString(R.string.button_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
    }



    protected void updateToCloud(){
        AVObject request = AVObject.createWithoutData(Constants.PARAM_REQUEST_REPLY,orderModel.getReplyRequestObjectID());
        request.put(Constants.PARAM_STATUS, OrderStatus.CANCELLED.toString());
        request.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                progressBar.setVisibility(View.GONE);
                if(e == null){
                    decreaseHelperNumber();
                }else{
                    failSubmit();
                }
            }
        });
    }

    private void decreaseHelperNumber(){
        AVObject request = AVObject.createWithoutData(Constants.TABLE_REQUEST,orderModel.getObjectID());
        request.increment(Constants.PARAM_NUM_HELPER,-1);
        request.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    //CurrentSession.updateRunStatus(activity,orderModel.getObjectID(),OrderStatus.CANCELLED);
                    //successSubmit();
                    saveOrderToDB(orderModel.getObjectID(),OrderStatus.CANCELLED);
                }else{
                    failSubmit();
                }
            }
        });
    }

    protected void updateDoneToCloud(){
        AVObject request = AVObject.createWithoutData(Constants.PARAM_REQUEST_REPLY,orderModel.getReplyRequestObjectID());
        request.put(Constants.PARAM_STATUS, OrderStatus.COMPLETED.toString());
        request.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                progressBar.setVisibility(View.GONE);
                if(e == null){
                    //successSubmit();
                    saveDoneToRequest();
                }else{
                    failSubmit();
                }
            }
        });
    }

    protected void saveDoneToRequest(){
        final AVObject request = AVObject.createWithoutData(Constants.TABLE_REQUEST,orderModel.getObjectID());
        request.put(Constants.PARAM_STATUS, OrderStatus.COMPLETED.toString());
        request.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                progressBar.setVisibility(View.GONE);
                if(e == null){
                    saveOrderToDB(orderModel.getObjectID(),OrderStatus.COMPLETED);
                    AVPush avPush = GeneralUtils.getPush(orderModel.getOwnerModel().getInstallationID(),
                            Constants.MSG_3);
                    avPush.sendInBackground();
                }else{
                    failSubmit();
                }
            }
        });
    }

    @Override
    public void helpersClick() {
        final View view = activity.getLayoutInflater().inflate(R.layout.helper_info, null);
        final CircleImageView iv_icon = (CircleImageView) view.findViewById(R.id.iv_helper);
        final TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        final TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        final TextView tv_email = (TextView) view.findViewById(R.id.tv_email);
        final TextView tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        final TextView tv_charge = (TextView) view.findViewById(R.id.tv_charge);
        final MaterialDialog materialDialog = new MaterialDialog(activity)
                .setView(view);
        CampusModel campusModel = orderModel.getOwnerModel();
        if(!StringUtil.isNull(campusModel.getUrl())){
            ImageLoader.getInstance().displayImage(campusModel.getUrl(),iv_icon,GeneralUtils.getOptions());
        }
        tv_name.setText(getString(R.string.helper_name,campusModel.getCampusName()));
        tv_phone.setText(getString(R.string.helper_phone,campusModel.getMobile()));
        tv_email.setText(getString(R.string.helper_email,campusModel.getEmail()));
        if(campusModel.getGenderType() != null) {
            tv_gender.setText(getString(R.string.helper_gender, campusModel.getGenderType().toString()));
        }
        tv_charge.setText(getString(R.string.helper_charge,StringUtil.convertIntegerToString(orderModel.getCharge())));
        materialDialog.setPositiveButton(getString(R.string.button_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
    }

    protected synchronized void saveOrderToDB(final String requestID, final OrderStatus status) {
        if(dbSaveOrder != null)
            return;
        dbSaveOrder = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                CurrentSession.updateRunStatus(activity,requestID,status);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                dbSaveOrder = null;
                successSubmit();
            }
        };
        AsyncTaskController.startTask(dbSaveOrder);
    }

}
