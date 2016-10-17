package com.zjut.runner.Model;

/**
 * Created by Administrator on 2016/10/14.
 */

public enum LanguageType {
    CHINESE("chinese"), ENGLISH("english");
    private final String text;
    LanguageType(String text){
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
    public boolean equals(String value){
        if(text == null){
            return false;
        }
        return  text.equals(value);
    }
}
