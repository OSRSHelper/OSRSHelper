package com.infonuascape.osrshelper.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.infonuascape.osrshelper.utils.Logger;

/**
 * Created by marc_ on 2018-02-03.
 */

public class OSRSFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "OSRSFirebaseInstanceIDS";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Logger.add(TAG, ": onTokenRefresh");
    }
}
