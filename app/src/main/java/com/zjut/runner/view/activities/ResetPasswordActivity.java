package com.zjut.runner.view.activities;

import com.zjut.runner.view.fragments.ResetPasswordFragment;

/**
 * Created by Phuylai on 2016/10/18.
 */

public class ResetPasswordActivity extends MainActivity {
    @Override
    protected void initFragment() {
        ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
        goToFragment(resetPasswordFragment,false);
    }
}
