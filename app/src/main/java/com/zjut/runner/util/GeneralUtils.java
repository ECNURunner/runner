package com.zjut.runner.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by Phuylai on 2016/10/5.
 */
public class GeneralUtils {

    public static int getDimenPx(Context context, int dimenId) {
        if (context == null) {
            return 0;
        }

        return context.getResources().getDimensionPixelOffset(dimenId);
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
}
