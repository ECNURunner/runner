package com.zjut.runner.Model;

/**
 * Created by Phuylai on 2016/10/25.
 */

public enum OrderStatus {
    PENDING("pending"),COMPLETED("completed"),CANCELLED("cancelled");
    private String text;
    OrderStatus(String text){
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

    public static OrderStatus getType(String value){
        if(PENDING.equal(value)){
            return PENDING;
        }
        if(COMPLETED.equal(value)){
            return COMPLETED;
        }
        return CANCELLED;
    }

}
