package com.android.hospitalapplication.UtilityAndNetworkingClasses;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.hospitalapplication.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Gaurav on 31-12-2017.
 */

public class FirebaseNotificationsService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("payload :", "" + remoteMessage.getData());

        String titleNotif = remoteMessage.getData().get("title_notif");
        String contentNotif = remoteMessage.getData().get("body_notif");
        createNotification(titleNotif,contentNotif);


    }

    private void createNotification(String titleNotif, String contentNotif){//, PendingIntent resultPendingIntent) {

        Notification notification = new Notification();

        notification.defaults |= Notification.DEFAULT_SOUND;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setDefaults(notification.defaults);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titleNotif)
                .setContentText(contentNotif)
                .setAutoCancel(true)
                .setVibrate(new long[]{50, 350, 200, 350, 200})
                .setLights(Color.RED, 3000, 3000);
                //.setContentIntent(resultPendingIntent);

        int mNotificationId = (int) System.currentTimeMillis();

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}