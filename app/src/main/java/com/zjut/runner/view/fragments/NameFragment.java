package com.zjut.runner.view.fragments;

import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.zjut.runner.Controller.CurrentSession;
import com.zjut.runner.Controller.ViewController;
import com.zjut.runner.Model.RefreshType;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.util.ToastUtil;

/**
 * Created by Administrator on 2016/10/21.
 */

public class NameFragment extends BindFragment {

    @Override
    protected void findViews(View rootView) {
        super.findViews(rootView);
        ViewController.setVisibility(inputPass, View.GONE);
        ViewController.setVisibility(tv_warning,View.GONE);
        inputID.setHint(R.string.str_register_username);
        inputID.setText(defValue);
        ViewController.setEditTextMaxLength(inputID,
                Constants.FULLNAME_LENGTH);
    }

    @Override
    protected boolean checkInputInfo() {
        String first = inputID.getText().toString();

        if (StringUtil.isNull(first)) {
            return false;
        }
        return !first.equals(defValue);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verificationBtn:
                final String name = inputID.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                AVUser.getCurrentUser().setFetchWhenSave(true);
                AVUser.getCurrentUser().setUsername(name);
                AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            progressBar.setVisibility(View.GONE);
                            ToastUtil.showToastShort(activity, R.string.toast_success_changed);
                            activity.campusModel.setUsername(name);
                            CurrentSession.updateNameWithCache(context, activity.campusModel);
                            activity.refreshNavView(RefreshType.NAME,name);
                            getFragmentManager().popBackStack();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            ToastUtil.showToastShort(activity, R.string.connection_not_avalible);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void changeTitle() {
        activity.changeTitle(R.string.str_username);
    }
}