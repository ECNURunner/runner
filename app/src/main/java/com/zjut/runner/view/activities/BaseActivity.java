package com.zjut.runner.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.avos.avoscloud.AVUser;
import com.zjut.runner.Controller.CurrentSession;
import com.zjut.runner.util.ActivitiesManager;
import com.zjut.runner.util.MLog;
import com.zjut.runner.util.MyPreference;
import com.zjut.runner.util.ToastUtil;
import com.zjut.runner.view.fragments.BaseFragment;

/**
 * Created by Phuylai on 2016/10/4.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected int layoutId = -1;
    protected BaseFragment currentFragment = null;
    protected Menu menu;
    protected ActionBar actionBar;

    protected void findViews(){
        initActionBar();
    }

    protected abstract void initActionBar();

    protected abstract void setListeners();

    private void initFragments(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isLoadingScreen() /*|| isLoginScreen()*/){
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

    private boolean isLoginScreen(){
        return this.getClass() == LoginActivity.class;
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

    /**
     * show toast by resId
     *
     * @param resID
     */
    protected void showToast(int resID) {
        ToastUtil.showToast(resID);
    }

    protected void setTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    public void logOut() {
        if (this.getClass() != LoginActivity.class) {
            AVUser.logOut();
            //TODO:clean cache

            // CLEAN PASSWORD FROM SHARED PREFERENCES
            MyPreference preference = MyPreference.getInstance(this);
            preference.setPassword("");

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();

            MLog.e("CMD_LOGOUT","SUCCESS");
        }
    }
}

