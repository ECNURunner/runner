package com.zjut.runner.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Administrator on 2016/10/20.
 */

public abstract class BaseViewHolder implements View.OnClickListener {

    protected LayoutInflater layoutInflater;
    protected View rootView;
    protected Context context;

    public BaseViewHolder(Context context){
        this.context = context;
        createRootViews(context);
        findViews(rootView);
        setListener();
    }

    protected abstract  int getLayoutId();

    protected abstract  void findViews(View rootView);

    public abstract void setEnabled(boolean enabled);

    public abstract void onDestroy();

    protected void createRootViews(Context context){
        layoutInflater = LayoutInflater.from(context);
        int layoutId = getLayoutId();
        rootView = layoutInflater.inflate(layoutId, null);
    }

    protected void setListener(){
        if(rootView == null)
            return;
        rootView.setOnClickListener(this);
    }

    public View getRootView(){
        return rootView;
    }

    @Override
    public void onClick(View v) {

    }
}
