package com.infonuascape.osrshelper.bubble;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import io.mattcarroll.hover.HoverView;
import io.mattcarroll.hover.window.HoverMenuService;

public class BubbleService extends HoverMenuService {
    private BubbleMenu bubbleMenu;

    @Override
    public void onCreate() {
        super.onCreate();
        bubbleMenu = new BubbleMenu(this);
    }

    @Override
    protected Context getContextForHoverMenu() {
        return super.getContextForHoverMenu();
    }

    @Override
    protected HoverView getHoverView() {
        return super.getHoverView();
    }

    @Override
    protected int getForegroundNotificationId() {
        return super.getForegroundNotificationId();
    }

    @Override
    protected Notification getForegroundNotification() {
        return super.getForegroundNotification();
    }

    @Override
    protected void onHoverMenuLaunched(Intent intent, HoverView hoverView) {
        hoverView.setMenu(bubbleMenu);
        hoverView.collapse();
    }

    @Override
    protected void onHoverMenuExitingByUserRequest() {
        super.onHoverMenuExitingByUserRequest();
    }
}
