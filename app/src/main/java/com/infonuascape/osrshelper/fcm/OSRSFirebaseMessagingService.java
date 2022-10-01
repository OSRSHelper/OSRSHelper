package com.infonuascape.osrshelper.fcm;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.infonuascape.osrshelper.controllers.NotificationController;
import com.infonuascape.osrshelper.utils.Logger;

/**
 * Created by marc_ on 2018-02-03.
 */

public class OSRSFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "OSRSFirebaseMessagingService";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_URL = "url";

    public static final String NEWS_TOPIC = "news";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Logger.add(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Logger.add(TAG, "Message data payload: " + remoteMessage.getData());
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
            Logger.add(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
