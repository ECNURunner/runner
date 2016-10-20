package com.zjut.runner.view.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.avos.avoscloud.AVUser;
import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.R;
import com.zjut.runner.util.GeneralUtils;

/**
 * Created by Phuylai on 2016/10/4.
 */
public class LoadingActivity extends BaseActivity {

    //UI
    private View iv_loading;
    private View rl_background;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        layoutId = R.layout.activity_loading;
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(runnable, 2000);// 2 seconds to jump to another page
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
                //TODO: get campus model here
                CampusModel campusModel = CampusModel.setCampusModel(AVUser.getCurrentUser());
                
                Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };


    @Override
    protected void initActionBar() {

    }

    @Override
    protected void setListeners() {

    }
}
