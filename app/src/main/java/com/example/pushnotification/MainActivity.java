package com.example.pushnotification;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    EditText topicET;
    Button subscribeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        subscribeButton=findViewById(R.id.subscribeButton);
        topicET=findViewById(R.id.topicET);

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

    }
}