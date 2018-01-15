package com.infonuascape.osrshelper.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class OSRSWidgetService extends RemoteViewsService {
	
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new OSRSWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
	}
}