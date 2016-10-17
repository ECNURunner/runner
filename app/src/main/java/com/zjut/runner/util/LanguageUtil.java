package com.zjut.runner.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.zjut.runner.Model.LanguageType;

import java.util.Locale;

/**
 * Created by Administrator on 2016/10/14.
 */

public class LanguageUtil {
    /**
     * change language
     * @author phuylai
     * @param context
     * @param languageType
     */
    public static void changeLanguage(Context context, LanguageType languageType) {
        if (context == null) {
            return;
        }
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        switch (languageType) {
            case CHINESE:
                config.locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case ENGLISH:
                config.locale = Locale.ENGLISH;
                break;
            default:
                break;
        }
        resources.updateConfiguration(config, dm);
        MyPreference.getInstance(context).changeLanguage(languageType);
    }

    /**
     * @author phuylai
     * get lanuguage
     */
    public static LanguageType getLang(Context context) {
        LanguageType languageType = MyPreference.getInstance(context).getLanguageType();
        if(languageType == null){
            return LanguageType.ENGLISH;
        }
        return languageType;
    }
}
