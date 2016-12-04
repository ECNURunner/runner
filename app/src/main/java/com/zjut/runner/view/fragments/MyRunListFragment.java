package com.zjut.runner.view.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.zjut.runner.Controller.FragmentFactory;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.ResourceUtil;

import me.majiajie.pagerbottomtabstrip.PagerBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.TabItemBuilder;
import me.majiajie.pagerbottomtabstrip.TabLayoutMode;

/**
 * Created by Phuylai on 2016/10/31.
 */

public class MyRunListFragment extends MyOrderFragment {

    @Override
    protected BaseFragment createFragment(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.PARAM_NO,index);
        return FragmentFactory.getFragment(Constants.FRAG_RUN_CONTENT,bundle,null);
    }

    @Override
    public void changeTitle() {
        setTitle(R.string.title_run_list);
    }

    @Override
    protected void findViews(View rootView) {
        pagerBottomTabLayout = (PagerBottomTabLayout) rootView.findViewById(R.id.tab);
        TabItemBuilder pending = new TabItemBuilder(activity).create()
                .setDefaultIcon(R.drawable.ic_action_pending)
                .setText(getString(R.string.str_pending))
                .setSelectedColor(ResourceUtil.getColor(R.color.colorPrimary))
                .setTag("1")
                .build();

        TabItemBuilder go = new TabItemBuilder(activity).create()
                .setDefaultIcon(R.drawable.ic_action_man)
                .setText(getString(R.string.str_go_help))
                .setSelectedColor(ResourceUtil.getColor(R.color.colorPrimaryDark))
                .setTag("2")
                .build();

        TabItemBuilder completed = new TabItemBuilder(activity).create()
                .setDefaultIcon(R.drawable.ic_action_check)
                .setText(getString(R.string.str_completed))
                .setSelectedColor(ResourceUtil.getColor(R.color.green))
                .setTag("3")
                .build();

        TabItemBuilder cancelled = new TabItemBuilder(activity).create()
                .setDefaultIcon(R.drawable.ic_action_cancel)
                .setText(getString(R.string.str_cancelled))
                .setSelectedColor(ResourceUtil.getColor(R.color.gray))
                .setTag("4")
                .build();

        TabItemBuilder rejeted = new TabItemBuilder(activity).create()
                .setDefaultIcon(R.drawable.ic_action_document)
                .setText(getString(R.string.str_rejected))
                .setSelectedColor(ResourceUtil.getColor(R.color.red))
                .setTag("5")
                .build();

        controller = pagerBottomTabLayout.builder()
                .addTabItem(pending)
                .addTabItem(go)
                .addTabItem(completed)
                .addTabItem(cancelled)
                .addTabItem(rejeted)
                .setMode(TabLayoutMode.HIDE_TEXT| TabLayoutMode.CHANGE_BACKGROUND_COLOR)
                .build();
        controller.addTabItemClickListener(listener);
        controller.setSelect(1);
        initFragment(1);
    }

}
