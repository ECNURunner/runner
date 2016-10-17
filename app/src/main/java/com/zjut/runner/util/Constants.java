package com.zjut.runner.util;

/**
 * Created by Phuylai on 2016/10/4.
 */
public class Constants {
    //first run flag
    public static final String FIRST_RUN_TAG = "first_run_tag";

    //debug true or false
    public static final boolean DEBUG_ENABLE = true;

    public final static String POP_TO_HOME = "HOMELIST_FRAGMENT";

    // setAlpha
    public static final int DEF_OPAQUE = 255;
    public static final int ENABLE_OPAQUE = (int) (DEF_OPAQUE * 0.6);
    public static final int DISABLE_OPAQUE = (int) (DEF_OPAQUE * 0.3);

    //version,logout
    public static final String PARAM_VERSION = "version";
    public static final String LOGOUT_REASON = "LOGOUT_REASON";
    public final static String POP_TO_HOMELIST_BACKNAME = "HOMELIST_FRAGMENT";

    //user
    public final static String PARAM_USERNAME = "username";
    public final static String PARAM_PASSWORD = "password";

    //Language
    public static final String PARAM_LANGUAGE = "language";

    //password
    public static final String PASSWORD_RULES_REGEX = "^[\\Sa-zA-Z0-9_.]{6,15}$";
    public static final int PASSWORD_MINNUM = 6;
    public static final int PASSWORD_MAXNUM = 15;
    public static final int FULLNAME_LENGTH = 64;

    //Register
    public static final String PARAM_ACCOUNT = "account";

    //message what value
    public static final int ERROR_INDEX = -1;
    public static final int SECOND_TICK = -5;
    public static final int TIMER_END = -6;

}
