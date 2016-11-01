package com.zjut.runner.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zjut.runner.Controller.ViewController;
import com.zjut.runner.Model.CampusModel;
import com.zjut.runner.R;
import com.zjut.runner.util.ResourceUtil;
import com.zjut.runner.util.StringUtil;

/**
 * Created by Administrator on 2016/10/21.
 */

public class UserHeaderHolder extends BaseViewHolder {

    private CampusModel campusModel;

    private View background;
    private View ll_gender;
    private View rl_icon;
    private CircleImageView circleImageView;
    private TextView tv_name;
    private TextView tv_major;
    private ImageView iv_gender;
    private TextView tv_gender;

    private ProfileClick profileClick;
    private Context context;

    private DisplayImageOptions options;

    public UserHeaderHolder(Context context,CampusModel campusModel,ProfileClick profileClick){
        super(context);
        this.context = context;
        this.campusModel = campusModel;
        this.profileClick = profileClick;
        setBackground();
        setView();
        setProfile(campusModel);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_usericon_default)
                .showImageForEmptyUri(R.drawable.ic_usericon_default)
                .showImageOnFail(R.drawable.ic_usericon_default)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    private void setBackground(){
        background.setBackgroundColor(ResourceUtil.getColor(R.color.colorPrimary));
    }

    public void setView(){
        ViewController.setVisible(false, rl_icon);
        setGender();
        tv_name.setText(campusModel.getCampusName());
    }

    private void setGender(){
        tv_name.setText(campusModel.getUsername());
        if(campusModel.getGenderType() != null){
            switch (campusModel.getGenderType()){
                case FEMALE:
                    iv_gender.setVisibility(View.VISIBLE);
                    iv_gender.setBackgroundResource(R.drawable.gender_female);
                    tv_gender.setText(R.string.str_female);
                    break;
                case MALE:
                    iv_gender.setVisibility(View.VISIBLE);
                    iv_gender.setBackgroundResource(R.drawable.gender_male);
                    tv_gender.setText(R.string.str_male);
                    break;
                default:
                    ll_gender.setVisibility(View.GONE);
                    break;
            }
        }else{
            ll_gender.setVisibility(View.GONE);
        }
    }

    public void setProfile(CampusModel campusModel){
        if(!StringUtil.isNull(campusModel.getUrl())){
            ImageLoader.getInstance().displayImage(campusModel.getUrl(),circleImageView,options);
        }
        else{
            circleImageView.setImageResource(R.drawable.ic_usericon_default);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.header_item;
    }

    @Override
    protected void findViews(View rootView) {
        background = rootView.findViewById(R.id.ll_header);
        rl_icon = rootView.findViewById(R.id.rl_icon);
        circleImageView = (CircleImageView) rootView.findViewById(R.id.iv_user);
        tv_gender = (TextView) rootView.findViewById(R.id.tv_gender);
        iv_gender = (ImageView) rootView.findViewById(R.id.iv_gender);
        ll_gender = rootView.findViewById(R.id.ll_gender);
        tv_name = (TextView) rootView.findViewById(R.id.tv_user);
        circleImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_user:
                performChangeProfile();
                break;
        }
    }

    public interface ProfileClick{
        void changeProfile();
    }

    public void setProfileClick(ProfileClick profileClick) {
        this.profileClick = profileClick;
    }

    private void performChangeProfile(){
        if(profileClick != null){
            profileClick.changeProfile();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public void onDestroy() {
        background = null;
        ll_gender = null;
        tv_major = null;
        tv_name = null;
        tv_gender = null;
        circleImageView = null;
        rl_icon = null;
        iv_gender = null;
    }
}
