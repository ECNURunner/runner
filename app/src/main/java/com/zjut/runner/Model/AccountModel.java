package com.zjut.runner.Model;

import com.avos.avoscloud.AVFile;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/17.
 */

public class AccountModel implements Serializable{
    private String username;
    private String mobile;
    private String password;
    private String email;
    private GenderType genderType;
    private AVFile urlProfile;
    private String url;

    public AccountModel(String username, String mobile, String email, GenderType genderType, String url) {
        this.username = username;
        this.mobile = mobile;
        this.email = email;
        this.genderType = genderType;
        this.url = url;
    }

    public AccountModel(String username, String mobile, String password) {
        this.username = username;
        this.mobile = mobile;
        this.password = password;

    }

    public AccountModel(String username, String mobile, String email, GenderType genderType, AVFile urlProfile) {
        this.username = username;
        this.mobile = mobile;
        this.email = email;
        this.genderType = genderType;
        this.urlProfile = urlProfile;
    }

    public AccountModel(String mobile) {
        this.mobile = mobile;
    }

    public AccountModel() {
    }

    public AccountModel(AccountModel accountModel) {
        this.username = accountModel.getUsername();
        this.mobile = accountModel.getMobile();
        this.email = accountModel.getEmail();
        this.genderType = accountModel.getGenderType();
        this.urlProfile = accountModel.getUrlProfile();
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

    public AVFile getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(AVFile urlProfile) {
        this.urlProfile = urlProfile;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
