package com.zjut.runner.view.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.zjut.runner.Controller.AsyncTaskController;
import com.zjut.runner.Controller.CurrentSession;
import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.util.ToastUtil;

import java.util.List;

/**
 * Created by Phuylai on 2016/10/4.
 */
public class LoadingActivity extends BaseActivity {

    //UI
    private View iv_loading;
    private View rl_background;

    //campus model
    private CampusModel campusModel;

    //load campus model
    private AsyncTask<Object,Void,CampusModel> dbLoad = null;
    //save campus model
    private AsyncTask<Object,Void,Void> dbSave = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        layoutId = R.layout.activity_loading;
        super.onCreate(savedInstanceState);
        //new Handler().postDelayed(runnable, 2000);// 2 seconds to jump to another page
    }

    @Override
    protected void findViews() {
        super.findViews();
        iv_loading = findViewById(R.id.iv_loading);
        rl_background = findViewById(R.id.rl_background);
        try{
            Resources resources = getResources();
            Bitmap loadingBackground = BitmapFactory.decodeResource(resources,R.drawable.loading_background);
            rl_background.setBackground(new BitmapDrawable(loadingBackground));
            Bitmap logo = BitmapFactory.decodeResource(resources, R.drawable.logo);
            iv_loading.setBackground(new BitmapDrawable(logo));
            loadingBackground = null;
            logo = null;
        }catch (OutOfMemoryError e){

        }
        switchUI();

    }

    private void switchUI(){
        if(AVUser.getCurrentUser() != null){
            campusModel = CampusModel.setCampusModel(AVUser.getCurrentUser());
            if(!StringUtil.isNull(campusModel.getCampusID())){
                loadCampusInfo();
            }else{
                goToMainActivity();
            }
        }else {
            Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void goToMainActivity(){
        Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAM_CAMPUS,campusModel);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GeneralUtils.recycleBackground(rl_background);
        GeneralUtils.recycleBackground(iv_loading);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(AVUser.getCurrentUser() != null){
                Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    private synchronized void loadCampusInfo(){
        if(dbLoad != null){
            return;
        }
        dbLoad = new AsyncTask<Object, Void, CampusModel>() {
            @Override
            protected CampusModel doInBackground(Object... params) {
                return CurrentSession.getCurrentCampusInfo(getApplicationContext(),campusModel);
            }

            @Override
            protected void onPostExecute(CampusModel campus) {
                dbLoad = null;
                if(campus != null){
                    campusModel = campus;
                }
                loadFromCloud(campusModel.getCampusID());
            }
        };
        AsyncTaskController.startTask(dbLoad);
    }

    private void loadFromCloud(final String campusID){
        AVQuery<AVObject> query = new AVQuery<AVObject>(Constants.TABLE_CAMPUS);
        query.setCachePolicy(AVQuery.CachePolicy.IGNORE_CACHE);
        query.whereEqualTo(Constants.PARAM_ID, campusID);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null && list.size() > 0) {
                    campusModel = CampusModel.refreshCampus(campusModel,list);
                    saveCampusModelToDB();
                } else {
                    ToastUtil.showToastShort(getApplicationContext(), R.string.connection_not_avalible);
                    goToMainActivity();
                }
            }
        });
    }

    private synchronized void saveCampusModelToDB() {
        if (dbSave != null) {
            return;
        }
        dbSave = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                CurrentSession.putCampusModelwithCache(getApplicationContext(),campusModel);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                dbSave = null;
                goToMainActivity();
            }
        };
        AsyncTaskController.startTask(dbSave);
    }


    @Override
    protected void initActionBar() {

    }

    @Override
    protected void setListeners() {

    }
}
