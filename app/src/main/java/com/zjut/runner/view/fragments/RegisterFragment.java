package com.zjut.runner.view.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.zjut.runner.Controller.PasswordController;
import com.zjut.runner.Controller.ViewController;
import com.zjut.runner.Model.AccountModel;
import com.zjut.runner.R;
import com.zjut.runner.util.CheckDataUtil;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.util.ToastUtil;

/**
 * Created by Administrator on 2016/10/17.
 */

public class RegisterFragment extends BaseFragment implements TextWatcher, View.OnClickListener {
    private EditText et_username;
    private EditText et_password;
    private EditText et_confirm_password;
    protected TextView tvWarning;
    protected EditText et_mobile;
    private Button btn_next;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutId = R.layout.fragment_register;
        drawerindicatorEnabled = false;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void findViews(View rootView) {
        tvWarning = (TextView) rootView.findViewById(R.id.tv_warning);
        et_username = (EditText) rootView.findViewById(R.id.et_username);
        et_password = (EditText) rootView.findViewById(R.id.et_password);
        et_confirm_password = (EditText)
                rootView.findViewById(R.id.et_confirm_password);
        et_mobile = (EditText) rootView.findViewById(R.id.et_mobile);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        GeneralUtils.setPassWordEditTextHintType(et_password);
        GeneralUtils.setPassWordEditTextHintType(et_confirm_password);
        setEditTextInputLength();

        setActionButtonDisable(checkInputInfo());
    }

    @Override
    protected void setListener() {
        btn_next.setOnClickListener(this);
        et_username.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_confirm_password.addTextChangedListener(this);
        et_mobile.addTextChangedListener(this);
    }

    protected void setEditTextInputLength() {
        ViewController.setEditTextMaxLength(et_username,
                Constants.FULLNAME_LENGTH);
        ViewController.setEditTextMaxLength(et_password,
                Constants.PASSWORD_MAXNUM);
        ViewController.setEditTextMaxLength(et_confirm_password,
                Constants.PASSWORD_MAXNUM);
    }

    @Override
    public void changeTitle() {
       setTitle(R.string.str_register);
    }

    protected void setActionButtonDisable(boolean isEnable) {

        btn_next.setEnabled(isEnable);

        if (isEnable) {
            btn_next.getBackground().mutate().setAlpha(Constants.DEF_OPAQUE);
        } else {
            btn_next.getBackground().mutate().setAlpha(Constants.DISABLE_OPAQUE);
        }
    }


    @Override
    public void onSearchClose() {

    }

    @Override
    public void search(String searchString) {

    }

    protected boolean checkInputInfo() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        String phone = et_mobile.getText().toString();
        String comfirm_password = et_confirm_password.getText().toString();
        if (StringUtil.isNull(username)) {
            return false;
        }
        if (StringUtil.isNull(password)
                || !CheckDataUtil.checkPassWord(password)) {
            return false;
        }
        if (StringUtil.isNull(comfirm_password)
                || !comfirm_password.equals(password)) {
            return false;
        }
        return !StringUtil.isNull(phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                registerAccount();
                break;
            default:
                break;
        }
    }

    private void registerAccount(){
        String userName = et_username.getText().toString();
        String mobile = et_mobile.getText().toString();
        String password = et_password.getText().toString();
        setEnabled(false);
        registerUser(userName, mobile, password);
    }

    private void registerUser(final String username, final String mobile, final String password) {
        AVUser user = new AVUser();
        user.setUsername(username);
        user.setMobilePhoneNumber(mobile);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    AccountModel model = new AccountModel(username, mobile, password);
                    ValidationCodeFragment validationCodeFragment = new ValidationCodeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.PARAM_ACCOUNT, model);
                    validationCodeFragment.setArguments(bundle);
                    activity.goToFragment(validationCodeFragment);
                } else {
                    ToastUtil.showToastShort(getActivity(), e.getMessage());
                    setEnabled(true);
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setActionButtonDisable(checkInputInfo());
        showInputInfo();
    }

    protected boolean showInputInfo() {
        String passWord = et_password.getText().toString();
        if (StringUtil.notNull(passWord)
                && !CheckDataUtil.checkPassWord(passWord)) {
            PasswordController.setWarningText(tvWarning,
                    R.string.toast_password_format_errormsg);
            PasswordController.setWarningTopMarginByLinearLayout(
                    activity, tvWarning);
            return false;
        }
        String confirmPassWord = et_confirm_password.getText().toString();
        if (StringUtil.notNull(confirmPassWord)
                && !confirmPassWord.equals(passWord)) {
            PasswordController.setWarningText(tvWarning,
                    R.string.toast_two_password_errormsg);
            PasswordController.setWarningTopMarginByLinearLayout(
                    activity, tvWarning);
            return false;
        }
        PasswordController.hiddenWarning(tvWarning);
        return true;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void setEnabled(boolean enabled) {
        et_username.setEnabled(enabled);
        et_password.setEnabled(enabled);
        et_confirm_password.setEnabled(enabled);
        et_mobile.setEnabled(enabled);
        btn_next.setEnabled(enabled);
    }
}
