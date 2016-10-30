package com.zjut.runner.view.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

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
import com.zjut.runner.util.ToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/10/27.
 */

public class AllOrderFragment extends OrderContentFragment{

    @Override
    protected void parseArgument() {
        campusID = activity.campusModel.getCampusID();
    }

    protected synchronized void loadList(final int skip,final int limit){
        if(dbLoad != null){
            return;
        }
        dbLoad = new AsyncTask<Object, Void, List<OrderModel>>() {
            @Override
            protected List<OrderModel> doInBackground(Object... params) {
                return CurrentSession.getAllOrderModels(activity,campusID);
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
    public void changeTitle() {
        activity.changeTitle(R.string.str_run);
    }

    @Override
    protected void loadFromCloud(int skip, int limit) {
        AVQuery<AVObject> innerQuery = AVQuery.getQuery(Constants.TABLE_REQUEST);
        innerQuery.whereNotEqualTo(Constants.PARAM_CAMPUS_ID,campusID);
        innerQuery.whereEqualTo(Constants.PARAM_STATUS, OrderStatus.PENDING.toString());
        innerQuery.whereEqualTo(Constants.PARAM_CHOSEN,false);
        innerQuery.include(Constants.PARAM_OWNER_USER);
        innerQuery.include(Constants.PARAM_OWNER_CAMPUS);
        AVQuery<AVObject> query = AVQuery.getQuery(Constants.PARAM_REQUEST_REPLY);
        query.whereMatchesQuery(Constants.PARAM_REQUEST_OBJ,innerQuery);
        query.include(Constants.PARAM_CAMPUS_INFO);
        query.include(Constants.PARAM_REQUEST_OBJ);
        query.setSkip(skip);
        query.setLimit(limit);
        innerQuery.orderByDescending(Constants.PARAM_CREATE);
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

    @Override
    protected void notifyData(List<AVObject> list) {
        onLoadData();
        if(onLoad){
            onLoad = false;
            orderModels.addAll(OrderModel.OrderModels(list,campusID));
            refreshList();
        }else{
            orderModels.clear();
            orderModels.addAll(OrderModel.OrderModels(list,campusID));
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
                    CurrentSession.putAllOrders(activity,orderModel,campusID);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OrderModel orderModel = orderModels.get(position);
        ReplyRequestFragment replyRequestFragment = new ReplyRequestFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAM_ORDER,orderModel);
        replyRequestFragment.setArguments(bundle);
        replyRequestFragment.registerSelectedCallBackListener(this,null);
        activity.goToFragment(replyRequestFragment);
    }
}
