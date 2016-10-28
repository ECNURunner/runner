package com.zjut.runner.view.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.zjut.runner.Controller.AsyncTaskController;
import com.zjut.runner.Controller.CurrentSession;
import com.zjut.runner.Model.OrderModel;
import com.zjut.runner.Model.OrderStatus;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.MLog;
import com.zjut.runner.util.RunnableManager;
import com.zjut.runner.util.ToastUtil;
import com.zjut.runner.view.Adapter.OrderListAdapter;
import com.zjut.runner.widget.PullListView;
import com.zjut.runner.widget.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuylai on 2016/10/25.
 */

public class OrderContentFragment extends BaseFragment implements PullToRefreshLayout.OnRefreshListener,
        Runnable, AdapterView.OnItemClickListener, BaseFragment.SelectedItemCallBackListener {

    private PullToRefreshLayout pullToRefreshLayout;
    private PullListView pullListView;
    protected ProgressBar progressBar;

    protected List<Object> models = new ArrayList<>();
    protected List<OrderModel> orderModels = new ArrayList<>();
    protected OrderListAdapter orderListAdapter;

    //load model
    protected AsyncTask<Object,Void,List<OrderModel>> dbLoad = null;

    //save model
    protected AsyncTask<Object,Void,Void> dbSaveOrder = null;

    // refresh
    protected Handler mUiHandler = new Handler();
    protected long DELAYMILLIS = 100;
    protected int SKIP = 0;
    protected boolean onLoad = false;

    private int index;
    private String status;
    protected String campusID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArgument();
    }

    protected void parseArgument(){
        Bundle bundle = getArguments();
        if(bundle == null)
            return;
        campusID = activity.campusModel.getCampusID();
        index = bundle.getInt(Constants.PARAM_NO);
        switch (index){
            case 0:
                status = OrderStatus.PENDING.toString();
                break;
            case 1:
                status = OrderStatus.COMPLETED.toString();
                break;
            case 2:
                status = OrderStatus.CANCELLED.toString();
                break;
            default:
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutId = R.layout.fragment_order_list;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void changeTitle() {

    }

    @Override
    protected void findViews(View rootView) {
        pullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.pull_refresh_layout);
        pullListView = (PullListView) rootView.findViewById(R.id.pull_list_view);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_sending_post);
        orderListAdapter = new OrderListAdapter(activity,models);
        pullListView.setAdapter(orderListAdapter);
        progressBar.setVisibility(View.VISIBLE);
        loadList(0,5);
    }

    protected synchronized void loadList(final int skip,final int limit){
        if(dbLoad != null){
            return;
        }
        dbLoad = new AsyncTask<Object, Void, List<OrderModel>>() {
            @Override
            protected List<OrderModel> doInBackground(Object... params) {
                return CurrentSession.getOrderModels(activity,campusID,status);
            }

            @Override
            protected void onPostExecute(List<OrderModel> orders) {
                dbLoad = null;
                if(orders != null && orders.size() > 0){
                    orderModels.clear();
                    orderModels.addAll(orders);
                    refreshList();
                }
                loadFromCloud(skip,limit);
            }
        };
        AsyncTaskController.startTask(dbLoad);
    }

    protected void loadFromCloud(int skip,int limit){
        AVQuery<AVObject> innerQuery = AVQuery.getQuery(Constants.TABLE_REQUEST);
        innerQuery.whereEqualTo(Constants.PARAM_CAMPUS_ID,campusID);
        innerQuery.whereEqualTo(Constants.PARAM_STATUS,status);
        innerQuery.setSkip(skip);
        innerQuery.setLimit(limit);
        innerQuery.orderByDescending(Constants.PARAM_CREATE);
        innerQuery.include(Constants.PARAM_HELPER_USER);
        innerQuery.include(Constants.PARAM_HELPER_CAMPUS);
        innerQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                progressBar.setVisibility(View.GONE);
                if(e == null){
                    if(list == null || list.size() == 0){
                        onLoadData();
                        return;
                    }
                    SKIP += 5;
                    notifyData(list);
                }else{
                    ToastUtil.showToastShort(activity, e.getMessage());
                    onLoadData();
                }
            }
        });
    }

    protected void notifyData(List<AVObject> list) {
        onLoadData();
        if(onLoad){
            onLoad = false;
            orderModels.addAll(OrderModel.setOrderModels(list));
            refreshList();
        }else{
            orderModels.clear();
            orderModels.addAll(OrderModel.setOrderModels(list));
            refreshList();
            saveOrderToDB();
        }

    }

    protected synchronized void saveOrderToDB(){
        if(dbSaveOrder != null)
            return;
        dbSaveOrder = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                for(OrderModel orderModel:orderModels){
                    CurrentSession.putOrderModel(activity,orderModel,campusID);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                dbSaveOrder = null;
            }
        };
        AsyncTaskController.startTask(dbSaveOrder);
    }

    protected void refreshList(){
        if(mUiHandler == null){
            return;
        }
        RunnableManager.getInstance().postDelayed(this, DELAYMILLIS);
    }

    protected void onLoadData(){
        pullToRefreshLayout.refreshFinish(true);
        pullToRefreshLayout.loadMoreFinish(true);
    }

    @Override
    protected void setListener() {
        pullToRefreshLayout.setOnRefreshListener(this);
        pullListView.setOnItemClickListener(this);
    }

    @Override
    public void onSearchClose() {

    }

    @Override
    public void search(String searchString) {

    }

    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefresh) {
        onLoad = false;
        SKIP = 0;
        loadFromCloud(0,5);
    }

    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefresh) {
        onLoad = true;
        loadFromCloud(SKIP,5);
    }

    @Override
    public void run() {
        if(models != null) {
            updateData();
            notifyData();
        }
    }

    protected void notifyData(){
        if (orderListAdapter != null)
            orderListAdapter.notifyDataSetChanged();
    }

    protected void updateData(){
        models.clear();
        models.addAll(orderModels);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MLog.d("CLICK","HERE");
        OrderModel orderModel = orderModels.get(position);
        RequestInfoFragment requestInfoFragment = new RequestInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAM_ORDER,orderModel);
        requestInfoFragment.setArguments(bundle);
        requestInfoFragment.registerSelectedCallBackListener(this,null);
        activity.goToFragment(requestInfoFragment);
    }

    @Override
    public void onItemSelected(Object selectedItem) {
        boolean refresh = (boolean) selectedItem;
        if(refresh){
            SKIP = 0;
            onLoad = false;
            pullListView.setSelectionAfterHeaderView();
            loadList(0,5);
        }
    }
}
