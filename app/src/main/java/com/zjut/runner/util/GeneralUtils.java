package com.zjut.runner.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.text.method.PasswordTransformationMethod;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zjut.runner.Model.LanguageType;
import com.zjut.runner.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.R.attr.phoneNumber;

/**
 * Created by Phuylai on 2016/10/5.
 */
public class GeneralUtils {

    private static final String TAG = "Utils";

    private static Context mContext = null;
    private static Resources mResources = null;


    public static void initConfig(Context context) {
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

    public static void recycleBackground(View view) {
        if (view == null) {
            return;
        }
        Drawable drawable = view.getBackground();
        if (!(drawable instanceof BitmapDrawable)) {
            drawable = null;
            return;
        }
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        if (bitmap != null && !bitmap.isRecycled()) {
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

    /**
     * return display option
     * @param
     *
     */
    public static DisplayImageOptions getOptions() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_usericon_default)
                .showImageForEmptyUri(R.drawable.ic_usericon_default)
                .showImageOnFail(R.drawable.ic_usericon_default)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
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

    public static LanguageType getLanguageType(Context context) {
        LanguageType languageType = MyPreference.getInstance(context).getLanguageType();
        if (languageType == null) {
            return Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage()) ?
                    LanguageType.CHINESE : LanguageType.ENGLISH;
        }
        return languageType;
    }

    public static void setMarginTop(int marginTop, View view) {
        if (view == null) {
            return;
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (lp == null) {
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        lp.topMargin = marginTop;
        view.setLayoutParams(lp);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem != null) {
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static Date getDateHourMinute(String value){
        if(value == null)
            return null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
        try {
            return simpleDateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDateWithTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
        Date date = new Date();
        try {
            return simpleDateFormat.parse(simpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AVPush getPush(String installationID, String alert) {
        AVPush avPush = new AVPush();
        AVQuery pushQuery = AVInstallation.getQuery();
        pushQuery.whereEqualTo("installationId", installationID);
        avPush.setQuery(pushQuery);
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("action", "com.zjut.runner");
        jsonObject.put(Constants.PARAM_ALERT, alert);
        avPush.setData(jsonObject);
        return avPush;
    }

    public static void showCallDialog(final Context context) {
        View layout = ((Activity) context).getLayoutInflater().inflate(
                R.layout.frame_call_dialog, null);
        Button btn_call = (Button) layout.findViewById(R.id.bt_call);
        btn_call.getBackground().setAlpha(Constants.DEF_OPAQUE);
        TextView tv_line = (TextView) layout.findViewById(R.id.tv_hotline);
        final String phoneNumber = "13127771810";
        tv_line.setText(context.getString((R.string.str_hotline), "13127771810"));
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setView(layout);
        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                context.startActivity(intent);
                alertDialog.dismiss();
            }
        });
    }

}
