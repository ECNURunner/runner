package com.zjut.runner.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjut.runner.R;

/**
 * Created by Phuylai on 2016/10/4.
 */
public class MainPageFragment extends BaseFragment {

    private View mView;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId = R.layout.fragment_main;
        Log.i("come","1");
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(layoutId, null);
        }
        findViews(mView);
        return mView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerindicatorEnabled = true;
    }

    @Override
    public void changeTitle() {

    }

    @Override
    protected void findViews(View rootView) {
        textView = (TextView) rootView.findViewById(R.id.tv_test);
        textView.setText("Change");
    }

    @Override
    protected void setListener() {

    }

    @Override
    public boolean onBackPressed() {
        activity.finish();
        return true;
    }

    @Override
    public void onSearchClose() {

    }

    @Override
    public void search(String searchString) {

    }
}
