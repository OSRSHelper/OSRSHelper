package com.infonuascape.osrshelper.fcm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.infonuascape.osrshelper.controllers.NotificationController;

/**
 * Created by marc_ on 2018-02-03.
 */

public class OSRSFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "OSRSFirebaseMessagingSe";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_URL = "url";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            try {
                final String title = remoteMessage.getData().get(KEY_TITLE);
                final String description = remoteMessage.getData().get(KEY_DESCRIPTION);
                final String url = remoteMessage.getData().get(KEY_URL);

                NotificationController.showOSRSNews(this, title, description, url);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
}
