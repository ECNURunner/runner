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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.zjut.runner.Model.AccountModel;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.MLog;
import com.zjut.runner.util.MyPreference;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.util.ToastUtil;
import com.zjut.runner.view.activities.RegisterActivity;

/**
 * Created by Administrator on 2016/10/17.
 */

public class ValidationCodeFragment extends BaseFragment implements View.OnClickListener, TextWatcher {
    private static final String TAG = ValidationCodeFragment.class.getName();

    private Button btn_register;
    private TextView tv_sendagain;
    private TextView tv_detail;
    protected EditText et_verification;
    protected ProgressBar progressBar;

    private static final int ONE_SECOND = 1000;
    private static final int MINUTE_SECOND = 60;
    private int secondCount = MINUTE_SECOND;

    //account
    private AccountModel accountModel;
    private String password;
    private String username;
    private String mobile;

    // SEND AGAIN
    private boolean isRequestInvtionAgain = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArgument();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutId = R.layout.fragment_validation;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void parseArgument(){
        Bundle bundle = getArguments();
        if(bundle == null){
            return;
        }
        accountModel = (AccountModel) bundle.getSerializable(Constants.PARAM_ACCOUNT);
        if(accountModel != null){
            mobile = accountModel.getMobile();
            username = accountModel.getUsername();
            password = accountModel.getPassword();
        }
    }

    @Override
    public void changeTitle() {
        setTitle(R.string.str_register);
    }

    @Override
    protected void findViews(View rootView) {
        btn_register = (Button) rootView.findViewById(R.id.btn_next);
        tv_sendagain = (TextView) rootView.findViewById(R.id.tv_sendagain);
        et_verification = (EditText) rootView.findViewById(R.id.et_code);
        tv_detail = (TextView) rootView.findViewById(R.id.tv_detail);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_sending_post);
        setGettingCaptcha(true);
        countTime();
        updateDetailInfo();
        setActionButtonDisable(checkInputInfo());
    }

    private void updateDetailInfo(){
        if(tv_detail == null){
            return;
        }
        if(StringUtil.notNull(mobile)){
            tv_detail.setText(R.string.register_detail_phone);
        }
    }

    @Override
    protected void setListener() {
        tv_sendagain.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        et_verification.addTextChangedListener(this);
    }

    protected void setActionButtonDisable(boolean isEnable) {

        btn_register.setEnabled(isEnable);

        if (isEnable) {
            btn_register.getBackground().setAlpha(Constants.DEF_OPAQUE);
        } else {
            btn_register.getBackground().setAlpha(Constants.DISABLE_OPAQUE);
        }
    }

    protected boolean checkInputInfo() {
        String code = et_verification.getText().toString();
        return !StringUtil.isNull(code);
    }

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ValidationCodeFragment fragment = null;
            TextView textView = null;
            switch (msg.what) {
                case Constants.SECOND_TICK:
                    fragment = (ValidationCodeFragment) msg.obj;
                    textView = fragment.getTextGetCaptcha();
                    textView.setClickable(false);
                    textView.setBackgroundResource(R.color.gray);
                    Activity activity = fragment.getActivity();
                    if (activity == null) {
                        return;
                    }
                    String strFormat = getString(
                            R.string.str_resend_captcha);
                    String strResult = String.format(strFormat,
                            fragment.getSecondCount());
                    textView.setText(strResult);
                    break;
                case Constants.TIMER_END:
                    fragment = (ValidationCodeFragment) msg.obj;
                    textView = fragment.getTextGetCaptcha();
                    textView.setClickable(true);
                    textView.setBackgroundResource(0);
                    textView.setText(R.string.register_sendagain);
                    fragment.setGettingCaptcha(false);
                    fragment.setSecondCount(MINUTE_SECOND);
                    break;
            }
        }
    };

    private TextView getTextGetCaptcha() {
        return tv_sendagain;
    }

    protected void setGettingCaptcha(boolean isGettingCaptcha) {
        this.isRequestInvtionAgain = isGettingCaptcha;
    }

    private void setSecondCount(int secondCount) {
        this.secondCount = secondCount;
    }

    private int getSecondCount() {
        return secondCount;
    }

    public void countTime() {
        new Thread(new timerCounter()).start();
    }

    private class timerCounter implements Runnable {
        @Override
        public void run() {
            while (true) {
                Message message = new Message();
                message.obj = ValidationCodeFragment.this;
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


    @Override
    public void onSearchClose() {

    }

    @Override
    public void search(String searchString) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                String captcha = et_verification.getText().toString();
                AVUser.verifyMobilePhoneInBackground(captcha, new AVMobilePhoneVerifyCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e == null){
                            MyPreference myPreference = MyPreference.getInstance(activity);
                            if(myPreference != null){
                                myPreference.setUsername(mobile);
                                myPreference.setPassword(password);
                            }
                            if(activity != null){
                                activity.setResult(RegisterActivity.RESULT_REGISTER_SUCCESS);
                                activity.finish();
                            }
                        }else{
                            ToastUtil.showToastShort(activity,e.getMessage());
                        }
                    }
                });
                break;
            case R.id.tv_sendagain:
                AVUser.requestMobilePhoneVerifyInBackground(mobile, new RequestMobileCodeCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e == null) {
                            //start timer
                            setGettingCaptcha(true);
                            countTime();
                        }else{
                            ToastUtil.showToastShort(activity,e.getMessage());
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        setActionButtonDisable(checkInputInfo());
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
