package com.zjut.runner.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.view.activities.BaseActivity;
import com.zjut.runner.view.activities.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Phuylai on 2016/10/4.
 */
public abstract class BaseFragment extends Fragment{

    protected MainActivity activity;
    protected View rootView;
    protected int layoutId = -1;
    private int titleResId = -1;
    protected Context context;
    protected Resources resources;
    protected boolean drawerindicatorEnabled = false;

    protected Map<String,SelectedItemCallBackListener> selectedItemCallBacks = new HashMap<>();
    protected List<String> filterList = new ArrayList<>();

    protected String searchString;

    public interface SelectedItemCallBackListener{
        void onItemSelected(Object selectedItem);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        resources = context.getResources();
        activity = (MainActivity) getActivity();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    private BaseActivity getBaseActivity(){
        if(getActivity() != null){
            return activity;
        }
        return null;
    }

    public void setDrawerIndicatorEnabled(){
        activity.setDrawerIndicatorEnabled(drawerindicatorEnabled);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        changeTitle();
        if(layoutId != -1){
            rootView = inflater.inflate(layoutId,null);
        }
        findViews(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if(activity == null)
            return;
        if(activity instanceof MainActivity){
            this.activity = (MainActivity) activity;
            setDrawerIndicatorEnabled();
            this.activity.onFragmentResume(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListener();
    }

    public abstract void changeTitle();

    public void initMenu(Context context, Menu menu){
        clearMenu(menu);
    }

    protected void clearMenu(Menu menu){
        if(menu != null){
            menu.clear();
        }
    }

    public int getTitle(){
        return titleResId;
    }

    protected void setTitle(CharSequence title){
        if(activity == null)
            return;
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar == null){
            return;
        }
        actionBar.setTitle(Html.fromHtml(title.toString()));
    }

    protected void setTitle(String title){
        if(activity == null)
            return;
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar == null)
            return;
        actionBar.setTitle(title);
    }

    protected void setTitle(int resourceId) {
        if (activity == null) {
            return;
        }

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        actionBar.setTitle(resourceId);
    }

    protected abstract void findViews(View rootView);

    protected abstract void setListener();

    public void registerSelectedCallBackListener(SelectedItemCallBackListener callBackListener,String filterString){
        selectedItemCallBacks.put(callBackListener.toString(),callBackListener);
        if(StringUtil.notNull(filterString)){
            filterList.add(filterString);
        }
    }

    protected void itemSelected(Object selectedItem){
        for(SelectedItemCallBackListener itemCallBackListener:selectedItemCallBacks.values()){
            itemCallBackListener.onItemSelected(selectedItem);
        }
    }

    protected void callItemSelected(Object selecctedItem){
        activity.actionBarClick();
        itemSelected(selecctedItem);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity = null;
        rootView = null;
        if(selectedItemCallBacks != null){
            selectedItemCallBacks.clear();
            selectedItemCallBacks = null;
        }
        if(filterList != null){
            filterList.clear();
            filterList = null;
        }
    }

    public boolean onBackPressed(){
        if(drawerindicatorEnabled){
            activity.goToMainPageFragment();
            return true;
        }
        return false;
    }

    public abstract void onSearchClose();

    public abstract void search(String searchString);

    protected void resumeSearch(MenuItem searchMenuItem) {
        if (StringUtil.isNull(searchString)) {
            return;
        }
        if (searchMenuItem == null) {
            return;
        }
        searchMenuItem.expandActionView();
        SearchView sv = (SearchView) searchMenuItem.getActionView();
        if (sv == null) {
            return;
        }
        sv.setQuery(searchString, false);
    }

    public void onKeyBoardStateChange(int state){

    }

}
