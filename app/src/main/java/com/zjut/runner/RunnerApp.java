package com.zjut.runner;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.avos.avoscloud.AVOSCloud;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
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
        final int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        final int cacheSize = 1024 * 1024 * memClass / 8;
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .showImageOnLoading(
                        R.drawable.ic_usericon_default)
                .showImageForEmptyUri(
                        R.drawable.ic_usericon_default)
                .cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(cacheSize)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache()).build();
        ImageLoader.getInstance().init(config);
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
