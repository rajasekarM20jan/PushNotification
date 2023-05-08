package com.example.pushnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText topicET;
    Button subscribeButton;
    public static NotificationChannel notificationChannel;
    public static NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        subscribeButton=findViewById(R.id.subscribeButton);
        topicET=findViewById(R.id.topicET);



        init();
    }

    void init(){
        subscribeButton.setOnClickListener(l->{
            try{
                String topic=topicET.getText().toString();
                Toast.makeText(this, "Subscribed to "+topic, Toast.LENGTH_SHORT).show();
                topicET.setText("");
                FirebaseMessaging.getInstance().subscribeToTopic(topic);
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        Uri mySound= new Uri.Builder().scheme("android.resource").authority(MainActivity.this.getResources().getResourcePackageName(R.raw.custom_msg)).appendPath(MainActivity.this.getResources().getResourceTypeName(R.raw.custom_msg)).appendPath(MainActivity.this.getResources().getResourceEntryName(R.raw.custom_msg)).build();
        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.custom_msg);
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "pushnotificationtest";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"Notification",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("code sphere");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableLights(true);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build();
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int result = audioManager.requestAudioFocus(null, AudioManager.STREAM_NOTIFICATION, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                notificationChannel.setSound(mySound, audioAttributes);
                notificationManager.createNotificationChannel(notificationChannel);
            }

        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}