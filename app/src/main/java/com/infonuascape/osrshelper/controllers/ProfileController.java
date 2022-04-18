package com.infonuascape.osrshelper.controllers;

import android.content.Context;

import com.infonuascape.osrshelper.models.Account;

import java.util.ArrayList;
import java.util.List;

public class ProfileController {
    private static final String TAG = "ProfileController";

    private final Context context;
    private Account currentProfile;
    private final List<Listener> listeners;

    public ProfileController(final Context context) {
        this.context = context;

        listeners = new ArrayList<>();
    }

    public Account getCurrentProfile() {
        return currentProfile;
    }

    public interface Listener {
        void onCurrentProfileChanged(Account currentProfile);
    }
}
