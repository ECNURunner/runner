package com.zjut.runner.Model;

/**
 * Created by Administrator on 2016/10/20.
 */

public enum ActionType {

    NAME("name"),GENDER("gender"),PHONE("phone"),EMAIL("email"),
    BINDING("binding"),UNBIND("unbind"),LANG("lang"),PASSWORD("password"), HELPER("helper");
    private final String text;
    ActionType(String text){
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

    public static ActionType getType(String value){
        if(NAME.equal(value)){
            return NAME;
        }
        if(GENDER.equal(value)){
            return GENDER;
        }
        if(BINDING.equal(value)){
            return BINDING;
        }
        if(UNBIND.equal(value)){
            return UNBIND;
        }
        if(EMAIL.equal(value)){
            return EMAIL;
        }
        if(PHONE.equal(value)){
            return PHONE;
        }
        if(PASSWORD.equal(value)){
            return PASSWORD;
        }
        if(LANG.equal(value)){
            return LANG;
        }
        if(HELPER.equal(value)){
            return HELPER;
        }
        return NAME;
    }
}
