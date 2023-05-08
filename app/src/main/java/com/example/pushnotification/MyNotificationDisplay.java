package com.example.pushnotification;

import static com.example.pushnotification.MainActivity.notificationManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.Random;

public class MyNotificationDisplay extends AppCompatActivity {
    Uri soundUri,mySound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification_display);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O_MR1){
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

        Intent i=getIntent();
        String title=i.getStringExtra("title");
        String body=i.getStringExtra("body");
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
        notification.sound=mySound;
        notificationManager.notify(new Random().nextInt(), notification);
    }
}