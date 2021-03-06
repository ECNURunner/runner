package com.zjut.runner.Model;

import com.avos.avoscloud.AVFile;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/17.
 */

public class AccountModel implements Serializable{

    private String userObjectId;
    private String username;
    private String mobile;
    private String password;
    private String email;
    private GenderType genderType;
    private String url;
    private String installationID;

    public AccountModel(String userObjectId, String username, String mobile, String email,
                        GenderType genderType, String url,String installationID) {
        this.username = username;
        this.userObjectId = userObjectId;
        this.mobile = mobile;
        this.email = email;
        this.genderType = genderType;
        this.url = url;
        this.installationID = installationID;
    }

    public AccountModel(String username, String mobile, String password) {
        this.username = username;
        this.mobile = mobile;
        this.password = password;

    }

    public String getInstallationID() {
        return installationID;
    }

    public void setInstallationID(String installationID) {
        this.installationID = installationID;
    }

    public String getUserObjectId() {
        return userObjectId;
    }

    public void setUserObjectId(String userObjectId) {
        this.userObjectId = userObjectId;
    }

    public AccountModel(String mobile) {
        this.mobile = mobile;
    }

    public AccountModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GenderType getGenderType() {
        return genderType;
    }

    public void setGenderType(GenderType genderType) {
        this.genderType = genderType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
