package com.zjut.runner.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zjut.runner.R;

/**
 * Created by Administrator on 2016/10/17.
 */

public class ResourceUtil {
    /**
     * this context for appliction
     */
    private static Context mContext = null;

    /**
     * resource refrenerce
     */
    private static Resources mRessurces = null;

    /**
     *
     * @param context
     */
    public static void initConfig(Context context) {
        mContext = context;
        mRessurces = mContext.getResources();
    }

    /**
     * Provide color by resource id
     *
     * @param colorResId
     * @return
     */
    public static int getColor(int colorResId) {
        return mRessurces.getColor(colorResId);
    }

    public static int getColor(Context context, int colorId) {
        if (context == null) {
            return 0;
        }

        return getColor(context.getResources(), colorId);
    }

    public static int getColor(Resources resources, int colorId) {
        if (resources == null) {
            return 0;
        }

        return resources.getColor(colorId);
    }

    public static String getString(int colorResId) {
        return mRessurces.getString(colorResId);
    }

    /**
     * prodide drawable by resource id
     *
     * @param drawableResId
     * @return
     */
    public static Drawable getDrawable(int drawableResId) {
        return mRessurces.getDrawable(drawableResId);
    }

    public static LayoutInflater getLayoutInflater() {
        return (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * provider arra by arr resource id
     *
     * @param arrResId
     */
    public static String[] getResourceArr(int arrResId) {
        return mRessurces.getStringArray(arrResId);
    }

    public static Context getContext(){
        return mContext;
    }

    public static Resources getmRessurces() {
        return mRessurces;
    }

    public static void setmRessurces(Resources mRessurces) {
        ResourceUtil.mRessurces = mRessurces;
    }

    public static void addGreyLine(Context context,LinearLayout ll, int height){
        View view = new View(context);
        view.setBackgroundColor(context.getResources().getColor(R.color.line_gray));
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        addView(ll,view);
    }

    public static void addView(LinearLayout bodyView,View view) {
        if (view == null) {
            return;
        }
        if (bodyView == null) {
            return;
        }
        if(view.getParent() != null){
            bodyView.removeAllViews();
        }
        bodyView.addView(view);
    }

    public static void setMarginTop(int marginTop, View view) {
        if (view == null) {
            return;
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (lp == null){
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        lp.topMargin = marginTop;
        view.setLayoutParams(lp);
    }
}
