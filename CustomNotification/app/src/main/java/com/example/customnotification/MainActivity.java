package com.example.customnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    String CHANNEL_ID = "";
    int NotificationID = 1;
    NotificationManager notificationManager;
    Button SimpleNotification, ExpandableNotification, ActionNotification;

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String name = "ChannelName";
            String description = "ChannelDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        SimpleNotification = findViewById(R.id.SimpleNotification);
        ExpandableNotification = findViewById(R.id.NotifyWithExpansion);
        ActionNotification = findViewById(R.id.ActionNotification);

        // Normal Notification
        // will Override previous notification hence only the latest notification will be displayed
        // Not expandible and No action bar
        SimpleNotification.setOnClickListener(view -> {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                                                            .setSmallIcon(R.drawable.icon)
                                                            .setContentTitle("Test Notification")
                                                            .setContentText("This is a Test Notification from LocusLogs")
//                                                            .setLargeIcon() //to set large icon
                                                            .setStyle(new NotificationCompat.BigTextStyle()
                                                                            .bigText("This is Much Bigger Text to Fit!!!"))
                                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            notificationManager.notify(NotificationID, notification.build());
        });

        // Notification with Expansion
        // can Contain Text, Images etc
//        ExpandableNotification.setOnClickListener(view -> {
//
//        });

        // Notification with certain Actions
        // like button to open an activity
        // showing progress
        // Text replying from notification
        ActionNotification.setOnClickListener(view -> {
            
        });
    }
}