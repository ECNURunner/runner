package com.zjut.runner.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zjut.runner.R;
import com.zjut.runner.widget.MaterialDialog;

/**
 * Created by Phuylai on 2016/12/3.
 */

public class DialogUtil {

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

    public static void showDialog(Context context, int title, int message){
        final MaterialDialog materialDialog = new MaterialDialog(context);
        materialDialog.setTitle(title);
        materialDialog.setMessage(message);
        materialDialog.setPositiveButton(R.string.button_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
    }

}
