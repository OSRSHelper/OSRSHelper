package com.infonuascape.osrshelper.bubble;

import android.content.Intent;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.utils.Logger;

import io.mattcarroll.hover.HoverView;
import io.mattcarroll.hover.OnExitListener;
import io.mattcarroll.hover.window.HoverMenuService;

public class HoverMenuServiceImpl extends HoverMenuService implements HoverView.Listener, OnExitListener {
    private static final String TAG = "HoverMenuServiceImpl";

    private Handler handler;
    private HoverMenuImpl menu;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.add(TAG, ": onCreate");
        handler = new Handler();
        PreferencesController.setPreference(this, PreferencesController.USER_PREF_HOVER_MENU_ENABLED, true);
    }

    @Override
    protected void onHoverMenuLaunched(@NonNull Intent intent, @NonNull HoverView hoverView) {
        Logger.add(TAG, ": onHoverMenuLaunched: intent=", intent, ", hoverView=", hoverView);
        if (menu == null) {
            hoverView.addOnExpandAndCollapseListener(this);
            hoverView.setOnExitListener(this);
            menu = new HoverMenuImpl(this);
            hoverView.setMenu(menu);
            hoverView.collapse();
        }
    }

    @Override
    protected void onHoverMenuExitingByUserRequest() {
        super.onHoverMenuExitingByUserRequest();
        Logger.add(TAG, ": onHoverMenuExitingByUserRequest");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.add(TAG, ": onDestroy");
        PreferencesController.setPreference(this, PreferencesController.USER_PREF_HOVER_MENU_ENABLED, false);
    }

    @Override
    public void onExpanding() {
        Logger.add(TAG, ": onExpanding");
    }

    @Override
    public void onExpanded() {
        Logger.add(TAG, ": onExpanded");
    }

    @Override
    public void onCollapsing() {
        Logger.add(TAG, ": onCollapsing");
    }

    @Override
    public void onCollapsed() {
        Logger.add(TAG, ": onCollapsed");
    }

    @Override
    public void onClosing() {
        Logger.add(TAG, ": onClosing");
    }

    @Override
    public void onClosed() {
        Logger.add(TAG, ": onClosed");
        stopSelf();
    }

    @Override
    public void onExit() {
        Logger.add(TAG, ": onExit");
    }
}
