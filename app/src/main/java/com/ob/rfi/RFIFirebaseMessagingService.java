package com.ob.rfi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class RFIFirebaseMessagingService extends FirebaseMessagingService {
    private String TAG="RFIFirebaseMessagingService";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        System.out.println(TAG + " token --> " + token);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            if (remoteMessage.getNotification() != null) {
                showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            } else {
                showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
            }
        } catch (Exception e) {
            System.out.println(TAG + " error -->" + e.getLocalizedMessage());
        }
    }

// ... other imports and class definition ...

    private void showNotification(String title, String body) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       /* PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );*/
        String channelId = getString(R.string.channel_id);
        String channelName = getString(R.string.channel_name);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupNotificationChannels(channelId, channelName, notificationManager);
        }
        android.net.Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        Intent notifyIntent = new Intent(this, DisplayNotificationActivity.class);
        notifyIntent.putExtra("data", body);
// Set the Activity to start in a new, empty task.
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Create the PendingIntent.
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(notifyPendingIntent);
        notificationManager.notify(0, notificationBuilder.build());


    }

// ... additional methods like setupNotificationChannels if needed ...

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupNotificationChannels(
            String channelId,
            String channelName,
            NotificationManager notificationManager
    ) {
        NotificationChannel channel =
                new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
        channel.enableLights(true);
        channel.setLightColor(Color.GREEN);
        channel.enableVibration(true);
        notificationManager.createNotificationChannel(channel);
    }


}
