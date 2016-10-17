package com.zjut.runner.util;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.zjut.runner.Controller.CurrentSession;

/**
 * Created by Administrator on 2016/10/17.
 */

public class ToastUtil {
    /**
     * show toast by resId
     *
     * @param stResID
     */
    public static void showToast(int stResID) {
        Toast.makeText(ResourceUtil.getContext(), stResID,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * show toast by str
     *
     * @param str
     */
    public static void showToast(String str) {
        Toast.makeText(ResourceUtil.getContext(), str, Toast.LENGTH_SHORT)
                .show();
    }


    public static void showToastShort(Context context, String text) {
        showToastShort(context, CurrentSession.getToast(), text);
    }

    public static void showToastShort(Context context, Toast toast, String text) {
        if (toast != null) {
            toast.setText(text);
            toast.notify();
        } else {
            if (context == null) {
                return;
            }

            if (Looper.myLooper() == null) {
                Looper.prepare();
            }

            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void showToastShort(Context context, int resId) {
        showToastShort(context, CurrentSession.getToast(), resId);
    }

    public static void showToastShort(Context context, Toast toast, int resId) {
        if (toast != null) {
            toast.setText(resId);
            toast.notify();
        } else {
            if (context == null) {
                return;
            }

            if (Looper.myLooper() == null) {
                Looper.prepare();
            }

            toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void showToastLong(Context context, String text) {
        showToastLong(context, CurrentSession.getToast(), text);
    }

    public static void showToastLong(Context context, Toast toast, String text) {
        if (toast != null) {
            toast.setText(text);
            toast.notify();
        } else {
            if (context == null) {
                return;
            }

            if (Looper.myLooper() == null) {
                Looper.prepare();
            }

            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }
        toast.show();
    }

    public static void showToastLong(Context context, int resId) {
        showToastLong(context, CurrentSession.getToast(), resId);
    }

    public static void showToastLong(Context context, Toast toast, int resId) {
        if (toast != null) {
            toast.setText(resId);
            toast.notify();
        } else {
            if (context == null) {
                return;
            }

            if (Looper.myLooper() == null) {
                Looper.prepare();
            }

            toast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
        }
        toast.show();
    }
}
