package com.zjut.runner.view.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.zjut.runner.Controller.AsyncTaskController;
import com.zjut.runner.Controller.CurrentSession;
import com.zjut.runner.Controller.FragmentFactory;
import com.zjut.runner.Model.OrderModel;
import com.zjut.runner.Model.OrderStatus;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.MLog;
import com.zjut.runner.util.ToastUtil;
import com.zjut.runner.view.Adapter.OrderListAdapter;
import com.zjut.runner.view.Adapter.RunListAdapter;
import com.zjut.runner.widget.PullListView;
import com.zjut.runner.widget.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuylai on 2016/10/31.
 */

public class RunContentFragment extends OrderContentFragment {

    private RunListAdapter runListAdapter;

    @Override
    protected void findViews(View rootView) {
        pullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.pull_refresh_layout);
        pullListView = (PullListView) rootView.findViewById(R.id.pull_list_view);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_sending_post);
        runListAdapter = new RunListAdapter(activity,models);
        pullListView.setAdapter(runListAdapter);
        progressBar.setVisibility(View.VISIBLE);
        loadList(0,5);
    }

    protected void notifyData(){
        if (runListAdapter != null)
            runListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OrderModel orderModel = orderModels.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAM_ORDER,orderModel);
        activity.goToFragment(FragmentFactory.getFragment(Constants.FRAG_RUN_INFO,bundle,this));
    }

    @Override
    protected void parseArgument() {
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
                status = OrderStatus.GO.toString();
                break;
            case 2:
                status = OrderStatus.COMPLETED.toString();
                break;
            case 3:
                status = OrderStatus.CANCELLED.toString();
                break;
            case 4:
                status = OrderStatus.REJECTED.toString();
                break;
            default:
                break;
        }
    }


    @Override
    protected synchronized void loadList(final int skip, final int limit) {
        if(dbLoad != null){
            return;
        }
        dbLoad = new AsyncTask<Object, Void, List<OrderModel>>() {
            @Override
            protected List<OrderModel> doInBackground(Object... params) {
                return CurrentSession.getRunModels(activity,campusID,status);
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

    @Override
    protected void loadFromCloud(int skip, int limit) {
        AVQuery<AVObject> innerQuery = AVQuery.getQuery(Constants.TABLE_CAMPUS);
        innerQuery.whereEqualTo(Constants.PARAM_ID,campusID);
        AVQuery<AVObject> query = AVQuery.getQuery(Constants.PARAM_REQUEST_REPLY);
        query.whereEqualTo(Constants.PARAM_STATUS,status);
        query.whereMatchesQuery(Constants.PARAM_CAMPUS_INFO,innerQuery);
        query.include(Constants.PARAM_REQUEST_OBJ);
        query.include(Constants.PARAM_OWNER_CAMPUS);
        query.include(Constants.PARAM_OWNER_USER);
        query.setSkip(skip);
        query.setLimit(limit);
        query.orderByDescending(Constants.PARAM_CREATE);
        query.findInBackground(new FindCallback<AVObject>() {
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
            orderModels.addAll(OrderModel.setRunModels(list));
            refreshList();
        }else{
            orderModels.clear();
            orderModels.addAll(OrderModel.setRunModels(list));
            refreshList();
            saveOrderToDB();
        }
    }

    @Override
    protected synchronized void saveOrderToDB() {
        if(dbSaveOrder != null)
            return;
        dbSaveOrder = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                for(OrderModel orderModel:orderModels){
                    CurrentSession.putRunModel(activity,orderModel,campusID);
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

}
