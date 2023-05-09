package com.example.pushnotification;

import static com.example.pushnotification.MainActivity.notificationChannel;
import static com.example.pushnotification.MainActivity.notificationManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Uri soundUri,mySound;

    private static final String TAG = "MyFirebaseMessagingService";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title=remoteMessage.getNotification().getTitle();
        String body=remoteMessage.getNotification().getBody();
        //calling the method for showing notification
        showNotification(title,body);
    }
    public void showNotification(String title,String body) {
        mySound= new Uri.Builder().scheme("android.resource").authority(this.getResources().getResourcePackageName(R.raw.custom_msg)).appendPath(this.getResources().getResourceTypeName(R.raw.custom_msg)).appendPath(this.getResources().getResourceEntryName(R.raw.custom_msg)).build();
        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.custom_msg);
        String NOTIFICATION_CHANNEL_ID = "pushnotificationtest";

        Intent resultIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(body));
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE);

        //Creating the notification using builder
        Notification notification = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(mySound)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setContentIntent(resultPendingIntent)
                .setContentInfo("Info").build();

        //setting notification sound using notification.sound
        notification.sound=mySound;

        //calling the notify method using notification manager.
        notificationManager.notify(new Random().nextInt(), notification);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("Firebasetoken ----->", s);
       /* mydb = new DatabaseHelper(MyFirebaseInstanceService.this);
        if(mydb.getfirebaseTokendetails().getCount() != 0)
        {
            mydb.deletefirebasetokendata();
        }
        boolean Isfbinserted = mydb.insertfirebasetoken(s);
        if(Isfbinserted == true)
        {
            boolean test = Isfbinserted;
            Log.i(null,"Insertion Done");
        }
        else
        {
            boolean test = Isfbinserted;
            Log.i(null,"Not Insertion Done");
        }*/
    }
}