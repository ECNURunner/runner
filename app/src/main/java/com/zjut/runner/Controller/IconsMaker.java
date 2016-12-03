package com.zjut.runner.Controller;

import com.zjut.runner.Model.GridViewIconModel;
import com.zjut.runner.Model.MenuType;
import com.zjut.runner.R;

/**
 * Created by Phuylai on 2016/12/3.
 */

public class IconsMaker {

    private GridViewIconModel helpMe;

    public IconsMaker(){
        helpMe = new GridViewIconModel(MenuType.CREATE,"Help Me","帮帮我", R.drawable.ic_request);
    }

    public GridViewIconModel getHelpMe() {
        return helpMe;
    }

    public void setHelpMe(GridViewIconModel helpMe) {
        this.helpMe = helpMe;
    }

    public GridViewIconModel myOrderIcon(){
        GridViewIconModel myOrder = null;
        try {
            myOrder = (GridViewIconModel) helpMe.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        myOrder.setType(MenuType.ORDER);
        myOrder.setEnName("My Order");
        myOrder.setChName("我的订单");
        myOrder.setImageId(R.drawable.ic_mylist);
        return myOrder;
    }

    public GridViewIconModel runList(){
        GridViewIconModel myOrder = null;
        try {
            myOrder = (GridViewIconModel) helpMe.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        myOrder.setType(MenuType.HELPS);
        myOrder.setEnName("Let's Run");
        myOrder.setChName("跑吧");
        myOrder.setImageId(R.drawable.ic_run);
        return myOrder;
    }

    public GridViewIconModel myRun(){
        GridViewIconModel myOrder = null;
        try {
            myOrder = (GridViewIconModel) helpMe.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        myOrder.setType(MenuType.RUN);
        myOrder.setEnName("Run List");
        myOrder.setChName("跑吧订单");
        myOrder.setImageId(R.drawable.ic_myrun);
        return myOrder;
    }
}
