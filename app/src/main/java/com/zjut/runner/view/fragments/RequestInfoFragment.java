package com.zjut.runner.view.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.zjut.runner.Controller.AsyncTaskController;
import com.zjut.runner.Controller.CurrentSession;
import com.zjut.runner.Model.ActionType;
import com.zjut.runner.Model.HelperModel;
import com.zjut.runner.Model.OrderModel;
import com.zjut.runner.Model.OrderStatus;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.MLog;
import com.zjut.runner.util.ResourceUtil;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.widget.DetailActionItemHolder;
import com.zjut.runner.widget.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuylai on 2016/10/26.
 */

public class RequestInfoFragment extends NewRequestFragment implements DetailActionItemHolder.HelpersClickListener{

    private OrderModel orderModel;

    @Override
    public void helpersClick() {
        MLog.i("helper","click");
    }

    private List<HelperModel> helperModels = new ArrayList<>();

    private LinearLayout ll_wrap;

    //load model
    private AsyncTask<Object,Void,List<HelperModel>> dbLoad = null;

    //save model
    private AsyncTask<Object,Void,Void> dbSave = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArgument();
    }

    private void parseArgument(){
        Bundle bundle = getArguments();
        if(bundle == null)
            return;
        orderModel = (OrderModel) bundle.getSerializable(Constants.PARAM_ORDER);
    }

    @Override
    protected void findViews(View rootView) {
        super.findViews(rootView);
        loadHelpers();
        ll_wrap = (LinearLayout) rootView.findViewById(R.id.ll_wrap);
        bt_submit.setVisibility(View.GONE);
        setView();
        setEnable(false);
    }

    private void loadHelpers(){
        if(orderModel.getHelpers() > 0){
            progressBar.setVisibility(View.VISIBLE);
            AddViewHelperClick();
            loadHelpersInfo();
        }
    }

    private void AddViewHelperClick(){
        DetailActionItemHolder detailActionItemHolder = new DetailActionItemHolder(activity,0,
                R.string.str_helpers,StringUtil.convertIntegerToString(orderModel.getHelpers()), ActionType.HELPER,true,this);
        View root = detailActionItemHolder.getRootView();
        addView(root);
        setMarginTop(GeneralUtils.getDimenPx(activity, R.dimen.margin_standard),root);
    }

    protected void addView(View view) {
        if (view == null) {
            return;
        }
        if (ll_wrap == null) {
            return;
        }
        ll_wrap.addView(view);
    }
    protected void setMarginTop(int marginTop, View view) {
        if (view == null) {
            return;
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (lp == null){
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        lp.topMargin = marginTop;
        view.setLayoutParams(lp);
    }

    private synchronized void loadHelpersInfo(){
        if(dbLoad != null)
            return;
        dbLoad = new AsyncTask<Object, Void, List<HelperModel>>() {
            @Override
            protected List<HelperModel> doInBackground(Object... params) {
                return CurrentSession.getHelperModels(activity,orderModel.getObjectID());
            }

            @Override
            protected void onPostExecute(List<HelperModel> helpers) {
                dbLoad = null;
                if(helpers != null && helpers.size() > 0){
                    helperModels.clear();
                    helperModels.addAll(helpers);
                }
                loadFromCloud();
            }
        };
        AsyncTaskController.startTask(dbLoad);
    }

    private void loadFromCloud(){
        AVQuery<AVObject> innerquery = new AVQuery<>(Constants.TABLE_REQUEST);
        innerquery.whereEqualTo(Constants.PARAM_OBJECT_ID,orderModel.getObjectID());
        AVQuery<AVObject> query = new AVQuery<>(Constants.PARAM_REQUEST_REPLY);
        query.whereMatchesQuery(Constants.PARAM_REQUEST_OBJ,innerquery);
        query.orderByAscending(Constants.PARAM_CREATE);
        query.include(Constants.PARAM_USER_INFO);
        query.include(Constants.PARAM_CAMPUS_INFO);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                progressBar.setVisibility(View.GONE);
                if(e == null){
                    helperModels.clear();
                    helperModels.addAll(HelperModel.setCampusModel(orderModel.getObjectID(),list));
                    saveHelpersToDB();
                }
            }
        });
    }

    protected synchronized void saveHelpersToDB(){
        if(dbSave != null){
            return;
        }
        dbSave = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                for(HelperModel helperModel:helperModels){
                    CurrentSession.putHelperModel(activity,helperModel);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                dbSave = null;
            }
        };
        AsyncTaskController.startTask(dbSave);
    }


    private void setView(){
        et_remarks.setText(orderModel.getRemark());
        tv_charge.setText(getString(R.string.str_charge,StringUtil.convertIntegerToString(orderModel.getCharge()))
                + getString(R.string.str_currency));
        seekBar.setProgress(orderModel.getCharge());
        et_title.setText(orderModel.getTitle());
        et_time.setText(orderModel.getOrderDate());
        et_deadline.setText(orderModel.getDeadline());
        et_dest.setText(orderModel.getDest());
    }

    @Override
    public void changeTitle() {
        activity.changeTitle(R.string.str_myorder_info);
    }

    @Override
    public void initMenu(Context context, Menu menu) {
        clearMenu(menu);
        activity.getMenuInflater().inflate(R.menu.main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
                if(orderModel.getHelpers() > 0){
                    showAlert();
                }else {
                    showCancellDialog();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showCancellDialog(){
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

    protected void updateToCloud(){
        AVObject request = AVObject.createWithoutData(Constants.TABLE_REQUEST,orderModel.getObjectID());
        request.put(Constants.PARAM_STATUS, OrderStatus.CANCELLED.toString());
        request.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                progressBar.setVisibility(View.GONE);
                if(e == null){
                    successSubmit();
                }else{
                    failSubmit();
                }
            }
        });
    }

    protected void failSubmit() {
        final MaterialDialog materialDialog = new MaterialDialog(activity);
        materialDialog.setTitle(R.string.str_fail);
        materialDialog.setMessage(R.string.fail_submit);
        materialDialog.setPositiveButton(R.string.button_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                progressBar.setVisibility(View.GONE);
            }
        });
        materialDialog.show();
    }

    protected void successSubmit() {
        final MaterialDialog materialDialog = new MaterialDialog(activity);
        materialDialog.setTitle(R.string.str_sucess);
        materialDialog.setMessage(R.string.submit);
        materialDialog.setPositiveButton(R.string.button_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                callItemSelected(true);
            }
        });
        materialDialog.show();
    }

    private void showAlert(){
        final MaterialDialog materialDialog = new MaterialDialog(activity)
                .setTitle(getString(R.string.reminder))
                .setMessage(getString(R.string.alert_cancel))
                .setCanceledOnTouchOutside(false);
        materialDialog.setPositiveButton(getString(R.string.button_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
    }



    protected void setEnable(boolean b) {
        et_time.setEnabled(b);
        et_remarks.setEnabled(b);
        bt_submit.setEnabled(b);
        et_deadline.setEnabled(b);
        et_title.setEnabled(b);
        et_dest.setEnabled(b);
        seekBar.setEnabled(b);
    }


}
