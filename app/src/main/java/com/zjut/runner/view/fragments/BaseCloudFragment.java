package com.zjut.runner.view.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjut.runner.Controller.AsyncTaskController;
import com.zjut.runner.Model.OrderModel;
import com.zjut.runner.util.RunnableManager;

import java.util.List;

/**
 * Created by Phuylai on 2016/12/4.
 */

public abstract class BaseCloudFragment extends BaseFragment implements Runnable {

    //load model
    protected AsyncTask<Object,Void,List<OrderModel>> dbLoad = null;

    //save model
    protected AsyncTask<Object,Void,Void> dbSaveOrder = null;

    // refresh
    protected Handler mUiHandler = new Handler();
    protected long DELAYMILLIS = 100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArgument();
    }

    public abstract void parseArgument();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(layoutId != -1){
            rootView = inflater.inflate(layoutId,null);
        }
        findViews(rootView);
        loadList(0,5);
        return rootView;
    }

    @Override
    public void changeTitle() {

    }

    @Override
    protected void findViews(View rootView) {

    }

    @Override
    protected void setListener() {

    }

    public synchronized void loadList(final int start, final int limit) {
        if(dbLoad != null){
            return;
        }
        dbLoad = new AsyncTask<Object, Void, List<OrderModel>>() {
            @Override
            protected List<OrderModel> doInBackground(Object... params) {
                return loadFromDataBase();
            }

            @Override
            protected void onPostExecute(List<OrderModel> orders) {
                dbLoad = null;
                if(orders != null && orders.size() > 0){
                    addModels(orders);
                    refreshList();
                }
                loadFromCloud(start,limit);
            }
        };
        AsyncTaskController.startTask(dbLoad);
    }

    protected abstract void addModels(List<OrderModel> orders);

    protected abstract void loadFromCloud(int start, int limit);

    protected abstract List<OrderModel> loadFromDataBase();

    protected synchronized void saveOrderToDB(){
        if(dbSaveOrder != null)
            return;
        dbSaveOrder = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                saveModelToDB();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                dbSaveOrder = null;
            }
        };
        AsyncTaskController.startTask(dbSaveOrder);
    }

    protected abstract void saveModelToDB();

    protected void refreshList(){
        if(mUiHandler == null){
            return;
        }
        RunnableManager.getInstance().postDelayed(this, DELAYMILLIS);
    }


    @Override
    public void run() {
        runThread();
    }

    protected abstract void runThread();

}
