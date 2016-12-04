package com.zjut.runner.view.fragments;

import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.zjut.runner.Controller.FragmentFactory;
import com.zjut.runner.Controller.IconsMaker;
import com.zjut.runner.Model.BannerModel;
import com.zjut.runner.Model.GridViewIconModel;
import com.zjut.runner.Model.MenuType;
import com.zjut.runner.Model.OrderModel;
import com.zjut.runner.Model.OrderStatus;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.DialogUtil;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.ResourceUtil;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.util.ToastUtil;
import com.zjut.runner.view.Adapter.GridViewAdapter;
import com.zjut.runner.view.Adapter.HeaderAdapter;
import com.zjut.runner.view.Adapter.OrderListAdapter;
import com.zjut.runner.widget.AutoScrollViewPager;
import com.zjut.runner.widget.CircleView;
import com.zjut.runner.widget.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuylai on 2016/10/4.
 */
public class MainPageFragment extends BaseFragment implements AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener {

    public static final String TAG = MainPageFragment.class.getSimpleName();

    //menu
    private GridView gv_icon;
    private List<GridViewIconModel> Icons = new ArrayList<>();
    private GridViewAdapter adapter;

    //Header
    private AutoScrollViewPager viewPager;
    private LinearLayout tapPoint;
    private List<BannerModel> bannerModels = new ArrayList<>();
    private HeaderAdapter headerAdapter;
    private View mView;

    private String campusID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId = R.layout.fragment_main;
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(layoutId, null);
        }
        findViews(mView);
        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gv_icon = null;
        if(Icons != null){
            Icons.clear();
            Icons = null;
        }
        viewPager = null;
        tapPoint = null;
        if(bannerModels != null){
            bannerModels.clear();
            bannerModels = null;
        }
        headerAdapter = null;
        adapter = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerindicatorEnabled = true;
        campusID = activity.campusModel.getCampusID();
        menuIcons();
        requestBanner();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewPager.stopAutoScroll();
    }

    @Override
    public void changeTitle() {
        setTitle(R.string.app_name);
    }

    @Override
    protected void findViews(View rootView) {
        activity.expandToolbar(false);
        //set menu
        gv_icon = (GridView) rootView.findViewById(R.id.icon_gv);
        adapter = new GridViewAdapter(activity,Icons);
        gv_icon.setAdapter(adapter);
        gv_icon.setOnItemClickListener(this);

        //set header
        viewPager = (AutoScrollViewPager) rootView.findViewById(R.id.customer_pager);
        headerAdapter = new HeaderAdapter(getChildFragmentManager(),bannerModels);
        viewPager.setAdapter(headerAdapter);
        tapPoint = (LinearLayout) rootView.findViewById(R.id.tap_point);
    }


    private void requestBanner() {
        final List<BannerModel> models = new ArrayList<>();
        AVQuery<AVObject> query = new AVQuery<AVObject>("Banner");
        query.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (AVObject avObject : list) {
                        AVFile file = avObject.getAVFile(Constants.PARAM_URL);
                        String url = file.getUrl();
                        String goTo = avObject.getString(Constants.PARAM_GOTO);
                        String title = avObject.getString(Constants.PARAM_TITLE);
                        models.add(new BannerModel(url,goTo,title));
                    }
                    bannerModels.addAll(models);
                    if(headerAdapter != null){
                        headerAdapter.notifyDataSetChanged();
                    }
                    updateHeader();
                } else {
                    ToastUtil.showToastShort(activity, R.string.connection_not_avalible);
                }
            }
        });
    }

    private void updateHeader() {
        viewPager.setInterval(3000);
        viewPager.startAutoScroll();
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % bannerModels.size());
        viewPager.setOnPageChangeListener(this);
        addPoints();
    }

    private void addPoints() {
        if(tapPoint != null){
            tapPoint.removeAllViewsInLayout();
        }
        int widthAndHeight = GeneralUtils.getDimenPx(
                getActivity().getApplicationContext(), R.dimen.margin_five) * 2;
        int padding = GeneralUtils.getDimenPx(getActivity().getApplicationContext(),
                R.dimen.margin_eight);
        int circleColor = ResourceUtil.getColor(getActivity().getApplicationContext(),
                R.color.line_gray);
        for (int i = 0; i < bannerModels.size(); i++) {
            CircleView circleView = new CircleView(getActivity().getApplicationContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(widthAndHeight, widthAndHeight);
            lp.rightMargin = padding;
            circleView.setLayoutParams(lp);
            circleView.setCircleColor(circleColor);
            tapPoint.addView(circleView);
        }
        onPageSelected(bannerModels.size() - 1);
    }

    private void menuIcons() {
        IconsMaker iconsMaker = new IconsMaker();
        Icons.add(iconsMaker.getHelpMe());
        Icons.add(iconsMaker.myOrderIcon());
        Icons.add(iconsMaker.runList());
        Icons.add(iconsMaker.myRun());
    }

    @Override
    protected void setListener() {
    }

    @Override
    public boolean onBackPressed() {
        activity.finish();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GridViewIconModel gridViewIconModel = (GridViewIconModel) adapter.getItem(position);
        MenuType menuType = gridViewIconModel.getType();
        authorizedFunction(menuType);
    }

    private void authorizedFunction(MenuType menuType){
        if(activity.campusModel == null || StringUtil.isNull(activity.campusModel.getCampusID())){
            DialogUtil.showDialog(activity,R.string.reminder,R.string.str_message_invalid);
            return;
        }
        activity.goToFragment(FragmentFactory.getFragment(menuType.toString()));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < tapPoint.getChildCount(); i++) {
            int color = 0;
            if (i != position) {
                color = ResourceUtil.getColor(activity.getApplicationContext(),
                        R.color.line_gray);
            } else {
                color = ResourceUtil.getColor(activity.getApplicationContext(),
                        R.color.colorAccent);
            }
            ((CircleView) tapPoint.getChildAt(i)).setCircleColor(color);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
