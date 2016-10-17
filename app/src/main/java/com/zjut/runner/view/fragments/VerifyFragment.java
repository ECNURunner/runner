package com.zjut.runner.view.fragments;

import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.zjut.runner.R;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.util.ToastUtil;

/**
 * Created by Phuylai on 2016/10/18.
 */

public class VerifyFragment extends ResetPasswordFragment {
    @Override
    public void changeTitle() {
        setTitle(R.string.verify_phone);
    }

    @Override
    protected void findViews(View rootView) {
        super.findViews(rootView);
        relativeLayoutResetByPhone.setVisibility(View.VISIBLE);
        relativeLayoutReset.setVisibility(View.GONE);
        buttonReset.setVisibility(View.VISIBLE);
        buttonReset.setText(R.string.btn_verify);
    }

    @Override
    public void onClick(View v) {
        String captcha = et_forget_psd_input_captcha.getText().toString();
        switch (v.getId()) {
            case R.id.btn_reset_psd_reset:
                final String phone = et_forget_psd_phone.getText().toString();
                if(!getCaptchaPhone.equals(phone)){
                    ToastUtil.showToastShort(getActivity(), R.string.now_phone_not_equals_get_captcha_num);
                    return;
                }
                AVUser.verifyMobilePhoneInBackground(captcha, new AVMobilePhoneVerifyCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e == null){
                            activity.overridePendingTransition(R.animator.back_in,
                                    R.animator.back_out);
                            activity.finish();
                        }else{
                            ToastUtil.showToastShort(getActivity(),e.getMessage());
                        }
                    }
                });
                break;
            case R.id.btn_account_activate_get_captcha:
                String phoneNum = et_forget_psd_phone.getText().toString();
                getCaptchaPhone = phoneNum;
                checkIfExist(phoneNum);
                break;
            default:
                break;
        }
    }

    @Override
    protected void checkIfExist(String phoneNum) {
        AVUser.requestMobilePhoneVerifyInBackground(phoneNum,new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    ToastUtil.showToastShort(getActivity(), R.string.register_detail_phone);
                    setGettingCaptcha(true);
                    countTime();
                }else{
                    ToastUtil.showToastShort(getActivity(),e.getMessage());
                }
            }
        });
    }

    @Override
    protected boolean showInputInfo() {
        String capcha = et_forget_psd_input_captcha.getText().toString();
        String phone = et_forget_psd_phone.getText().toString();
        return !(StringUtil.isNull(capcha) || StringUtil.isNull(phone));
    }
}
