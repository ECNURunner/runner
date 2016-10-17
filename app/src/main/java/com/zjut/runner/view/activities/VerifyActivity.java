package com.zjut.runner.view.activities;

import com.zjut.runner.R;
import com.zjut.runner.util.ResourceUtil;
import com.zjut.runner.view.fragments.VerifyFragment;

/**
 * Created by Phuylai on 2016/10/18.
 */

public class VerifyActivity extends MainActivity {

    public static final int REQUEST_REGISTER_CODE = 1;
    public static final int RESULT_REGISTER_SUCCESS = 2;

    @Override
    protected void initFragment() {
        toolbar.setBackgroundColor(ResourceUtil.getColor(R.color.colorPrimary));
        VerifyFragment verifyFragment = new VerifyFragment();
        goToFragment(verifyFragment,false);
    }
}
