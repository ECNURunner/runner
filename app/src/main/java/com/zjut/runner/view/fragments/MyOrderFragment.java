package com.zjut.runner.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjut.runner.R;
import com.zjut.runner.util.ResourceUtil;

import me.majiajie.pagerbottomtabstrip.Controller;
import me.majiajie.pagerbottomtabstrip.PagerBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.TabItemBuilder;
import me.majiajie.pagerbottomtabstrip.TabLayoutMode;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectListener;

/**
 * Created by Phuylai on 2016/10/25.
 */

public class MyOrderFragment extends BaseFragment {

    private PagerBottomTabLayout pagerBottomTabLayout;
    private Controller controller;

    //List<Fragment> mFragments;

    @Override
    public void changeTitle() {
        activity.changeTitle("my order");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initFragment();
    }

    /*private void initFragment()
    {
        mFragments = new ArrayList<>();

        mFragments.add(createFragment("A"));
        mFragments.add(createFragment("B"));
        mFragments.add(createFragment("C"));
        mFragments.add(createFragment("D"));
        mFragments.add(createFragment("E"));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // transaction.setCustomAnimations(R.anim.push_up_in,R.anim.push_up_out);
        transaction.add(R.id.frameLayout,mFragments.get(0));
        transaction.commit();
    }*/

    /*private Fragment createFragment(String content)
    {
        AFragment fragment = new AFragment();
        Bundle bundle = new Bundle();
        bundle.putString("content",content);
        fragment.setArguments(bundle);

        return fragment;
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutId = R.layout.tab_bottom;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void findViews(View rootView) {
        pagerBottomTabLayout = (PagerBottomTabLayout) rootView.findViewById(R.id.tab);
        TabItemBuilder tabItemBuilder = new TabItemBuilder(activity).create()
                .setDefaultIcon(android.R.drawable.ic_menu_send)
                .setText("信息")
                .setSelectedColor(ResourceUtil.getColor(R.color.colorAccent))
                .setTag("A")
                .build();

        controller = pagerBottomTabLayout.builder()
                .addTabItem(tabItemBuilder)
                .addTabItem(android.R.drawable.ic_menu_compass, "位置",ResourceUtil.getColor(R.color.colorPrimary))
                .addTabItem(android.R.drawable.ic_menu_search, "搜索",ResourceUtil.getColor(R.color.colorAccent))
                .addTabItem(android.R.drawable.ic_menu_help, "帮助",ResourceUtil.getColor(R.color.colorPrimary))
//                .setMode(TabLayoutMode.HIDE_TEXT)
//                .setMode(TabLayoutMode.CHANGE_BACKGROUND_COLOR)
                .setMode(TabLayoutMode.HIDE_TEXT| TabLayoutMode.CHANGE_BACKGROUND_COLOR)
                .build();
        //        controller.setMessageNumber("A",2);
//        controller.setDisplayOval(0,true);
        controller.addTabItemClickListener(listener);
    }

    OnTabItemSelectListener listener = new OnTabItemSelectListener() {
        @Override
        public void onSelected(int index, Object tag)
        {
            Log.i("asd","onSelected:"+index+"   TAG: "+tag.toString());
        }

        @Override
        public void onRepeatClick(int index, Object tag) {
            Log.i("asd","onRepeatClick:"+index+"   TAG: "+tag.toString());
        }
    };


    @Override
    protected void setListener() {

    }

    @Override
    public void onSearchClose() {

    }

    @Override
    public void search(String searchString) {

    }
}
