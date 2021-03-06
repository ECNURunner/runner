package com.zjut.runner.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjut.runner.Controller.FragmentFactory;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.Controller;
import me.majiajie.pagerbottomtabstrip.PagerBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.TabItemBuilder;
import me.majiajie.pagerbottomtabstrip.TabLayoutMode;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectListener;

/**
 * Created by Phuylai on 2016/10/25.
 */

public class MyOrderFragment extends BaseFragment {

    protected PagerBottomTabLayout pagerBottomTabLayout;
    protected Controller controller;

    @Override
    public void changeTitle() {
        setTitle(R.string.str_myorder);
    }

    protected void initFragment(int index)
    {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayout,createFragment(index));
        transaction.commit();
    }

    private void goToFragment(int index){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout,createFragment(index));
        transaction.commit();
    }

    protected BaseFragment createFragment(int index){
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.PARAM_NO,index);
        return FragmentFactory.getFragment(Constants.FRAG_ORDER_CONTENT,bundle,null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutId = R.layout.tab_bottom;
        return super.onCreateView(inflater, container, savedInstanceState);
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

        TabItemBuilder completed = new TabItemBuilder(activity).create()
                .setDefaultIcon(R.drawable.ic_action_check)
                .setText(getString(R.string.str_completed))
                .setSelectedColor(ResourceUtil.getColor(R.color.green))
                .setTag("2")
                .build();

        TabItemBuilder cancelled = new TabItemBuilder(activity).create()
                .setDefaultIcon(R.drawable.ic_action_cancel)
                .setText(getString(R.string.str_cancelled))
                .setSelectedColor(ResourceUtil.getColor(R.color.red))
                .setTag("3")
                .build();

        controller = pagerBottomTabLayout.builder()
                .addTabItem(pending)
                .addTabItem(completed)
                .addTabItem(cancelled)
//                .setMode(TabLayoutMode.HIDE_TEXT)
//                .setMode(TabLayoutMode.CHANGE_BACKGROUND_COLOR)
                .setMode(TabLayoutMode.HIDE_TEXT| TabLayoutMode.CHANGE_BACKGROUND_COLOR)
                .build();
        //        controller.setMessageNumber("A",2);
//        controller.setDisplayOval(0,true);
        controller.addTabItemClickListener(listener);
        initFragment(0);
    }

    OnTabItemSelectListener listener = new OnTabItemSelectListener() {
        @Override
        public void onSelected(int index, Object tag)
        {
            goToFragment(index);
        }

        @Override
        public void onRepeatClick(int index, Object tag) {
            Log.i("asd","onRepeatClick:"+index+"   TAG: "+tag.toString());
        }
    };


    @Override
    protected void setListener() {

    }

}
