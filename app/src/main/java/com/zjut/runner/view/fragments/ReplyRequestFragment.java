package com.zjut.runner.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zjut.runner.Controller.CurrentSession;
import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.Model.OrderModel;
import com.zjut.runner.Model.OrderStatus;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.widget.CircleImageView;
import com.zjut.runner.widget.MaterialDialog;


/**
 * Created by Administrator on 2016/10/27.
 */

public class ReplyRequestFragment extends RequestInfoFragment {

    private String campusID;
    private int charge;
    private AVObject requestObj;
    private AVUser userObj;
    private AVObject campusObj;
    private CampusModel user;

    @Override
    protected void parseArgument() {
        Bundle bundle = getArguments();
        if(bundle == null)
            return;
        orderModel = (OrderModel) bundle.getSerializable(Constants.PARAM_ORDER);
        campusID = activity.campusModel.getObjectId();
        userObj = AVUser.getCurrentUser();
        user = orderModel.getOwnerModel();
    }

    @Override
    protected void findViews(View rootView) {
        super.findViews(rootView);
        bt_submit.setVisibility(View.VISIBLE);
        bt_submit.setText(getString(R.string.str_go_help));
        setView();
        setEnable(false);
        setButtonDisable(true);
    }

    @Override
    protected void loadHelpers() {

    }

    @Override
    public void changeTitle() {
        activity.changeTitle(R.string.str_myorder_info);
    }

    @Override
    public void initMenu(Context context, Menu menu) {
        clearMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sub:
                showDialog();
                //fetchObj();
                break;
            default:
                super.onClick(v);
        }
    }

    private void showDialog(){
        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_slider, null);
        final AppCompatSeekBar seekBar = (AppCompatSeekBar) view.findViewById(R.id.seekbar);
        final TextView tv_charge = (TextView) view.findViewById(R.id.tv_charge);
        final TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        final TextView tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        final CircleImageView iv_user = (CircleImageView) view.findViewById(R.id.iv_helper);
        final MaterialDialog materialDialog = new MaterialDialog(activity)
                .setView(view);
        charge = orderModel.getCharge();
        seekBar.setProgress(charge);
        tv_charge.setText(getString(R.string.helper_charge, StringUtil.convertIntegerToString(charge)));
        if(user.getGenderType() != null){
            tv_gender.setText(user.getGenderType().toString());
        }
        tv_name.setText(user.getCampusName());
        ImageLoader.getInstance().displayImage(user.getUrl(),iv_user, GeneralUtils.getOptions());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                charge = progress;
                tv_charge.setText(getString(R.string.helper_charge,String.valueOf(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        materialDialog.setPositiveButton(getString((R.string.button_ok)), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                submitHelperToCloud();
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

    private void submitHelperToCloud(){
        AVObject campus = AVObject.createWithoutData(Constants.TABLE_CAMPUS,campusID);
        campus.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(e == null){
                    campusObj = avObject;
                    putToRequestReply();
                }else{
                    failSubmit();
                }
            }
        });
    }

    private void putToRequestReply(){
        AVObject reply;
        if(orderModel.getHelpers() > 0) {
            reply = new AVObject(Constants.PARAM_REQUEST_REPLY);
        }else{
            reply = AVObject.createWithoutData(Constants.PARAM_REQUEST_REPLY,orderModel.getReplyRequestObjectID());
        }
        fetchRequestObj(reply);
    }

    private void fetchRequestObj(final AVObject reply){
        AVObject campus = AVObject.createWithoutData(Constants.TABLE_REQUEST,orderModel.getObjectID());
        campus.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(e == null){
                    requestObj = avObject;
                    reply.put(Constants.PARAM_REQUEST_OBJ, requestObj);
                    reply.put(Constants.PARAM_CAMPUS_INFO, campusObj);
                    reply.put(Constants.PARAM_USER_INFO, userObj);
                    reply.put(Constants.PARAM_CHARGE, charge);
                    reply.put(Constants.PARAM_STATUS, OrderStatus.PENDING.toString());
                    reply.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                incrementHelper();
                            } else {
                                failSubmit();
                            }
                        }
                    });
                }else{
                    failSubmit();
                }
            }
        });
    }

    private void incrementHelper(){
        AVObject request = AVObject.createWithoutData(Constants.TABLE_REQUEST,orderModel.getObjectID());
        request.increment(Constants.PARAM_NUM_HELPER, 1);
        request.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    CurrentSession.removeRunOrder(activity,orderModel.getObjectID());
                    successSubmit();
                }else{
                    failSubmit();
                }
            }
        });
    }
}
