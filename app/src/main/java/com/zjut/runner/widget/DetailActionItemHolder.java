package com.zjut.runner.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjut.runner.Model.ActionType;
import com.zjut.runner.R;
import com.zjut.runner.view.activities.MainActivity;
import com.zjut.runner.view.fragments.RequestInfoFragment;

/**
 * Created by Administrator on 2016/10/20.
 */

public class DetailActionItemHolder extends BaseViewHolder {
    private int iconImage;
    private int actionName;
    private String actionDesc;
    private ActionType actionType;
    private boolean clickAble;
    private ItemClickListener itemClickListener;
    private SettingItemClickListener settingItemClickListener;
    private HelpersClickListener helpersClickListener;

    private TextView tv_name;
    private TextView tv_desc;
    private ImageView iv_icon;
    private ImageView iv_navigate;

    public  DetailActionItemHolder(Context context, int iconImage, int actionName,
                                   String actionDesc, ActionType actionType, Boolean clickAble,
                                   ItemClickListener itemClickListener){
        super(context);
        this.iconImage = iconImage;
        this.actionDesc = actionDesc;
        this.actionName = actionName;
        this.actionType = actionType;
        this.itemClickListener = itemClickListener;
        this.clickAble = clickAble;
        setView();
    }

    public  DetailActionItemHolder(Context context,int iconImage,int actionName,
                                   String actionDesc, ActionType actionType, Boolean clickAble,
                                   SettingItemClickListener settingItemClickListener){
        super(context);
        this.iconImage = iconImage;
        this.actionDesc = actionDesc;
        this.actionName = actionName;
        this.actionType = actionType;
        this.settingItemClickListener = settingItemClickListener;
        this.clickAble = clickAble;
        setView();
    }

    public  DetailActionItemHolder(Context context,int iconImage,int actionName,
                                   String actionDesc, ActionType actionType, Boolean clickAble,
                                   HelpersClickListener helpersClickListener){
        super(context);
        this.iconImage = iconImage;
        this.actionDesc = actionDesc;
        this.actionName = actionName;
        this.actionType = actionType;
        this.helpersClickListener = helpersClickListener;
        this.clickAble = clickAble;
        setView();
    }

    private void setView(){
        if(iconImage > 0){
            iv_icon.setImageResource(iconImage);
        }
        if(actionDesc != null){
            tv_desc.setText(actionDesc);
        }else{
            tv_desc.setText(R.string.str_not_set);
        }
        if(actionType != null) {
            switch (actionType) {
                case BINDING:
                case UNBIND:
                case PASSWORD:
                case LANG:
                    tv_name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    tv_desc.setText(null);
                    break;
            }
        }
        tv_name.setText(actionName);
        if(clickAble){
            iv_navigate.setVisibility(View.VISIBLE);
        }
    }

    public interface ItemClickListener{
        void nameClick();
        void genderClick();
        void phoneClick();
        void emailClick();
        void bindClick();
        void unbindClick();
    }

    public interface SettingItemClickListener{
        void langClick();
        void passClick();
    }

    public interface HelpersClickListener{
        void helpersClick();
    }

    public void setSettingItemClickListener(SettingItemClickListener settingItemClickListener) {
        this.settingItemClickListener = settingItemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setHelpersClickListener(HelpersClickListener helpersClickListener){
        this.helpersClickListener = helpersClickListener;
    }

    private void performHelpersClick(){
        if(helpersClickListener != null){
            helpersClickListener.helpersClick();
        }
    }

    private void performLangClick(){
        if(settingItemClickListener != null){
            settingItemClickListener.langClick();
        }
    }

    private void performPassClick(){
        if(settingItemClickListener != null){
            settingItemClickListener.passClick();
        }
    }

    private void performNameClick(){
        if(itemClickListener != null){
            itemClickListener.nameClick();
        }
    }
    private void performGenderClick(){
        if(itemClickListener != null){
            itemClickListener.genderClick();
        }
    }
    private void performPhoneClick(){
        if(itemClickListener != null){
            itemClickListener.phoneClick();
        }
    }
    private void performEmailClick(){
        if(itemClickListener != null){
            itemClickListener.emailClick();
        }
    }
    private void performBindClick(){
        if(itemClickListener != null){
            itemClickListener.bindClick();
        }
    }
    private void performUnbindClick(){
        if(itemClickListener != null){
            itemClickListener.unbindClick();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.detail_action_item;
    }

    @Override
    protected void findViews(View rootView) {
        iv_icon = (ImageView) rootView.findViewById(R.id.iv_icon);
        tv_name = (TextView) rootView.findViewById(R.id.tv_action_name);
        tv_desc = (TextView) rootView.findViewById(R.id.tv_desc);
        iv_navigate = (ImageView) rootView.findViewById(R.id.iv_action);
    }

    @Override
    public void onClick(View v) {
        if(actionType == null){
            return;
        }
        switch (actionType){
            case NAME:
                performNameClick();
                break;
            case GENDER:
                performGenderClick();
                break;
            case PHONE:
                performPhoneClick();
                break;
            case EMAIL:
                performEmailClick();
                break;
            case BINDING:
                performBindClick();
                break;
            case UNBIND:
                performUnbindClick();
                break;
            case LANG:
                performLangClick();
                break;
            case PASSWORD:
                performPassClick();
                break;
            case HELPER:
                performHelpersClick();
                break;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public void onDestroy() {
        tv_desc = null;
        tv_name = null;
        iv_navigate = null;
        iv_icon = null;
    }
}
