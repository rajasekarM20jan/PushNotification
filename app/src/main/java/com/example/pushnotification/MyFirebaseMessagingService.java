package com.example.pushnotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Uri soundUri;

    private static final String TAG = "MyFirebaseMessagingService";

    @Override
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
        String packageName = getApplicationContext().getPackageName();
        int resourceId = getResources().getIdentifier("custom_tone", "raw", packageName);
        soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + File.pathSeparator + File.separator
                + File.separator + getApplicationContext().getPackageName() + File.separator  + resourceId);

        Notification ntf = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL & ~NotificationCompat.DEFAULT_SOUND)
                .setSound(soundUri)
                .setContentIntent(pendingIntent).build();


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a default notification channel for the app
            NotificationChannel channel = new NotificationChannel(channelId, "Channel Name", NotificationManager.IMPORTANCE_HIGH);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.setSound(soundUri, attributes);

            notificationManager.createNotificationChannel(channel);
        }


        notificationManager.notify(0, ntf);
    }
}

