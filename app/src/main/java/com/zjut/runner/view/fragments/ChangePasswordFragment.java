package com.zjut.runner.view.fragments;

import android.text.InputType;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.zjut.runner.Controller.PasswordController;
import com.zjut.runner.R;
import com.zjut.runner.util.CheckDataUtil;
import com.zjut.runner.util.MyPreference;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.util.ToastUtil;

/**
 * Created by Phuylai on 2016/11/1.
 */

public class ChangePasswordFragment extends BindFragment {

    @Override
    protected void findViews(View rootView) {
        super.findViews(rootView);
        inputOldPass.setVisibility(View.VISIBLE);
        inputID.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD| InputType.TYPE_CLASS_TEXT);
        inputID.setHint(R.string.tv_reset_psd_new);
        inputPass.setHint(R.string.tv_reset_psd_confirm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verificationBtn:
                final String password = inputID.getText().toString();
                final String oldInput = inputOldPass.getText().toString();
                final MyPreference myPreference = MyPreference.getInstance(activity);
                final String oldPass = myPreference.getPassword();
                if(!oldPass.equals(oldInput)){
                    ToastUtil.showToastShort(activity,R.string.toast_old_psd_error);
                    return;
                }
                setEnable(false);
                progressBar.setVisibility(View.VISIBLE);
                AVUser.getCurrentUser().updatePasswordInBackground(oldPass, password, new UpdatePasswordCallback() {
                    @Override
                    public void done(AVException e) {
                        progressBar.setVisibility(View.VISIBLE);
                        if(e == null){
                            ToastUtil.showToastShort(activity,R.string.toast_success_changed);
                            myPreference.setPassword(password);
                            getFragmentManager().popBackStack();
                        }else{
                            setEnable(true);
                            ToastUtil.showToastShort(activity,e.getMessage());
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void changeTitle() {
       setTitle(R.string.str_change_psd);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setActionButtonDisable(showInputInfo());
        setActionButtonDisable(checkInputInfo());
    }
    protected boolean showInputInfo() {
        String newPassword = inputID.getText().toString();
        if(StringUtil.notNull(newPassword) && !CheckDataUtil.checkPassWord(newPassword)){
            PasswordController.setWarningText(tv_warning,
                    R.string.toast_password_format_errormsg);
            PasswordController.setWarningTopMarginByRelativeLayout(
                    activity, tv_warning);
            return false;
        }
        String confirmPassWord = inputPass.getText().toString();
        if (StringUtil.notNull(confirmPassWord)
                && !confirmPassWord.equals(newPassword)) {
            PasswordController.setWarningText(tv_warning,
                    R.string.toast_two_password_errormsg);
            PasswordController.setWarningTopMarginByRelativeLayout(
                    activity, tv_warning);
            return false;
        }
        PasswordController.hiddenWarning(tv_warning);
        if (StringUtil.isNull(newPassword)) {
            return false;
        }
        return !StringUtil.isNull(confirmPassWord);
    }

    @Override
    protected void setEnable(boolean enable) {
        inputID.setEnabled(enable);
        inputPass.setEnabled(enable);
        inputOldPass.setEnabled(enable);
        btn_submit.setEnabled(enable);
    }

    @Override
    protected boolean checkInputInfo() {
        String newPsd = inputID.getText().toString();
        String confirm = inputPass.getText().toString();
        String oldPsd = inputOldPass.getText().toString();
        if (StringUtil.isNull(oldPsd)) {
            return false;
        }
        if (StringUtil.isNull(newPsd)
                || !CheckDataUtil.checkPassWord(newPsd)) {
            return false;
        }
        if (StringUtil.isNull(confirm)
                || !confirm.equals(newPsd)) {
            return false;
        }
        return !StringUtil.isNull(oldPsd);
    }

}
