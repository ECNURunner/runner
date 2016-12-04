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
import com.zjut.runner.Controller.FragmentFactory;
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
    public void parseArgument() {
        campusID = activity.campusModel.getCampusID();
    }

    @Override
    protected List<OrderModel> loadFromDataBase() {
        return CurrentSession.getAllOrderModels(activity,campusID);
    }

    @Override
    public void changeTitle() {
        setTitle(R.string.str_run);
    }

    @Override
    protected void loadFromCloud(int skip, int limit) {
        AVQuery<AVObject> innerQuery = AVQuery.getQuery(Constants.TABLE_REQUEST);
        innerQuery.whereNotEqualTo(Constants.PARAM_CAMPUS_ID,campusID);
        innerQuery.whereEqualTo(Constants.PARAM_STATUS, OrderStatus.PENDING.toString());
        innerQuery.whereEqualTo(Constants.PARAM_CHOSEN,false);
        innerQuery.whereLessThan(Constants.PARAM_NUM_HELPER,10);
        //TODO: limit deadline
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

    @Override
    protected void saveModelToDB() {
        for(OrderModel orderModel:orderModels){
            CurrentSession.putAllOrders(activity,orderModel,campusID);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OrderModel orderModel = orderModels.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAM_ORDER,orderModel);
        activity.goToFragment(FragmentFactory.getFragment(Constants.FRAG_REPLY_REQ,bundle,this));
    }
}
