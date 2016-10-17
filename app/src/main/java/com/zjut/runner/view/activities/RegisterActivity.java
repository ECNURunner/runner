package com.zjut.runner.view.activities;

import com.zjut.runner.view.fragments.RegisterFragment;

/**
 * Created by Administrator on 2016/10/17.
 */

public class RegisterActivity extends MainActivity {
    public static final int REQUEST_REGISTER_CODE = 1;
    public static final int RESULT_REGISTER_SUCCESS = 2;

    @Override
    protected void initFragment() {
        RegisterFragment registerFragment = new RegisterFragment();
        goToFragment(registerFragment,false);
    }
}
