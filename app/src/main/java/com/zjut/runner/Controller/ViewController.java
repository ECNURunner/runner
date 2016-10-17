package com.zjut.runner.Controller;

import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/10/17.
 */

public class ViewController {
    public static void setVisible(boolean visible, View view) {
        if (visible) {
            setVisibilityVisible(view);
        } else {
            setVisibilityGone(view);
        }
    }

    private static void setVisibilityVisible(View view) {
        setVisibility(view, View.VISIBLE);
    }

    private static void setVisibilityGone(View view) {
        setVisibility(view, View.GONE);
    }

    public static void setVisibility(View view, int visibility) {
        if (view == null) {
            return;
        }

        if (view.getVisibility() == visibility) {
            return;
        }

        view.setVisibility(visibility);
    }

    public static void setEditTextMaxLength(EditText editText, int maxLength) {
        InputFilter[] filters = new InputFilter[] { new InputFilter.LengthFilter(
                maxLength) };
        editText.setFilters(filters);
    }
}
