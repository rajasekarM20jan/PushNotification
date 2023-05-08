package com.example.pushnotification;

import static android.content.Intent.getIntent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;


public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("qasdfgvbhnm,nbvfcxzaxcvbnm,mnbvfcxsza");
        String title=intent.getStringExtra("title");
        String body= intent.getStringExtra("body");

        Intent i=new Intent(context,MyNotificationDisplay.class);
        i.putExtra("title",title);
        i.putExtra("body",body);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }
}