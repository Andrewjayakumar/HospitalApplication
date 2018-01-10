package com.android.hospitalapplication.UtilityAndNetworkingClasses;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.hospitalapplication.Activities.AppointmentReceiptActivity;
import com.android.hospitalapplication.Activities.Doctor.DoctorActivity;
import com.android.hospitalapplication.Activities.Patient.AppointmentStatusActivity;
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
        String from = remoteMessage.getData().get("from_user_id");
        String typeNotif = remoteMessage.getData().get("type");

        if(typeNotif.equals("request")){
        Intent i  = new Intent(this, DoctorActivity.class);
        i.putExtra("notif_frag","new_req");
        PendingIntent resultingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        createNotification(titleNotif,contentNotif,resultingIntent);

        }
        else if(typeNotif.equals("confirmed")){
            Intent i = new Intent(this, AppointmentReceiptActivity.class);
            String docId = from;
            String patId = remoteMessage.getData().get("to_user_id");
            i.putExtra("doc_id",docId);
            i.putExtra("pat_id",patId);
            PendingIntent resultingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            createNotification(titleNotif,contentNotif,resultingIntent);
        }
        else if(typeNotif.equals("rescheduled")){
            Intent i = new Intent(this, AppointmentReceiptActivity.class);
            String docId = from;
            String patId = remoteMessage.getData().get("to_user_id");
            i.putExtra("doc_id",docId);
            i.putExtra("pat_id",patId);
            PendingIntent resultingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            createNotification(titleNotif,contentNotif,resultingIntent);
        }
        else if(typeNotif.equals("follow")){
            Intent i = new Intent(this, AppointmentReceiptActivity.class);
            String docId = from;
            String patId = remoteMessage.getData().get("to_user_id");
            i.putExtra("doc_id",docId);
            i.putExtra("pat_id",patId);
            PendingIntent resultingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            createNotification(titleNotif,contentNotif,resultingIntent);
        }
        else if(typeNotif.equals("declined")){
            Intent i = new Intent(this, AppointmentStatusActivity.class);
            PendingIntent resultingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            createNotification(titleNotif,contentNotif,resultingIntent);
        }



    }

    private void createNotification(String titleNotif, String contentNotif, PendingIntent resultPendingIntent) {

        Notification notification = new Notification();

        notification.defaults |= Notification.DEFAULT_SOUND;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setDefaults(notification.defaults);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titleNotif)
                .setContentText(contentNotif)
                .setAutoCancel(true)
                .setVibrate(new long[]{50, 350, 200, 350, 200})
                .setLights(Color.RED, 3000, 3000)
                .setContentIntent(resultPendingIntent);

        int mNotificationId = (int) System.currentTimeMillis();

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}