package com.zjut.runner.view.fragments;

import android.os.Bundle;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SaveCallback;
import com.zjut.runner.Controller.CurrentSession;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.ToastUtil;

/**
 * Created by Administrator on 2016/10/21.
 */

public class ChangePhoneFragment extends ValidationCodeFragment {

    private String phoneNumber;

    @Override
    protected void findViews(View rootView) {
        super.findViews(rootView);
        setGettingCaptcha(true);
        countTime();
    }

    @Override
    protected void parseArgument() {
        Bundle bundle = getArguments();
        if(bundle == null){
            return;
        }
        phoneNumber = bundle.getString(Constants.PARAM_ACCOUNT,null);
    }

    @Override
    public void changeTitle() {
        setTitle(R.string.str_phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                String captcha = et_verification.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                AVOSCloud.verifyCodeInBackground(captcha, phoneNumber, new AVMobilePhoneVerifyCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            ToastUtil.showToastShort(activity, R.string.toast_change_phone_success);
                            changePhoneToDB();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            ToastUtil.showToastShort(activity, e.getMessage());
                        }
                    }
                });
                break;
            case R.id.tv_sendagain:
                AVOSCloud.requestSMSCodeInBackground(phoneNumber, new RequestMobileCodeCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            setGettingCaptcha(true);
                            countTime();
                        } else {
                            ToastUtil.showToastShort(activity, e.getMessage());
                        }
                    }
                });
                break;
        }
    }

    private void changePhoneToDB() {
        AVUser.getCurrentUser().setFetchWhenSave(true);
        AVUser.getCurrentUser().setMobilePhoneNumber(phoneNumber);
        AVUser.getCurrentUser().put(Constants.PARAM_VERIFIED,true);
        AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                progressBar.setVisibility(View.GONE);
                if (e == null) {
                    String oldPhone = activity.campusModel.getMobile();
                    activity.campusModel.setMobile(phoneNumber);
                    CurrentSession.updatePhoneWithCache(context, oldPhone, activity.campusModel);
                    activity.goBackByName(PhoneFragment.TAG);
                } else {
                    ToastUtil.showToastShort(activity, e.getMessage());
                }
            }
        });
    }
}
