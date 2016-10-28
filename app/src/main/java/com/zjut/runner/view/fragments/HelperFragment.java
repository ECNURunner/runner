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
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.Model.HelperModel;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.RunnableManager;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.view.Adapter.HelperAdapter;
import com.zjut.runner.widget.CircleImageView;
import com.zjut.runner.widget.MaterialDialog;

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
        showConfirmDialog(helperModel);
    }

    private void showConfirmDialog(final HelperModel helperModel){
        final View view = activity.getLayoutInflater().inflate(R.layout.helper_info, null);
        final CircleImageView iv_icon = (CircleImageView) view.findViewById(R.id.iv_helper);
        final TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        final TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        final TextView tv_email = (TextView) view.findViewById(R.id.tv_email);
        final TextView tv_gender = (TextView) view.findViewById(R.id.tv_gender);
        final TextView tv_charge = (TextView) view.findViewById(R.id.tv_charge);
        final MaterialDialog materialDialog = new MaterialDialog(activity)
                .setView(view);
        if(!StringUtil.isNull(helperModel.getUrl())){
            ImageLoader.getInstance().displayImage(helperModel.getUrl(),iv_icon, GeneralUtils.getOptions());
        }
        tv_name.setText(getString(R.string.helper_name,helperModel.getCampusName()));
        tv_phone.setText(getString(R.string.helper_phone,helperModel.getMobile()));
        tv_email.setText(getString(R.string.helper_email,helperModel.getEmail()));
        if(helperModel.getGenderType() != null) {
            tv_gender.setText(getString(R.string.helper_gender, helperModel.getGenderType().toString()));
        }
        tv_charge.setText(getString(R.string.helper_charge,StringUtil.convertIntegerToString(helperModel.getHelperCharge())));
        materialDialog.setPositiveButton(getString(R.string.button_ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                callItemSelected(helperModel);
            }
        });
        materialDialog.setNegativeButton(getString(R.string.button_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
    }
}
