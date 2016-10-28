package com.zjut.runner.view.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zjut.runner.Controller.AsyncTaskController;
import com.zjut.runner.Controller.CurrentSession;
import com.zjut.runner.Model.ActionType;
import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.Model.HelperModel;
import com.zjut.runner.Model.OrderModel;
import com.zjut.runner.Model.OrderStatus;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.MLog;
import com.zjut.runner.util.ResourceUtil;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.widget.CircleImageView;
import com.zjut.runner.widget.DetailActionItemHolder;
import com.zjut.runner.widget.MaterialDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuylai on 2016/10/26.
 */

public class RequestInfoFragment extends NewRequestFragment implements DetailActionItemHolder.HelpersClickListener, BaseFragment.SelectedItemCallBackListener {

    protected OrderModel orderModel;

    private List<HelperModel> helperModels = new ArrayList<>();
    private AVObject campusObj;
    private AVObject campusUser;

    private LinearLayout ll_wrap;

    private  DetailActionItemHolder detailActionItemHolder;

    //load model
    private AsyncTask<Object,Void,List<HelperModel>> dbLoad = null;

    //save model
    private AsyncTask<Object,Void,Void> dbSave = null;

    private DisplayImageOptions options;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArgument();
    }

    protected void parseArgument(){
        Bundle bundle = getArguments();
        if(bundle == null)
            return;
        orderModel = (OrderModel) bundle.getSerializable(Constants.PARAM_ORDER);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_usericon_default)
                .showImageForEmptyUri(R.drawable.ic_usericon_default)
                .showImageOnFail(R.drawable.ic_usericon_default)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    protected void findViews(View rootView) {
        super.findViews(rootView);
        ll_wrap = (LinearLayout) rootView.findViewById(R.id.ll_wrap);
        loadHelpers();
        bt_submit.setVisibility(View.GONE);
        setView();
        setEnable(false);
    }

    protected void loadHelpers(){
        if(orderModel.getHelpers() > 0) {
            if(orderModel.isChosen()) {
                AddViewHelperClick(orderModel.getHelper().getCampusName());
            }else{
                progressBar.setVisibility(View.VISIBLE);
                loadHelpersInfo();
                AddViewHelperClick(StringUtil.convertIntegerToString(orderModel.getHelpers()));
            }
        }
    }

    private void AddViewHelperClick(String name){
        detailActionItemHolder = new DetailActionItemHolder(activity,0,
                R.string.str_helper,name, ActionType.HELPER,true,this);
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

    protected void loadFromCloud(){
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


    protected void setView(){
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
        if(orderModel.getStatus() == OrderStatus.PENDING && orderModel.getHelpers() == 0){
            activity.getMenuInflater().inflate(R.menu.main,menu);
        }
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

    protected void updateStatusToCloud(HelperModel helperModel,AVObject campusObj,AVObject campusUser){
        AVObject request = AVObject.createWithoutData(Constants.TABLE_REQUEST,orderModel.getObjectID());
        request.put(Constants.PARAM_HELPER_USER,campusUser);
        request.put(Constants.PARAM_HELPER_CAMPUS,campusObj);
        request.put(Constants.PARAM_CHOSEN,true);
        request.put(Constants.PARAM_FINAL_CHARGE,helperModel.getHelperCharge());
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

    private void getHelperObjects(final HelperModel helperModel){
        final AVObject campusObject = AVObject.createWithoutData(Constants.TABLE_CAMPUS,helperModel.getObjectId());
        campusObject.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(e == null){
                    campusObj = avObject;
                    final AVObject avUser = AVUser.createWithoutData(Constants.TABLE_USER,helperModel.getUserObjectId());
                    avUser.fetchInBackground(new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            if(e == null){
                                campusUser = avObject;
                                updateStatusToCloud(helperModel,campusObj,campusUser);
                            }else{
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

    protected void failSubmit() {
        final MaterialDialog materialDialog = new MaterialDialog(activity);
        progressBar.setVisibility(View.GONE);
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

    @Override
    public void helpersClick() {
        MLog.i("helper","click");
        if(orderModel.getHelper() != null){
            showFixedHelper();
        }else {
            HelperFragment helperFragment = new HelperFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.PARAM_HELPER_USER, (Serializable) helperModels);
            helperFragment.setArguments(bundle);
            helperFragment.registerSelectedCallBackListener(this, null);
            activity.goToFragment(helperFragment);
        }
    }

    private void showFixedHelper(){
        final View view = activity.getLayoutInflater().inflate(R.layout.helper_info, null);
        final CircleImageView iv_icon = (CircleImageView) view.findViewById(R.id.iv_helper);
        final TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        final TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        final TextView tv_email = (TextView) view.findViewById(R.id.tv_email);
        final TextView tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        final TextView tv_charge = (TextView) view.findViewById(R.id.tv_charge);
        final MaterialDialog materialDialog = new MaterialDialog(activity)
                .setView(view);
        CampusModel campusModel = orderModel.getHelper();
        if(!StringUtil.isNull(campusModel.getUrl())){
            ImageLoader.getInstance().displayImage(campusModel.getUrl(),iv_icon,options);
        }
        tv_name.setText(getString(R.string.helper_name,campusModel.getCampusName()));
        tv_phone.setText(getString(R.string.helper_phone,campusModel.getMobile()));
        tv_email.setText(getString(R.string.helper_email,campusModel.getEmail()));
        if(campusModel.getGenderType() != null) {
            tv_gender.setText(getString(R.string.helper_gender, campusModel.getGenderType().toString()));
        }
        tv_charge.setText(getString(R.string.helper_charge,StringUtil.convertIntegerToString(orderModel.getFinalCharge())));
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
        et_deadline.setEnabled(b);
        et_title.setEnabled(b);
        et_dest.setEnabled(b);
        seekBar.setEnabled(b);
    }


    @Override
    public void onItemSelected(Object selectedItem) {
        if(selectedItem instanceof HelperModel){
            HelperModel helperModel = (HelperModel) selectedItem;
            detailActionItemHolder.changeFixedHelper(helperModel.getCampusName());
            progressBar.setVisibility(View.VISIBLE);
            getHelperObjects(helperModel);
        }
    }
}
