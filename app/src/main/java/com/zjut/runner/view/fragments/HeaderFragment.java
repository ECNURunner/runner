package com.zjut.runner.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;
import com.zjut.runner.Controller.FragmentFactory;
import com.zjut.runner.Model.BannerModel;
import com.zjut.runner.R;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.util.ToastUtil;
import com.zjut.runner.view.activities.MainActivity;

/**
 * Created by Phuylai on 2016/10/23.
 */

public class HeaderFragment extends Fragment implements View.OnClickListener {

    public static final String PARAM_RESID = "resid";
    private ImageView iv_backgroud;
    private BannerModel resId;
    private MainActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        resId = (BannerModel) bundle.getSerializable(PARAM_RESID);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_header, null);
        findViews(rootView);
        return rootView;
    }

    protected void findViews(View rootView) {
        iv_backgroud = (ImageView) rootView.findViewById(R.id.header_backgroud);
        iv_backgroud.setOnClickListener(this);
        updateBannerView();
    }

    private void updateBannerView() {
        Ion.with(iv_backgroud)
                .placeholder(R.drawable.side_nav_bar)
                .error(R.drawable.side_nav_bar)
                .load(resId.getImageUrl());
    }

    @Override
    public void onClick(View v) {
        if(GeneralUtils.isConnect(activity)){
            String url = resId.getGoToUrl();
            if (StringUtil.isNull(url)) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PARAM_URL, url);
            bundle.putString(Constants.PARAM_TITLE, resId.getTitle());
            activity.goToFragment(FragmentFactory.getFragment(Constants.FRAG_WEB,bundle,null));
        }
        else{
            ToastUtil.showToastShort(activity, R.string.connection_not_avalible);
        }
    }
}

