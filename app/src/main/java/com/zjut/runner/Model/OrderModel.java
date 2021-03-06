package com.zjut.runner.Model;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phuylai on 2016/10/25.
 */

public class OrderModel implements Serializable{

    private String remark;
    private boolean isChosen;
    private String orderDate;
    private String deadline;
    private String title;
    private String dest;
    private int charge;
    private int finalCharge;
    private CampusModel helper;
    private CampusModel ownerModel;
    private String ownerUserID;
    private OrderStatus status;
    private String objectID;
    private int helpers;
    private String owner;
    private String replyRequestObjectID;

    public OrderModel(String remark, String orderDate, String deadline, String title, String dest,
                      int charge, CampusModel ownerModel, String objectID,int helpers,OrderStatus status,String getReplyID) {
        this.remark = remark;
        this.orderDate = orderDate;
        this.deadline = deadline;
        this.title = title;
        this.dest = dest;
        this.charge = charge;
        this.ownerModel = ownerModel;
        this.objectID = objectID;
        this.helpers = helpers;
        this.status = status;
        this.replyRequestObjectID = getReplyID;
    }

    public OrderModel(String remark, boolean isChosen, String orderDate, String deadline, String title,
                      String dest, int charge, int finalCharge,
                      CampusModel helper, OrderStatus status, String objectID, int helpers) {
        this.remark = remark;
        this.isChosen = isChosen;
        this.orderDate = orderDate;
        this.title = title;
        this.dest = dest;
        this.charge = charge;
        this.finalCharge = finalCharge;
        this.helper = helper;
        this.status = status;
        this.deadline = deadline;
        this.objectID = objectID;
        this.helpers = helpers;
    }

    public OrderModel(String remark, String orderDate, String deadline,
                      String title, String dest, int charge, int finalCharge, CampusModel ownerModel,
                      String replyRequestObjectID,String orderID, OrderStatus status) {
        this.remark = remark;
        this.orderDate = orderDate;
        this.deadline = deadline;
        this.title = title;
        this.dest = dest;
        this.charge = charge;
        this.finalCharge = finalCharge;
        this.ownerModel = ownerModel;
        this.replyRequestObjectID = replyRequestObjectID;
        this.status = status;
        this.objectID = orderID;
    }

    public CampusModel getOwnerModel() {
        return ownerModel;
    }

    public void setOwnerModel(CampusModel ownerModel) {
        this.ownerModel = ownerModel;
    }

    public String getOwnerUserID() {
        return ownerUserID;
    }

    public void setOwnerUserID(String ownerUserID) {
        this.ownerUserID = ownerUserID;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public String getReplyRequestObjectID() {
        return replyRequestObjectID;
    }

    public void setReplyRequestObjectID(String replyRequestObjectID) {
        this.replyRequestObjectID = replyRequestObjectID;
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
            AVObject helperCampus = avObject.getAVObject(Constants.PARAM_HELPER_CAMPUS);
            AVUser helperUser = avObject.getAVUser(Constants.PARAM_HELPER_USER);
            CampusModel user = CampusModel.setCampusModel(helperUser);
            CampusModel helper = CampusModel.refreshCampus(user,helperCampus);
            int numHelpers = (int)avObject.getNumber(Constants.PARAM_NUM_HELPER);
            String owner = avObject.getString(Constants.PARAM_CAMPUS_ID);
            OrderModel orderModel = new OrderModel(remark,chosen,orderDate,deadline,title,dest,charge,
                    finalCharge,helper,status,objectID,numHelpers);
            orderModel.setOwner(owner);
            orderModels.add(orderModel);
        }
        return orderModels;
    }

