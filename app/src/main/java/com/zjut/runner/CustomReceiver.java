package com.zjut.runner;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.avos.avoscloud.AVOSCloud;
import com.zjut.runner.util.Constants;
import com.zjut.runner.util.MLog;
import com.zjut.runner.util.StringUtil;
import com.zjut.runner.view.activities.LoadingActivity;

import org.json.JSONObject;

/**
 * Created by Phuylai on 2016/11/2.
 */

public class CustomReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals("com.zjut.runner")) {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
                final String alert = json.getString(Constants.PARAM_ALERT);
                String message = "";
                if(alert.equals("1")){
                    message = context.getString(R.string.push_message_1);
                }else if(alert.equals("2")){
                    message = context.getString(R.string.push_messge_2);
                }else{
                    message = context.getString(R.string.push_message_3);
                }
                Intent resultIntent = new Intent(AVOSCloud.applicationContext, LoadingActivity.class);
                resultIntent.putExtra(Constants.PARAM_STATUS,Integer.parseInt(alert));
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(AVOSCloud.applicationContext, 0, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(AVOSCloud.applicationContext)
                                .setSmallIcon(R.mipmap.icon_xw_ptr_arrow)
                                .setContentTitle(
                                        AVOSCloud.applicationContext.getResources().getString(R.string.app_name))
                                .setContentText(message)
                                .setTicker(message);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setAutoCancel(true);

                int mNotificationId = 10086;
                NotificationManager mNotifyMgr =
                        (NotificationManager) AVOSCloud.applicationContext
                                .getSystemService(
                                        Context.NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }
        } catch (Exception e) {
            MLog.i("receiver",e.getMessage());
        }
    }
}
