package com.zjut.runner.view.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.zjut.runner.Controller.AsyncTaskController;
import com.zjut.runner.Controller.CurrentSession;
import com.zjut.runner.Model.ActionType;
import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.Model.GenderType;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.RunnableManager;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.util.ToastUtil;
import com.zjut.runner.widget.BaseViewHolder;
import com.zjut.runner.widget.DetailActionItemHolder;
import com.zjut.runner.widget.MaterialDialog;
import com.zjut.runner.widget.UserHeaderHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * Created by Administrator on 2016/10/21.
 */

public class UserProfileFragment extends BaseFragment implements
        DetailActionItemHolder.ItemClickListener,Runnable{

    public final static String TAG = PhoneFragment.class.getName();

    private ProgressBar progressBar;
    private Collection<BaseViewHolder> baseViewHolders = new ArrayList<>();
    private LinearLayout bodyView;
    private ScrollView wrapView;
    private View mView;

    private UserHeaderHolder userHeaderHolder;
    private List<DetailActionItemHolder> detailActionItemHolders = new ArrayList<>();
    private List<DetailActionItemHolder> detailCampusItemHolders = new ArrayList<>();

    private static final int REQUEST_IMAGE = 9;

    //load campus model
    private AsyncTask<Object,Void,CampusModel> dbLoad = null;

    //save campus model
    private AsyncTask<Object,Void,Void> dbSave = null;

    // refresh
    protected Handler mUiHandler = new Handler();
    public static final long DELAYMILLIS = 700;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerindicatorEnabled = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutId = R.layout.fragment_profile;
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(layoutId, null);
        }
        findViews(mView);
        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressBar = null;
        if(baseViewHolders.size() > 0){
            baseViewHolders.clear();
            baseViewHolders = null;
        }
        bodyView = null;
        wrapView = null;
        mUiHandler = null;
    }

    @Override
    public void changeTitle() {
        setTitle(R.string.title_profile);
    }

    @Override
    protected void findViews(View rootView) {
        activity.addHeader();
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_sending_post);
        bodyView = (LinearLayout) rootView.findViewById(R.id.ll_details_view);
        wrapView = (ScrollView) rootView.findViewById(R.id.sv_action_area);
        requestModel();
    }

    private void requestModel(){
        refreshList();
        if(!StringUtil.isNull(activity.campusModel.getCampusID())){
            loadCampusInfo();
        }
    }

    private synchronized void loadCampusInfo() {
        if (dbLoad != null) {
            return;
        }
        dbLoad = new AsyncTask<Object, Void, CampusModel>() {
            @Override
            protected CampusModel doInBackground(Object... params) {
                return CurrentSession.getCurrentCampusInfo(activity, activity.campusModel);
            }

            @Override
            protected void onPostExecute(CampusModel campus) {
                dbLoad = null;
                if(campus != null){
                    activity.campusModel = campus;
                }
                refreshList();
            }
        };
        AsyncTaskController.startTask(dbLoad);
    }

    private void setView(){
        removeView();
        setDetail();
    }

    private void removeView(){
        if(bodyView != null){
            bodyView.removeAllViews();
        }
    }

    private void setDetail(){
        if(detailActionItemHolders.size() > 0){
            detailActionItemHolders.clear();
        }
        if(detailCampusItemHolders.size() > 0){
            detailCampusItemHolders.clear();
        }
        addDetail(0,R.string.str_username,activity.campusModel.getUsername(),ActionType.NAME,true);
        addDetail(0,R.string.str_gender,genderString(), ActionType.GENDER,true);
        addDetail(0,R.string.str_phone,activity.campusModel.getMobile(),ActionType.PHONE,true);
        addDetail(0,R.string.str_email,activity.campusModel.getEmail(),ActionType.EMAIL,true);
        addToBody(detailActionItemHolders);
        addGreyLine(GeneralUtils.getDimenPx(activity, R.dimen.super_large_margin));
        if(!StringUtil.isNull(activity.campusModel.getCampusID())){
            addCampusDetail(0, R.string.str_id, activity.campusModel.getCampusID(), null, false);
            addCampusDetail(0,R.string.str_name,activity.campusModel.getCampusName(),null,false);
            addCampusDetail(0,R.string.card_balance,String.valueOf(activity.campusModel.getBalance()),null,false);
            addCampusDetail(0,R.string.str_unbind,null,ActionType.UNBIND,true);
        }else{
            addCampusDetail(0, R.string.str_bind, null, ActionType.BINDING, true);
        }
        addToBody(detailCampusItemHolders);
    }

    private String genderString() {
        String gender = "";
        if(activity.campusModel.getGenderType() != null){
            switch (activity.campusModel.getGenderType()){
                case FEMALE:
                    gender = getString(R.string.str_female);
                    break;
                case MALE:
                    gender = getString(R.string.str_male);
                    break;
            }
        }
        return gender;
    }

    private void addToBody(List<DetailActionItemHolder> list){
        for(DetailActionItemHolder detailActionItemHolder:list){
            addViewHolder(detailActionItemHolder);
            View rootview = detailActionItemHolder.getRootView();
            addView(rootview);
            setMarginTop(GeneralUtils.getDimenPx(activity,R.dimen.margin_zero),rootview);
        }
    }

    private void addDetail(int iconImage,int actionName,String actionDesc,ActionType actionType,
                           boolean clickAble){
        detailActionItemHolders.add(new DetailActionItemHolder(activity, iconImage, actionName,
                actionDesc, actionType, clickAble, this));
    }

    private void addCampusDetail(int iconImage,int actionName,String actionDesc,ActionType actionType,
                                 boolean clickAble){
        detailCampusItemHolders.add(new DetailActionItemHolder(activity, iconImage, actionName,
                actionDesc, actionType, clickAble, this));
    }

    private void addGreyLine(int height){
        View view = new View(activity);
        view.setBackgroundColor(getResources().getColor(R.color.line_gray));
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        addView(view);
    }

    private UserHeaderHolder AddHeader(UserHeaderHolder userHeaderHolder,int marginTop){
        addViewHolder(userHeaderHolder);
        View rootView = userHeaderHolder.getRootView();
        addView(rootView);
        setMarginTop(marginTop, rootView);
        return userHeaderHolder;
    }

    protected void addViewHolder(BaseViewHolder baseViewHolder) {
        if (baseViewHolders == null) {
            return;
        }
        baseViewHolders.add(baseViewHolder);
    }

    protected void addView(View view) {
        if (view == null) {
            return;
        }
        if (bodyView == null) {
            return;
        }
        if(view.getParent() != null){
            bodyView.removeAllViews();
        }
        bodyView.addView(view);
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

    @Override
    protected void setListener() {

    }

    @Override
    public void onSearchClose() {

    }

    @Override
    public void search(String searchString) {

    }

    @Override
    public void nameClick() {
        NameFragment nameFragment = new NameFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM_ACTION, ActionType.NAME.toString());
        bundle.putString(Constants.PARAM_VALUE, activity.campusModel.getUsername());
        nameFragment.setArguments(bundle);
        activity.goToFragment(nameFragment);
    }

    @Override
    public void genderClick() {
        View layout = activity.getLayoutInflater().inflate(R.layout.dialog_gender, null);
        final RadioButton rb_female = (RadioButton) layout.findViewById(R.id.rb_female);
        View rl_female = layout.findViewById(R.id.rl_female);
        View rl_male = layout.findViewById(R.id.rl_male);
        final RadioButton rb_male = (RadioButton) layout.findViewById(R.id.rb_male);
        final MaterialDialog materialDialog = new MaterialDialog(activity);
        setChecked(rb_female, rb_male);
        rl_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update to female
                saveGender("female");
                materialDialog.dismiss();
            }
        });
        rl_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update to male
                saveGender("male");
                materialDialog.dismiss();
            }
        });
        materialDialog.setView(layout);
        materialDialog.setCanceledOnTouchOutside(true);
        materialDialog.show();
    }

    private void setChecked(RadioButton rb_female, RadioButton rb_male) {
        if(rb_female == null || rb_male == null){
            return;
        }
        GenderType genderType = activity.campusModel.getGenderType();
        if(genderType != null){
            switch (genderType){
                case FEMALE:
                    rb_female.setChecked(true);
                    break;
                case MALE:
                    rb_male.setChecked(true);
                    break;
            }
        }
    }

    private void saveGender(final String gender) {
        progressBar.setVisibility(View.VISIBLE);
        AVUser.getCurrentUser().setFetchWhenSave(true);
        AVUser.getCurrentUser().put(Constants.PARAM_GENDER, gender);
        AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    ToastUtil.showToastShort(activity, R.string.toast_success_changed);
                    activity.campusModel.setGenderType(GenderType.getType(gender));
                    CurrentSession.updateGenderWithCache(context, activity.campusModel);
                    progressBar.setVisibility(View.GONE);
                    refreshList();
                } else {
                    ToastUtil.showToastShort(activity, R.string.connection_not_avalible);
                }
            }
        });
    }

    @Override
    public void phoneClick() {
        PhoneFragment phoneFragment = new PhoneFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM_VALUE, activity.campusModel.getMobile());
        phoneFragment.setArguments(bundle);
        activity.goToFragment(phoneFragment,TAG);
    }

    @Override
    public void emailClick() {
        EmailFragment emailFragment = new EmailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM_ACTION,ActionType.NAME.toString());
        bundle.putString(Constants.PARAM_VALUE, activity.campusModel.getEmail());
        emailFragment.setArguments(bundle);
        activity.goToFragment(emailFragment);
    }

    @Override
    public void bindClick() {
        BindFragment bindFragment = new BindFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM_ACTION, ActionType.BINDING.toString());
        bindFragment.setArguments(bundle);
        activity.goToFragment(bindFragment);
    }

    @Override
    public void unbindClick() {
        final MaterialDialog materialDialog = new MaterialDialog(activity)
                .setTitle(R.string.dialog_warning)
                .setMessage(R.string.dialog_unbind)
                .setCanceledOnTouchOutside(false);
        materialDialog.setPositiveButton(getString(R.string.button_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                setUnbind();
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

    private void setUnbind() {
        AVUser.getCurrentUser().setFetchWhenSave(true);
        AVUser.getCurrentUser().put(Constants.PARAM_ID, "");
        AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                progressBar.setVisibility(View.GONE);
                if (e == null) {
                    ToastUtil.showToastShort(activity, R.string.str_unbind);
                    activity.campusModel = CampusModel.setCampusModel(AVUser.getCurrentUser());
                    CurrentSession.removeWithCache(context, activity.campusModel);
                    refreshList();
                } else {
                    ToastUtil.showToastShort(activity, R.string.connection_not_avalible);
                }
            }
        });
    }

    @Override
    public void run() {
        setView();
    }

    private void refreshList(){
        if(mUiHandler == null){
            return;
        }
        RunnableManager.getInstance().postDelayed(this, DELAYMILLIS);
    }

    @Override
    public void onResume() {
        super.onResume();
        changeTitle();
        activity.hideKeyBoard();
        refreshList();
    }
}
