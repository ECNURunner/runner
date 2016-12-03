package com.zjut.runner.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.RadioButton;

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

    public static LanguageType getLanguageType(Context context) {
        LanguageType languageType = MyPreference.getInstance(context).getLanguageType();
        if (languageType == null) {
            return Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage()) ?
                    LanguageType.CHINESE : LanguageType.ENGLISH;
        }
        return languageType;
    }

    public static void setChecked(Context context, RadioButton rb_english, RadioButton rb_chinese) {
        if (rb_english == null || rb_chinese == null) {
            return;
        }
        LanguageType languageType = getLanguageType(context);
        switch (languageType) {
            case ENGLISH:
                rb_english.setChecked(true);
                break;
            case CHINESE:
                rb_chinese.setChecked(true);
                break;
        }
    }

}
