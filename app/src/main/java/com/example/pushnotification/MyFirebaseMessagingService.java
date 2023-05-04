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
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Uri soundUri;

    private static final String TAG = "MyFirebaseMessagingService";
    MainActivity m;

    /*@Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);



        if (remoteMessage.getNotification() != null) {
            // Get the notification payload data
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            sendNotification(title, message);
        }

    }
    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "12345678";
        */
    /*soundUri = Uri.parse("android.resource://"
                +getApplicationContext().getPackageName() + "/raw/custom_msg");*/
    /*

        soundUri = Uri.parse("android.resource://"
                +getApplicationContext().getPackageName() + "/"+R.raw.custom_msg);

        Notification ntf = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                */
    /*.setSound(soundUri)*/
    /*
                .setContentIntent(pendingIntent).build();

        ntf.sound=soundUri;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a default notification channel for the app

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            NotificationChannel channel = new NotificationChannel(channelId, "Channel Name", NotificationManager.IMPORTANCE_HIGH);

            channel.setSound(soundUri, attributes);

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, ntf);
    }*/

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData().isEmpty())
        {
            showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

        }
        else
        {
            showNotification(remoteMessage.getData());

        }

    }

    public void showNotification(Map<String,String> data)
    {

        String title = data.get("title");
        String body = data.get("body");
        // Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notification);
        // Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/raw/notification");
        Uri mySound= new Uri.Builder().scheme("android.resource").authority(MyFirebaseMessagingService.this.getResources().getResourcePackageName(R.raw.custom_msg)).appendPath(MyFirebaseMessagingService.this.getResources().getResourceTypeName(R.raw.custom_msg)).appendPath(MyFirebaseMessagingService.this.getResources().getResourceEntryName(R.raw.custom_msg)).build();
        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.custom_msg);

        //String NOTIFICATION_CHANNEL_ID = "aki.claimregistration.services.test";
        String NOTIFICATION_CHANNEL_ID = "pushnotificationtest";
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"Notification",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("code sphere");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            notificationChannel.setSound(mySound, audioAttributes);
            notificationManager.createNotificationChannel(notificationChannel);
        }*/


        //        Intent intent = new Intent(getApplicationContext(), TermsConditions.class);
        //        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //        Intent intent = new Intent(getApplicationContext(), MyFirebaseMessagingService.class);
        //        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //        intent.putExtra("pushnotification", "yes");
        //        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Intent resultIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(body));
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyFirebaseMessagingService.this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this,NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(mySound,AudioManager.STREAM_NOTIFICATION)
                .setContentIntent(resultPendingIntent)
                .setContentInfo("Info");
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(null, AudioManager.STREAM_NOTIFICATION, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
        }

    }

    public void showNotification(String title,String body)
    {
        // Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notification);
        //   Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/raw/notification");
        Uri mySound= new Uri.Builder().scheme("android.resource").authority(MyFirebaseMessagingService.this.getResources().getResourcePackageName(R.raw.custom_msg)).appendPath(MyFirebaseMessagingService.this.getResources().getResourceTypeName(R.raw.custom_msg)).appendPath(MyFirebaseMessagingService.this.getResources().getResourceEntryName(R.raw.custom_msg)).build();
        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.custom_msg);
        String NOTIFICATION_CHANNEL_ID = "pushnotificationtest";

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"Notification",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("code sphere");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableLights(true);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            notificationChannel.setSound(mySound, audioAttributes);
            notificationManager.createNotificationChannel(notificationChannel);
        }*/
        //        Intent intent = new Intent(this, TermsConditions.class);
        //        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //        Intent intent = new Intent(getApplicationContext(), MyFirebaseMessagingService.class);
        //        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //        intent.putExtra("pushnotification", "yes");
        //        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Intent resultIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(body));
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyFirebaseMessagingService.this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this,NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(mySound,AudioManager.STREAM_NOTIFICATION)
                .setContentIntent(resultPendingIntent)
                .setContentInfo("Info");
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(null, AudioManager.STREAM_NOTIFICATION, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
        }

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