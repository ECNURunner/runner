package com.zjut.runner.util;

import android.util.Log;

/**
 * Created by Administrator on 2016/10/17.
 */

public class MLog {
    public static final boolean DEBUG = Constants.DEBUG_ENABLE;

    public static void e(String tag, Exception e) {
        if (e == null) {
            return;
        }
        String message = e.getMessage();
        if (StringUtil.isNull(message)) {
            return;
        }
        e(tag, message);
    }

    public static void e(String tag, OutOfMemoryError e) {
        if (e == null) {
            return;
        }
        String message = e.getMessage();
        if (StringUtil.isNull(message)) {
            return;
        }
        e(tag, message);
    }

    public static void d(String tag, String message) {
        if (DEBUG)
            Log.d(tag, message);
    }

    public static void e(String tag, String message) {
        if (DEBUG)
            Log.e(tag, message);
    }

    public static void i(String tag, String message) {
        if (DEBUG)
            Log.i(tag, message);
    }

    public static void v(String tag, String message) {
        if (DEBUG)
            Log.v(tag, message);
    }

    public static void w(String tag, String message) {
        if (DEBUG)
            Log.w(tag, message);
    }

    public static void w(String tag, String message, Throwable tr) {
        if (DEBUG)
            Log.wtf(tag, message, tr);
    }
}
