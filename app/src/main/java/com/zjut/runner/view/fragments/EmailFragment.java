package com.zjut.runner.view.fragments;

import android.text.InputType;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.zjut.runner.Controller.CurrentSession;
import com.zjut.runner.R;
import com.zjut.runner.util.CheckDataUtil;
import com.zjut.runner.util.ToastUtil;

/**
 * Created by Administrator on 2016/10/21.
 */

public class EmailFragment extends NameFragment {

    @Override
    protected void findViews(View rootView) {
        super.findViews(rootView);
        inputID.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        inputID.setHint(R.string.str_email);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verificationBtn:
                final String name = inputID.getText().toString();
                if(!CheckDataUtil.isTrueEmail(name)){
                    ToastUtil.showToastShort(getActivity(),
                            R.string.toast_email_isnot_effective);
                    return;
                }
                AVUser.getCurrentUser().setFetchWhenSave(true);
                AVUser.getCurrentUser().setEmail(name);
                AVUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            ToastUtil.showToastShort(activity, R.string.toast_success_changed);
                            activity.campusModel.setEmail(name);
                            CurrentSession.updateEmailWithCache(context, activity.campusModel);
                            getFragmentManager().popBackStack();
                        } else {
                            ToastUtil.showToastShort(activity, R.string.connection_not_avalible);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void changeTitle() {
        activity.changeTitle(R.string.str_email);
    }
}
