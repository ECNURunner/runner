package com.zjut.runner.view.Adapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zjut.runner.Model.BannerModel;
import com.zjut.runner.view.fragments.HeaderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuylai on 2016/10/23.
 */

public class HeaderAdapter extends FragmentStatePagerAdapter {

    private List<BannerModel> resourceIds = new ArrayList<BannerModel>();

    public HeaderAdapter(FragmentManager fm, List<BannerModel> resourceIds) {
        super(fm);
        this.resourceIds = resourceIds;
    }

    @Override
    public Fragment getItem(int position) {
        HeaderFragment HeaderFragment = new HeaderFragment();
        Bundle data = new Bundle();
        data.putSerializable(com.zjut.runner.view.fragments.HeaderFragment.PARAM_RESID, resourceIds.get(position));
        HeaderFragment.setArguments(data);
        return HeaderFragment;
    }
    @Override
    public int getCount() {
        return resourceIds.size();
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}

