package com.zjut.runner.Model;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.zjut.runner.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuylai on 2016/10/25.
 */

public class OrderModel{

    private String remark;
    private boolean isChosen;
    private String orderDate;
    private String deadline;
    private String title;
    private String dest;
    private int charge;
    private int finalCharge;
    private CampusModel owner;
    private CampusModel helper;
    private OrderStatus status;
    private String objectID;
    private int helpers;

    public OrderModel(String remark, boolean isChosen, String orderDate, String deadline,String title,
                      String dest, int charge, int finalCharge, CampusModel owner,
                      CampusModel helper, OrderStatus status, String objectID,int helpers) {
        this.remark = remark;
        this.isChosen = isChosen;
        this.orderDate = orderDate;
        this.title = title;
        this.dest = dest;
        this.charge = charge;
        this.finalCharge = finalCharge;
        this.owner = owner;
        this.helper = helper;
        this.status = status;
        this.deadline = deadline;
        this.objectID = objectID;
        this.helpers = helpers;
    }

    public int getHelpers() {
        return helpers;
    }

    public void setHelpers(int helpers) {
        this.helpers = helpers;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public int getFinalCharge() {
        return finalCharge;
    }

    public void setFinalCharge(int finalCharge) {
        this.finalCharge = finalCharge;
    }

    public CampusModel getOwner() {
        return owner;
    }

    public void setOwner(CampusModel owner) {
        this.owner = owner;
    }

    public CampusModel getHelper() {
        return helper;
    }

    public void setHelper(CampusModel helper) {
        this.helper = helper;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public static List<OrderModel> setOrderModels(List<AVObject> avObjects){
        if(avObjects == null)
            return null;
        List<OrderModel> orderModels = new ArrayList<>();
        for(AVObject avObject:avObjects){
            String objectID = avObject.getObjectId();
            String remark = avObject.getString(Constants.PARAM_REMARK);
            Boolean chosen = avObject.getBoolean(Constants.PARAM_CHOSEN);
            String orderDate = avObject.getString(Constants.PARAM_ORDER_DATE);
            String deadline = avObject.getString(Constants.PARAM_DEADLINE);
            String title = avObject.getString(Constants.PARAM_TITLE);
            String dest = avObject.getString(Constants.PARAM_DEST);
            int charge = (int) avObject.getNumber(Constants.PARAM_CHARGE);
            int finalCharge = (int) avObject.getNumber(Constants.PARAM_FINAL_CHARGE);
            OrderStatus status = OrderStatus.getType(avObject.getString(Constants.PARAM_STATUS));
            AVObject ownerCampus = avObject.getAVObject(Constants.PARAM_OWNER_CAMPUS);
            AVUser ownerUser = avObject.getAVUser(Constants.PARAM_OWNER_USER);
            CampusModel user = CampusModel.setCampusModel(ownerUser);
            CampusModel owner = CampusModel.refreshCampus(user,ownerCampus);
            AVObject helperCampus = avObject.getAVObject(Constants.PARAM_HELPER_CAMPUS);
            AVUser helperUser = avObject.getAVUser(Constants.PARAM_HELPER_USER);
            user = CampusModel.setCampusModel(helperUser);
            CampusModel helper = CampusModel.refreshCampus(user,helperCampus);
            int numHelpers = (int)avObject.getNumber(Constants.PARAM_NUM_HELPER);
            orderModels.add(new OrderModel(remark,chosen,orderDate,deadline,title,dest,charge,
                    finalCharge,owner,helper,status,objectID,numHelpers));
        }
        return orderModels;
    }
}
