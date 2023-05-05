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
        mySound= new Uri.Builder().scheme("android.resource").authority(MyFirebaseMessagingService.this.getResources().getResourcePackageName(R.raw.custom_msg)).appendPath(MyFirebaseMessagingService.this.getResources().getResourceTypeName(R.raw.custom_msg)).appendPath(MyFirebaseMessagingService.this.getResources().getResourceEntryName(R.raw.custom_msg)).build();

        String title=remoteMessage.getNotification().getTitle();
        String body=remoteMessage.getNotification().getBody();
        Intent intent=new Intent(this,SplashScreen.class);
        intent.putExtra("title",title);
        intent.putExtra("body",body);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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