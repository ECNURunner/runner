package com.zjut.runner.view.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
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
import com.zjut.runner.Controller.DetailItemMaker;
import com.zjut.runner.Controller.FragmentFactory;
import com.zjut.runner.Model.ActionType;
import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.Model.GenderType;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.ResourceUtil;
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
    private View mView;

    private List<BaseViewHolder> detailActionItemHolders = new ArrayList<>();

    //load campus model
    private AsyncTask<Object,Void,CampusModel> dbLoad = null;

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
        mUiHandler = null;
    }

    @Override
    public void changeTitle() {
        setTitle(R.string.title_profile);
    }

    @Override
    protected void findViews(View rootView) {
        activity.addHeader();
        activity.expandToolbar(true);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_sending_post);
        bodyView = (LinearLayout) rootView.findViewById(R.id.ll_details_view);
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
        DetailItemMaker detailItemMaker = new DetailItemMaker(activity,activity.campusModel.getUsername(),this);
        detailActionItemHolders.add(detailItemMaker.getDetailActionItemHolder());
        detailActionItemHolders.add(detailItemMaker.genderItem(StringUtil.genderString(activity,
                activity.campusModel.getGenderType())));
        detailActionItemHolders.add(detailItemMaker.phoneItem(activity.campusModel.getMobile()));
        detailActionItemHolders.add(detailItemMaker.emailItem(activity.campusModel.getEmail()));
        addToBody(detailActionItemHolders);
        ResourceUtil.addGreyLine(activity,bodyView,
                GeneralUtils.getDimenPx(activity,R.dimen.super_large_margin));
        if(!StringUtil.isNull(activity.campusModel.getCampusID())){
            detailActionItemHolders.clear();
            detailActionItemHolders.add(detailItemMaker.campusID(activity.campusModel.getCampusID()));
            detailActionItemHolders.add(detailItemMaker.campusName(activity.campusModel.getCampusName()));
            detailActionItemHolders.add(detailItemMaker.campusBalance(String.valueOf(activity.
                    campusModel.getBalance())));
            detailActionItemHolders.add(detailItemMaker.campusUnbind());
        }else{
            detailActionItemHolders.add(detailItemMaker.campusBind());
        }
        addToBody(detailActionItemHolders);
    }

    private void addToBody(List<BaseViewHolder> list){
        for(BaseViewHolder detailActionItemHolder:list){
            addViewHolder(detailActionItemHolder);
            View rootview = detailActionItemHolder.getRootView();
            ResourceUtil.addView(bodyView,rootview);
            ResourceUtil.setMarginTop(GeneralUtils.getDimenPx(activity,R.dimen.margin_zero),rootview);
        }
    }

    protected void addViewHolder(BaseViewHolder baseViewHolder) {
        if (baseViewHolders == null) {
            return;
        }
        baseViewHolders.add(baseViewHolder);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void nameClick() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM_ACTION, ActionType.NAME.toString());
        bundle.putString(Constants.PARAM_VALUE, activity.campusModel.getUsername());
        activity.goToFragment(FragmentFactory.getFragment(Constants.FRAG_CHANGE_NAME,bundle,null));
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
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM_ACTION,ActionType.NAME.toString());
        bundle.putString(Constants.PARAM_VALUE, activity.campusModel.getEmail());
        activity.goToFragment(FragmentFactory.getFragment(Constants.FRAG_CHANGE_EMAIL,bundle,null));
    }

    @Override
    public void bindClick() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAM_ACTION, ActionType.BINDING.toString());
        activity.goToFragment(FragmentFactory.getFragment(Constants.FRAG_BIND,bundle,null));
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

    @Override
    public boolean onBackPressed() {
        activity.removeHeader();
        return super.onBackPressed();
    }

}
