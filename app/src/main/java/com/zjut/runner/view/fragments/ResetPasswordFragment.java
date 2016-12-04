package com.zjut.runner.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.zjut.runner.Controller.PasswordController;
import com.zjut.runner.R;
import com.zjut.runner.util.CheckDataUtil;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.MLog;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.util.ToastUtil;

/**
 * Created by Phuylai on 2016/10/18.
 */

public class ResetPasswordFragment extends BaseFragment implements View.OnClickListener, TextWatcher {

    private static final String TAG = ResetPasswordFragment.class.getName();

    private View resetPsdPhoneView;
    protected RelativeLayout relativeLayoutResetByPhone;
    protected RelativeLayout relativeLayoutReset;
    private Button buttonGetCaptcha;

    protected Button buttonReset;

    protected EditText et_forget_psd_phone;
    protected EditText et_forget_psd_input_captcha;

    protected EditText et_reset_psd_new;
    protected EditText et_reset_psd_confirm;

    private TextView tv_warning_reset;

    private boolean isGettingCaptcha = false;
    private static final int ONE_SECOND = 1000;
    private static final int MINUTE_SECOND = 60;
    private int secondCount = MINUTE_SECOND;

    protected String getCaptchaPhone;
    private String phoneNumber;

    //handle the messages
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ResetPasswordFragment fragment = null;
            Button button = null;
            switch (msg.what) {
                case Constants.SECOND_TICK:
                    fragment = (ResetPasswordFragment) msg.obj;
                    button = fragment.getButtonGetCaptcha();
                    button.setClickable(false);
                    button.setBackgroundResource(R.color.gray);
                    Activity activity = fragment.getActivity();
                    if (activity == null) {
                        return;
                    }
                    String strFormat = getString(R.string.str_resend_captcha);
                    String strResult = String.format(strFormat,
                            fragment.getSecondCount());
                    button.setText(strResult);
                    break;
                case Constants.TIMER_END:
                    fragment = (ResetPasswordFragment) msg.obj;
                    button = fragment.getButtonGetCaptcha();
                    button.setClickable(true);
                    button.setBackgroundResource(R.color.colorPrimary);
                    button.getBackground().mutate().setAlpha(Constants.DEF_OPAQUE);
                    button.setText(R.string.str_get_captcha);
                    fragment.setGettingCaptcha(false);
                    fragment.setSecondCount(MINUTE_SECOND);
                    break;
            }
        }
    };

    private Button getButtonGetCaptcha() {
        return buttonGetCaptcha;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutId = R.layout.fragment_reset_password;
        drawerindicatorEnabled = false;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void changeTitle() {
        setTitle(R.string.str_reset_psd_reset);
    }

    @Override
    protected void findViews(View rootView) {
        resetPsdPhoneView = rootView;
        buttonReset = (Button) resetPsdPhoneView
                .findViewById(R.id.btn_reset_psd_reset);
        buttonGetCaptcha = (Button) resetPsdPhoneView
                .findViewById(R.id.btn_account_activate_get_captcha);
        relativeLayoutResetByPhone = (RelativeLayout) resetPsdPhoneView
                .findViewById(R.id.rl_forget_psd_by_phone);
        relativeLayoutReset = (RelativeLayout) resetPsdPhoneView
                .findViewById(R.id.rl_reset_psd);

        et_forget_psd_phone = (EditText) resetPsdPhoneView
                .findViewById(R.id.et_forget_psd_phone);
        et_forget_psd_input_captcha = (EditText) resetPsdPhoneView
                .findViewById(R.id.et_forget_psd_input_captcha);

        et_reset_psd_new = (EditText) resetPsdPhoneView
                .findViewById(R.id.et_reset_psd_new);
        et_reset_psd_confirm = (EditText) resetPsdPhoneView
                .findViewById(R.id.et_reset_psd_confirm);
        tv_warning_reset = (TextView) resetPsdPhoneView
                .findViewById(R.id.tv_warning_reset);

        setActionButtonDisable(checkCapcha(),buttonGetCaptcha);
        setActionButtonDisable(checkPassword(), buttonReset);
    }

    protected void setActionButtonDisable(boolean isEnable,Button button) {

        button.setEnabled(isEnable);

        if (isEnable) {
            button.getBackground().mutate().setAlpha(Constants.DEF_OPAQUE);
        } else {
            button.getBackground().mutate().setAlpha(Constants.DISABLE_OPAQUE);
        }
    }

    protected boolean showInputInfo() {
        String phoneNumber = et_forget_psd_phone.getText().toString();
        String newPassword = et_reset_psd_new.getText().toString();
        if(StringUtil.notNull(newPassword) && !CheckDataUtil.checkPassWord(newPassword)){
            PasswordController.setWarningText(tv_warning_reset,
                    R.string.toast_password_format_errormsg);
            PasswordController.setWarningTopMarginByLinearLayout(
                    activity, tv_warning_reset);
            return false;
        }
        String confirmPassWord = et_reset_psd_confirm.getText().toString();
        if (StringUtil.notNull(confirmPassWord)
                && !confirmPassWord.equals(newPassword)) {
            PasswordController.setWarningText(tv_warning_reset,
                    R.string.toast_two_password_errormsg);
            PasswordController.setWarningTopMarginByLinearLayout(
                    activity, tv_warning_reset);
            return false;
        }
        PasswordController.hiddenWarning(tv_warning_reset);
        String capcha = et_forget_psd_input_captcha.getText().toString();
        return !(StringUtil.isNull(newPassword) || StringUtil.isNull(confirmPassWord)
                || StringUtil.isNull(capcha) || StringUtil.isNull(phoneNumber));
    }

    protected boolean checkInputInfo() {
        String phoneNumber = et_forget_psd_phone.getText().toString();
        String capcha = et_forget_psd_input_captcha.getText().toString();
        if (StringUtil.isNull(phoneNumber)) {
            return false;
        }
        return !StringUtil.isNull(capcha);
    }

    protected boolean checkCapcha(){
        String phoneNumber = et_forget_psd_phone.getText().toString();
        return !StringUtil.isNull(phoneNumber);
    }

    protected boolean checkPassword(){
        String password = et_reset_psd_new.getText().toString();
        String confirm_password = et_reset_psd_confirm.getText().toString();
        String capcha = et_forget_psd_input_captcha.getText().toString();
        return !(StringUtil.isNull(password) || StringUtil.isNull(confirm_password)
                || StringUtil.isNull(capcha));
    }

    @Override
    protected void setListener() {
        buttonReset.setOnClickListener(this);
        buttonGetCaptcha.setOnClickListener(this);
        et_forget_psd_input_captcha.addTextChangedListener(this);
        et_forget_psd_phone.addTextChangedListener(this);
        et_reset_psd_new.addTextChangedListener(this);
        et_reset_psd_confirm.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        String captcha = et_forget_psd_input_captcha.getText().toString();
        switch (v.getId()) {
            case R.id.btn_reset_psd_reset:
                final String confirmPsd = et_reset_psd_confirm.getText().toString();
                final String phone = et_forget_psd_phone.getText().toString();
                if(getCaptchaPhone != null && !getCaptchaPhone.equals(phone)){
                    ToastUtil.showToastShort(getActivity(),R.string.now_phone_not_equals_get_captcha_num);
                    return;
                }
                AVUser.resetPasswordBySmsCodeInBackground(captcha, confirmPsd, new UpdatePasswordCallback() {
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

    protected void checkIfExist(final String phoneNum) {
        AVUser.requestPasswordResetBySmsCodeInBackground(phoneNum, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    ToastUtil.showToastShort(getActivity(), R.string.register_detail_phone);
                    setGettingCaptcha(true);
                    countTime();
                } else {
                    ToastUtil.showToastShort(getActivity(), e.getMessage());
                }
            }
        });

    }

    private class timerCounter implements Runnable {
        @Override
        public void run() {
            while (true) {
                Message message = new Message();
                message.obj = ResetPasswordFragment.this;
                if (secondCount == 0) {
                    message.what = Constants.TIMER_END;
                    handler.sendMessage(message);
                    return;
                }
                secondCount--;
                message.what = Constants.SECOND_TICK;
                handler.sendMessage(message);
                try {
                    Thread.sleep(ONE_SECOND);
                } catch (InterruptedException e) {
                    MLog.e(TAG, e);
                }
            }
        }
    }

    public void countTime() {
        new Thread(new timerCounter()).start();
    }

    public boolean isGettingCaptcha() {
        return isGettingCaptcha;
    }

    public void setGettingCaptcha(boolean isGettingCaptcha) {
        this.isGettingCaptcha = isGettingCaptcha;
    }

    private int getSecondCount() {
        return secondCount;
    }

    private void setSecondCount(int secondCount) {
        this.secondCount = secondCount;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setActionButtonDisable(checkCapcha(),buttonGetCaptcha);
        setActionButtonDisable(showInputInfo(), buttonReset);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
