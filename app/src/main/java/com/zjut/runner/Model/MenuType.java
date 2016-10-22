package com.zjut.runner.Model;

/**
 * Created by Phuylai on 2016/10/23.
 */

public enum MenuType {

    CREATE("create"),ORDER("order"),HELPS("helps"),RUN("run");

    private final String text;
    MenuType(String text){
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

    public static MenuType getType(String value){

        if(CREATE.equal(value)){
            return CREATE;
        }
        if(ORDER.equal(value)){
            return ORDER;
        }
        if(HELPS.equal(value)){
            return HELPS;
        }
        if(RUN.equal(value)){
            return RUN;
        }

        return HELPS;
    }
}
