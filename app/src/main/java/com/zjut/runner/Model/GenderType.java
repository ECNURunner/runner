package com.zjut.runner.Model;

/**
 * Created by Administrator on 2016/10/17.
 */

public enum GenderType {
    FEMALE("female"),MALE("male"),OTHER("other");

    private final String text;
    GenderType(String text){
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
    public static GenderType getType(String value){
        if(FEMALE.equal(value)){
            return FEMALE;
        }
        if(MALE.equal(value)){
            return MALE;
        }
        return OTHER;
    }
    public boolean equal(String value){
        if(text == null)
            return false;
        return text.equals(value);
    }
}
