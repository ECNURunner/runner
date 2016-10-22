package com.zjut.runner.view.activities;

import com.zjut.runner.R;
import com.zjut.runner.util.ResourceUtil;
import com.zjut.runner.view.fragments.ResetPasswordFragment;

/**
 * Created by Phuylai on 2016/10/18.
 */

public class ResetPasswordActivity extends MainActivity {

    @Override
    protected void initHeaderView() {

    }

    @Override
    protected void setHeaderClick() {

    }

    @Override
    protected void initFragment() {
        toolbar.setBackgroundColor(ResourceUtil.getColor(R.color.colorPrimary));
        ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
        goToFragment(resetPasswordFragment,false);
    }
}
