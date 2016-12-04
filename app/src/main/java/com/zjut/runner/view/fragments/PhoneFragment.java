package com.zjut.runner.view.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.zjut.runner.Controller.FragmentFactory;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.ToastUtil;

/**
 * Created by Administrator on 2016/10/21.
 */

public class PhoneFragment extends NameFragment {

    public final static String TAG = PhoneFragment.class.getName();

    @Override
    protected void findViews(View rootView) {
        super.findViews(rootView);
        inputID.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL |
                InputType.TYPE_NUMBER_FLAG_SIGNED);
        inputID.setHint(R.string.str_phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verificationBtn:
                final String name = inputID.getText().toString();
                mobilePhoneVerify(name);
                break;
        }
    }

    private void mobilePhoneVerify(final String name) {
        AVOSCloud.requestSMSCodeInBackground(name, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.PARAM_ACCOUNT, name);
                    activity.goToFragment(FragmentFactory.getFragment(Constants.FRAG_CHANGE_PHONE,
                            bundle,null));
                }else{
                    ToastUtil.showToastShort(activity,e.getMessage());
                }
            }
        });
    }

    @Override
    public void changeTitle() {
        setTitle(R.string.str_phone);
    }
}