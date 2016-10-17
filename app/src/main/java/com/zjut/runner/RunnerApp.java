package com.zjut.runner;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.zjut.runner.Model.LanguageType;
import com.zjut.runner.util.GeneralUtils;
import com.zjut.runner.util.LanguageUtil;
import com.zjut.runner.util.MyPreference;
import com.zjut.runner.util.ResourceUtil;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by Administrator on 2016/10/14.
 */

public class RunnerApp extends Application {
    public static String TAG = RunnerApp.class.getSimpleName();
    private static RunnerApp instance;

    public static synchronized RunnerApp getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SQLiteDatabase.loadLibs(this);
        instance = this;
        ResourceUtil.initConfig(getApplicationContext());
        setPreferenceChange();
        AVOSCloud.initialize(this,"CCzSqBhPvPJnE1L9YuvJqtJ0-gzGzoHsz","eAxo9lNzT3bb3PGY1o1GQVxN");
    }

    private void setPreferenceChange() {
        Context context = getApplicationContext();
        MyPreference myPreference = MyPreference.getInstance(context);
        int currentPreferenceVersion = myPreference.getVersion();
        if (currentPreferenceVersion != MyPreference.VERSION) {
            myPreference.setUsername("");
            myPreference.setPassword("");
        }
        myPreference.putVersion();
        LanguageType languageType = myPreference.getLanguageType();
        if (languageType != null) {
            LanguageUtil.changeLanguage(context, languageType);
        }
    }

}
