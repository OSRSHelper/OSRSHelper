package com.infonuascape.osrshelper.widget.grandexchange;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class GrandExchangeWidgetService extends RemoteViewsService {
	
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new GrandExchangeWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
	}
}