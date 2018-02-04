package com.infonuascape.osrshelper.fcm;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.infonuascape.osrshelper.controllers.NotificationController;

/**
 * Created by marc_ on 2018-02-03.
 */

public class OSRSFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "OSRSFirebaseMessagingSe";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            try {
                final String title = remoteMessage.getData().get("title");
                final String description = remoteMessage.getData().get("description");
                final String url = remoteMessage.getData().get("url");

                NotificationController.showOSRSNews(this, title, description, url);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
    }
}
