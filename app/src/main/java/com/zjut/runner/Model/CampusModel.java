package com.zjut.runner.Model;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.zjut.runner.util.Constants;

import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CampusModel extends AccountModel {
    private String cardPass;
    private String campusID;
    private String campusName;
    private String campusClass;
    private String majorEn;
    private String majorCn;
    private String dormNo;
    private String roomNo;
    private float cardBalance;

    public CampusModel(String username, String mobile, String email, GenderType genderType,
                       String url, String cardPass, String campusID, String campusName,
                       String campusClass, String majorEn, String majorCn, String dormNo,
                       String roomNo, float cardBalance) {
        super(username, mobile, email, genderType, url);
        this.cardPass = cardPass;
        this.campusID = campusID;
        this.campusName = campusName;
        this.campusClass = campusClass;
        this.majorEn = majorEn;
        this.majorCn = majorCn;
        this.dormNo = dormNo;
        this.roomNo = roomNo;
        this.cardBalance = cardBalance;
    }

    public CampusModel(String username, String mobile, String email, GenderType genderType, AVFile urlProfile) {
        super(username, mobile, email, genderType, urlProfile);
    }

    public CampusModel(String mobile) {
        super(mobile);
    }

    public CampusModel() {
    }

    public CampusModel(AccountModel accountModel){
        super(accountModel);
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

    public String getCampusClass() {
        return campusClass;
    }

    public void setCampusClass(String campusClass) {
        this.campusClass = campusClass;
    }

    public String getMajorEn() {
        return majorEn;
    }

    public void setMajorEn(String majorEn) {
        this.majorEn = majorEn;
    }

    public String getMajorCn() {
        return majorCn;
    }

    public void setMajorCn(String majorCn) {
        this.majorCn = majorCn;
    }

    public String getDormNo() {
        return dormNo;
    }

    public void setDormNo(String dormNo) {
        this.dormNo = dormNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public float getCardBalance() {
        return cardBalance;
    }

    public void setCardBalance(int cardBalance) {
        this.cardBalance = cardBalance;
    }

    public void setCardBalance(float cardBalance) {
        this.cardBalance = cardBalance;
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
            campusModel.setUrlProfile(urlProfile);
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

    public static CampusModel refreshCampus(CampusModel oldModel,List<AVObject> avObjects){
        if(oldModel == null || avObjects == null){
            return null;
        }
        AVObject avObject = avObjects.get(0);
        CampusModel campusModel = new CampusModel();
        campusModel.setCampusClass(avObject.getString(Constants.PARAM_CLASS));
        campusModel.setCardPass(avObject.getString(Constants.PARAM_CARD_PASS));
        campusModel.setCampusName(avObject.getString(Constants.PARAM_CARD_NAME));
        campusModel.setMajorCn(avObject.getString(Constants.PARAM_MAJOR_CN));
        campusModel.setMajorEn(avObject.getString(Constants.PARAM_MAJOR_EN));
        campusModel.setDormNo(avObject.getString(Constants.PARAM_DORM));
        campusModel.setRoomNo(avObject.getString(Constants.PARAM_NO));
        campusModel.setCardBalance((float)avObject.getDouble(Constants.PARAM_CARD_BALANCE));
        campusModel.setEmail(oldModel.getEmail());
        campusModel.setMobile(oldModel.getMobile());
        campusModel.setUsername(oldModel.getUsername());
        campusModel.setUrlProfile(oldModel.getUrlProfile());
        campusModel.setUrl(oldModel.getUrl());
        campusModel.setGenderType(oldModel.getGenderType());
        campusModel.setCampusID(oldModel.getCampusID());
        return campusModel;
    }
}