    public static List<OrderModel> OrderModels(List<AVObject> avObjects, String campusID){
        if(avObjects == null)
            return null;
        AVObject avObject,campusObject;
        List<OrderModel> orderModels = new ArrayList<>();
        List<OrderModel> returnModels = new ArrayList<>();
        List<String>  stringList = new ArrayList<>();
        String studentID = "";
        for(AVObject avObj:avObjects){
            avObject = avObj.getAVObject(Constants.PARAM_REQUEST_OBJ);
            String deadline = avObject.getString(Constants.PARAM_DEADLINE);
            if(GeneralUtils.getDateHourMinute(deadline).before(GeneralUtils.getDateWithTime())){
                continue;
            }
            campusObject = avObj.getAVObject(Constants.PARAM_CAMPUS_INFO);
            int numHelpers = (int) avObject.getNumber(Constants.PARAM_NUM_HELPER);
            if(campusObject != null) {
                studentID = campusObject.getString(Constants.PARAM_ID);
            }
            String replyRequestID = avObj.getObjectId();
            String objectID = avObject.getObjectId();
            if(!StringUtil.isNull(studentID) && studentID.equals(campusID)){
                stringList.add(objectID);
            }
            String remark = avObject.getString(Constants.PARAM_REMARK);
            String orderDate = avObject.getString(Constants.PARAM_ORDER_DATE);
            String title = avObject.getString(Constants.PARAM_TITLE);
            String dest = avObject.getString(Constants.PARAM_DEST);
            int charge = (int) avObject.getNumber(Constants.PARAM_CHARGE);
            AVObject ownerCampus = avObj.getAVObject(Constants.PARAM_OWNER_CAMPUS);
            AVUser ownerUser = avObj.getAVUser(Constants.PARAM_OWNER_USER);
            OrderStatus status = OrderStatus.getType(avObject.getString(Constants.PARAM_STATUS));
            CampusModel owner = CampusModel.setCampusModel(ownerUser);
            CampusModel ownerModel = CampusModel.refreshCampus(owner, ownerCampus);
            OrderModel orderModel = new  OrderModel(remark, orderDate, deadline, title, dest, charge,
                    ownerModel, objectID, numHelpers, status, replyRequestID);
            if(numHelpers == 0){
                returnModels.add(orderModel);
            }else {
                orderModels.add(orderModel);
            }
        }
        if(stringList.size() > 0){
            for(OrderModel model:orderModels){
                if(filter(model,stringList)){
                    returnModels.add(model);
                }
            }
        }
        return returnModels;
    }

    private static boolean filter(OrderModel model,List<String> list){
        for(String s:list){
            if(model.getObjectID().equals(s)){
                return false;
            }
        }
        return true;
    }


    public static List<OrderModel> setRunModels(List<AVObject> avObjects){
        if(avObjects == null)
            return null;
        List<OrderModel> orderModels = new ArrayList<>();
        for(AVObject avObject:avObjects){
            String objectID = avObject.getObjectId();
            AVObject request = avObject.getAVObject(Constants.PARAM_REQUEST_OBJ);
            String orderID = request.getObjectId();
            String remark = request.getString(Constants.PARAM_REMARK);
            String orderDate = request.getString(Constants.PARAM_ORDER_DATE);
            String deadline = request.getString(Constants.PARAM_DEADLINE);
            String title = request.getString(Constants.PARAM_TITLE);
            String dest = request.getString(Constants.PARAM_DEST);
            int charge = (int) request.getNumber(Constants.PARAM_CHARGE);
            int finalCharge = (int) avObject.getNumber(Constants.PARAM_CHARGE);
            OrderStatus status = OrderStatus.getType(avObject.getString(Constants.PARAM_STATUS));
            AVObject ownerCampus = avObject.getAVObject(Constants.PARAM_OWNER_CAMPUS);
            AVUser ownerUser = avObject.getAVUser(Constants.PARAM_OWNER_USER);
            CampusModel user = CampusModel.setCampusModel(ownerUser);
            CampusModel owner = CampusModel.refreshCampus(user,ownerCampus);
            orderModels.add(new OrderModel(remark,orderDate,deadline,title,dest,charge,finalCharge,
                    owner,objectID,orderID,status));
        }
        return orderModels;
    }

}
