package com.zjut.runner.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/10/17.
 */

public class CheckDataUtil {

    private static final Pattern PASSWORD_PATTERN = Pattern
            .compile("^[\\Sa-zA-Z0-9_.]{6,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");

    public static boolean checkPassWord(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isTrueEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches() != false;
    }

}
