package com.zjut.runner.Controller;

import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/17.
 */

public class CurrentSession {

    private static Toast toast = null;
    private static CurrentSession instance = null;

    public static Toast getToast() {
        return toast;
    }

    private static CurrentSession getInstance() {
        if (instance == null) {
            instance = new CurrentSession();
        }
        return instance;
    }
}
