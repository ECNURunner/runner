package com.zjut.runner.Model;

/**
 * Created by Administrator on 2016/10/21.
 */

public enum RefreshType {
    PROFILE("profile"), NAME("name"), MOBILE("mobile");
    private String text;
    RefreshType(String text){
        this.text = text;
    }
    @Override
    public String toString() {
        return text;
    }
    public boolean equal(String value){
        if(text == null)
            return false;
        return text.equals(value);
    }
    public static RefreshType getType(String value){
        if(NAME.equal(value)){
            return NAME;
        }
        if(PROFILE.equal(value)) {
            return PROFILE;
        }
        if(MOBILE.equals(value)){
            return MOBILE;
        }
        return NAME;
    }
}
