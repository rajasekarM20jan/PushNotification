package com.example.pushnotification;

import static com.example.pushnotification.MainActivity.notificationManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import java.io.IOException;
import java.util.Random;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Intent intent=getIntent();
        String title=intent.getStringExtra("title");
        String body=intent.getStringExtra("body");
        showNotification(title,body);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O_MR1){
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager k=(KeyguardManager) getSystemService(this.KEYGUARD_SERVICE);
            k.requestDismissKeyguard(this,null);
        }else {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public void showNotification(String title,String body) {
        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.custom_msg);
        String NOTIFICATION_CHANNEL_ID = "pushnotificationtest";

        Intent resultIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(body));
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(SplashScreen.this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(SplashScreen.this,NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(null)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setContentIntent(resultPendingIntent)
                .setContentInfo("Info").build();
        notification.sound=null;
        MediaPlayer mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setLegacyStreamType(AudioAttributes.CONTENT_TYPE_MOVIE).build());
        mediaPlayer.reset();
        mediaPlayer=MediaPlayer.create(this,R.raw.custom_msg);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            mediaPlayer.start();
            notificationManager.notify(new Random().nextInt(), notification);
            MediaPlayer finalMediaPlayer = mediaPlayer;
            mediaPlayer.setOnCompletionListener(l->{
                finalMediaPlayer.stop();
                finalMediaPlayer.release();
            });
    }
}