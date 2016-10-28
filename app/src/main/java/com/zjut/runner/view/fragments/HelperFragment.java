package com.zjut.runner.view.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjut.runner.Model.HelperModel;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.RunnableManager;
import com.zjut.runner.view.Adapter.HelperAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/27.
 */

public class HelperFragment extends BaseFragment implements Runnable, HelperAdapter.HelperClickListener {

    private List<HelperModel> helperModels = new ArrayList<>();

    private RecyclerView recyclerView;
    private HelperAdapter helperAdapter;

    // refresh
    protected Handler mUiHandler = new Handler();
    protected long DELAYMILLIS = 200;
    private List<HelperModel> models = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArgument();
    }

    private void parseArgument(){
        Bundle bundle = getArguments();
        if(bundle == null)
            return;
        helperModels = (List<HelperModel>) bundle.getSerializable(Constants.PARAM_HELPER_USER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId = R.layout.fragment_recycler;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void changeTitle() {
        activity.changeTitle(R.string.str_helpers);
    }

    @Override
    protected void findViews(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        helperAdapter = new HelperAdapter(activity,models,this);
        recyclerView.setAdapter(helperAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        refreshList();
    }

    @Override
    public void run() {
        if(models != null) {
            updateData();
            notifyData();
        }
    }

    protected void updateData(){
        models.clear();
        models.addAll(helperModels);
    }

    protected void notifyData(){
        if (helperAdapter != null)
            helperAdapter.notifyDataSetChanged();
    }

    protected void refreshList(){
        if(mUiHandler == null){
            return;
        }
        RunnableManager.getInstance().postDelayed(this, DELAYMILLIS);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onSearchClose() {

    }

    @Override
    public void search(String searchString) {

    }

    @Override
    public void onCardItemClick(HelperModel helperModel) {
        callItemSelected(helperModel);
    }
}
