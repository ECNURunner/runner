package com.zjut.runner.Model;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.zjut.runner.util.Constants;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CampusModel extends AccountModel{
    private String cardPass;
    private String campusID;
    private String campusName;
    private float balance;

    public CampusModel(String username, String mobile, String email, GenderType genderType,
                       String url, String cardPass, String campusID, String campusName, float balance) {
        super(username, mobile, email, genderType, url);
        this.cardPass = cardPass;
        this.campusID = campusID;
        this.campusName = campusName;
        this.balance = balance;
    }

    public CampusModel(String username, String mobile, String email, GenderType genderType, String urlProfile) {
        super(username, mobile, email, genderType,urlProfile);
    }

    public CampusModel(String mobile) {
        super(mobile);
    }

    public CampusModel() {
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


    public static CampusModel setCampusModel(AVUser avUser){
        CampusModel campusModel = new CampusModel();
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
        if(campusID != null){
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

    public static CampusModel refreshCampus(CampusModel oldModel,List<AVObject> avObjects){
        if(oldModel == null || avObjects == null){
            return null;
        }
        AVObject avObject = avObjects.get(0);
        CampusModel campusModel = new CampusModel();
        campusModel.setCardPass(avObject.getString(Constants.PARAM_CARD_PASS));
        campusModel.setCampusName(avObject.getString(Constants.PARAM_CARD_NAME));
        campusModel.setBalance((float)avObject.getDouble(Constants.PARAM_CARD_BALANCE));
        campusModel.setEmail(oldModel.getEmail());
        campusModel.setMobile(oldModel.getMobile());
        campusModel.setUsername(oldModel.getUsername());
        campusModel.setUrl(oldModel.getUrl());
        campusModel.setGenderType(oldModel.getGenderType());
        campusModel.setCampusID(oldModel.getCampusID());
        return campusModel;
    }
}
