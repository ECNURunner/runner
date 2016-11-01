package com.zjut.runner.view.fragments;

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
import com.zjut.runner.Model.BannerModel;
import com.zjut.runner.Model.GridViewIconModel;
import com.zjut.runner.Model.MenuType;
import com.zjut.runner.Model.OrderModel;
import com.zjut.runner.Model.OrderStatus;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
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

    //list
    private ListView recyclerView;
    protected OrderListAdapter orderListAdapter;
    protected List<Object> models = new ArrayList<>();
    private List<OrderModel> temp = new ArrayList<>();
    private String campusID;
    private LinearLayout ll_info;

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
        loadFromCloud(0,5);
    }

    @Override
    public void onPause() {
        super.onPause();
        viewPager.stopAutoScroll();
    }

    @Override
    public void changeTitle() {
        activity.changeTitle(R.string.app_name);
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

        //list
        recyclerView = (ListView) rootView.findViewById(R.id.rv_list);
        orderListAdapter = new OrderListAdapter(activity,models);
        recyclerView.setAdapter(orderListAdapter);
        ll_info = (LinearLayout) rootView.findViewById(R.id.ll_indicator);
        GeneralUtils.setListViewHeightBasedOnChildren(recyclerView);
        loadFromCloud(0,5);
    }

    protected void loadFromCloud(int skip, int limit) {
        AVQuery<AVObject> innerQuery = AVQuery.getQuery(Constants.TABLE_REQUEST);
        innerQuery.whereNotEqualTo(Constants.PARAM_CAMPUS_ID,campusID);
        innerQuery.whereEqualTo(Constants.PARAM_STATUS, OrderStatus.PENDING.toString());
        innerQuery.whereEqualTo(Constants.PARAM_CHOSEN,false);
        AVQuery<AVObject> query = AVQuery.getQuery(Constants.PARAM_REQUEST_REPLY);
        query.whereMatchesQuery(Constants.PARAM_REQUEST_OBJ,innerQuery);
        query.include(Constants.PARAM_CAMPUS_INFO);
        query.include(Constants.PARAM_REQUEST_OBJ);
        query.include(Constants.PARAM_OWNER_USER);
        query.include(Constants.PARAM_OWNER_CAMPUS);
        query.setSkip(skip);
        query.setLimit(limit);
        innerQuery.orderByDescending(Constants.PARAM_CREATE);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e == null){
                    if(list == null || list.size() == 0){
                        ll_info.setVisibility(View.GONE);
                        return;
                    }
                    notifyData(list);
                }else{
                    ToastUtil.showToastShort(activity, e.getMessage());
                }
            }
        });
    }

    protected void notifyData(List<AVObject> list) {
        models.clear();
        temp = OrderModel.OrderModels(list,campusID);
        if(temp.size() > 0){
            ll_info.setVisibility(View.VISIBLE);
        }else{
            ll_info.setVisibility(View.GONE);
        }
        models.addAll(temp);
        if (orderListAdapter != null) {
            orderListAdapter.notifyDataSetChanged();
            GeneralUtils.setListViewHeightBasedOnChildren(recyclerView);
        }
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
        Icons.add(0,new GridViewIconModel(MenuType.CREATE,"Help Me","帮帮我",R.mipmap.ic_launcher));
        Icons.add(1,new GridViewIconModel(MenuType.ORDER,"My Order","我的订单",R.mipmap.ic_launcher));
        Icons.add(2,new GridViewIconModel(MenuType.HELPS,"Let's Run","跑吧",R.mipmap.ic_launcher));
        Icons.add(3,new GridViewIconModel(MenuType.RUN,"Run List","跑吧订单",R.mipmap.ic_launcher));
    }

    @Override
    protected void setListener() {
        recyclerView.setOnItemClickListener(this);
    }

    @Override
    public boolean onBackPressed() {
        activity.finish();
        return true;
    }

    @Override
    public void onSearchClose() {

    }

    @Override
    public void search(String searchString) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.rv_list){
            OrderModel orderModel = (OrderModel) models.get(position);
            ReplyRequestFragment replyRequestFragment = new ReplyRequestFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.PARAM_ORDER,orderModel);
            replyRequestFragment.setArguments(bundle);
            activity.goToFragment(replyRequestFragment);
        }else {
            GridViewIconModel gridViewIconModel = (GridViewIconModel) adapter.getItem(position);
            MenuType menuType = gridViewIconModel.getType();
            switch (menuType) {
                case HELPS:
                case ORDER:
                case RUN:
                case CREATE:
                    authorizedFunction(menuType);
                    break;
            }
        }
    }

    private void authorizedFunction(MenuType menuType){
        if(activity.campusModel == null || StringUtil.isNull(activity.campusModel.getCampusID())){
            showDialog();
            return;
        }
        switch (menuType){
            case RUN:
                activity.goToFragment(new MyRunListFragment());
                break;
            case ORDER:
                activity.goToFragment(new MyOrderFragment());
                break;
            case HELPS:
                activity.goToFragment(new AllOrderFragment());
                break;
            case CREATE:
                activity.goToFragment(new NewRequestFragment());
                break;
        }
    }

    private void showDialog(){
        final MaterialDialog materialDialog = new MaterialDialog(activity);
        materialDialog.setTitle(R.string.reminder);
        materialDialog.setMessage(R.string.str_message_invalid);
        materialDialog.setPositiveButton(R.string.button_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
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
