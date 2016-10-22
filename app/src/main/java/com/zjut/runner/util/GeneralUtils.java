package com.zjut.runner.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.method.PasswordTransformationMethod;
import android.view.Display;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by Phuylai on 2016/10/5.
 */
public class GeneralUtils {

    private static final String TAG = "Utils";

    private static Context mContext = null;
    private static Resources mResources = null;


    public static void initConfig(Context context){
        mContext = context;
        mResources = mContext.getResources();
    }

    public static int getDimenPx(Context context, int dimenId) {
        if (context == null) {
            return 0;
        }

        return context.getResources().getDimensionPixelOffset(dimenId);
    }

    /**Set hint for edittext
     * @param editText
     */

    public static void setPassWordEditTextHintType(EditText editText) {
        if (editText == null) {
            return;
        }
        editText.setTypeface(Typeface.DEFAULT);
        editText.setTransformationMethod(new PasswordTransformationMethod());
    }

    public static int getDisplayLayoutHeightForNoActionBarScreen(
            Activity activity) {
        if (activity == null) {
            return 0;
        }
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    /*** To see if the string fit the pattern
    * @param REGEX
    * @param matchString
    */
    public static boolean matchREGEX(String REGEX, String matchString) {
        Pattern pattern = Pattern.compile(REGEX);
        return matchREGEX(pattern, matchString);
    }

    public static boolean matchREGEX(Pattern pattern, String matchString) {
        return pattern.matcher(matchString).matches();
    }

    /**
     * To recycle the bitmap when activity is destroyed
     * @author phuylai
     * @param view
     */

    public static void recycleBackground(View view){
        if(view == null){
            return;
        }
        Drawable drawable = view.getBackground();
        if(!(drawable instanceof BitmapDrawable)){
            drawable = null;
            return;
        }
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
        bitmapDrawable = null;
    }

    /**
     * Check if device is connected to internet
     * @param context
     *
     */

    public static boolean isConnect(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            MLog.e(TAG, e);
        }
        return false;
    }

}
