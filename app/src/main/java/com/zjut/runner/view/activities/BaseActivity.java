package com.zjut.runner.view.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.zjut.runner.util.ActivitiesManager;
import com.zjut.runner.view.fragments.BaseFragment;

/**
 * Created by Phuylai on 2016/10/4.
 */
public class BaseActivity extends AppCompatActivity {
    protected int layoutId = -1;
    protected BaseFragment currentFragment = null;
    protected Menu menu;
    protected void findViews(){
        initActionBar();
    }
    protected void initActionBar(){
        ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }
    protected void setListeners(){}

    private void initFragments(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isLoadingScreen()){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        initFragments();
        if(layoutId > 0){
            setContentView(layoutId);
            findViews();
            setListeners();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private boolean isLoadingScreen(){
        return this.getClass() == LoadingActivity.class;
    }

    public void setCurrentFragment(BaseFragment currentFragment){
        this.currentFragment = currentFragment;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivitiesManager.getInstance().addActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ActivitiesManager.getInstance().removeActivity(this);
    }

    protected void setTitle(String title){
        getActionBar().setTitle(title);
    }

}

