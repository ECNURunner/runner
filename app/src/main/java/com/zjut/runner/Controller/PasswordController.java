package com.zjut.runner.Controller;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjut.runner.R;
import com.zjut.runner.util.GeneralUtils;

/**
 * Created by Administrator on 2016/10/17.
 */

public class PasswordController {
    /*
  hide the warning textview
  * */
    public static void hiddenWarning(TextView textView) {
        if (textView == null) {
            return;
        }
        textView.setVisibility(View.INVISIBLE);
    }

    /*
    * set text for the warning textview
    * */
    public static void setWarningText(TextView textView, int textId) {
        if (textId < 0) {
            hiddenWarning(textView);
            return;
        }
        ViewController.setVisible(true, textView);
        if (textView == null) {
            return;
        }

        textView.setText(textId);
    }

    /**
     * set top maring for warning text in relative layout
     */
    public static void setWarningTopMarginByRelativeLayout(Context context,
                                                           TextView textView) {
        if (textView == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        lp.topMargin = GeneralUtils.getDimenPx(context,
                R.dimen.super_large_margin);
        textView.setLayoutParams(lp);
    }

    /**
     * set top maring for warning text in linear layout
     */
    public static void setWarningTopMarginByLinearLayout(Context context,
                                                         TextView textView) {
        if (textView == null) {
            return;
        }
        android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) textView
                .getLayoutParams();
        lp.topMargin = GeneralUtils.getDimenPx(context,
                R.dimen.super_large_margin);
        textView.setLayoutParams(lp);
    }
}
