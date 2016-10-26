package com.zjut.runner.Model;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.StringUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CampusModel extends AccountModel{

    private String objectId;
    private String cardPass;
    private String campusID;
    private String campusName;
    private float balance;

    public CampusModel(String objectId,String userObjectId, String username, String mobile, String email, GenderType genderType,
                       String url, String cardPass, String campusID, String campusName, float balance) {
        super(userObjectId,username, mobile, email, genderType, url);
        this.cardPass = cardPass;
        this.campusID = campusID;
        this.campusName = campusName;
        this.balance = balance;
        this.objectId = objectId;
    }

    public CampusModel(String userObjectId, String username, String mobile, String email, GenderType genderType, String urlProfile) {
        super(userObjectId,username, mobile, email, genderType,urlProfile);
    }

    public CampusModel(String mobile) {
        super(mobile);
    }

    public CampusModel() {
    }

    public CampusModel(String objectId, String userObjectId, String username, String mobile,
                       String email, GenderType genderType, String url, String campusID, String campusName) {
        super(userObjectId,username, mobile, email, genderType, url);
        this.campusID = campusID;
        this.campusName = campusName;
        this.objectId = objectId;
    }

    public String getCardPass() {
        return cardPass;
    }

    public void setCardPass(String cardPass) {
        this.cardPass = cardPass;
    }

    public String getCampusID() {
        return campusID;
    }

    public void setCampusID(String campusID) {
        this.campusID = campusID;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public static CampusModel setCampusModel(AVUser avUser){
        if(avUser == null)
            return null;
        CampusModel campusModel = new CampusModel();
        String userObjectId = avUser.getObjectId();
        campusModel.setUserObjectId(userObjectId);
        String email = avUser.getEmail();
        if(email != null){
            campusModel.setEmail(email);
        }
        String phone = avUser.getMobilePhoneNumber();
        if(phone != null){
            campusModel.setMobile(phone);
        }
        String userName = avUser.getUsername();
        if(userName != null){
            campusModel.setUsername(userName);
        }
        AVFile urlProfile = avUser.getAVFile(Constants.PARAM_PIC_URL);
        if(urlProfile != null){
            campusModel.setUrl(urlProfile.getThumbnailUrl(false,100,100));
        }
        String genderType = avUser.getString(Constants.PARAM_GENDER);
        if(genderType != null){
            campusModel.setGenderType(GenderType.getType(genderType));
        }
        String campusID = avUser.getString(Constants.PARAM_ID);
        if(!StringUtil.isNull(campusID)){
            campusModel.setCampusID(campusID);
        }
        return campusModel;
    }

    public void setBalance(float cardBalance) {
        this.balance = cardBalance;
    }

    public float getBalance(){
        return balance;
    }

    public static CampusModel refreshCampus(CampusModel oldModel,AVObject avObject){
        if(oldModel == null || avObject == null){
            return null;
        }
        CampusModel campusModel = new CampusModel();
        campusModel.setObjectId(avObject.getObjectId());
        campusModel.setCardPass(avObject.getString(Constants.PARAM_CARD_PASS));
        campusModel.setCampusName(avObject.getString(Constants.PARAM_CARD_NAME));
        campusModel.setBalance((float)avObject.getDouble(Constants.PARAM_CARD_BALANCE));
        campusModel.setEmail(oldModel.getEmail());
        campusModel.setMobile(oldModel.getMobile());
        campusModel.setUsername(oldModel.getUsername());
        campusModel.setUrl(oldModel.getUrl());
        campusModel.setGenderType(oldModel.getGenderType());
        campusModel.setCampusID(oldModel.getCampusID());
        campusModel.setUserObjectId(oldModel.getUserObjectId());
        return campusModel;
    }

}
