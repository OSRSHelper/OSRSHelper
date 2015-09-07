package com.infonuascape.osrshelper.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.db.OSRSHelperDataSource;

public class OSRSWidgetConfigurationActivity extends Activity implements OnClickListener {

	private EditText editText;
	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setResult(RESULT_CANCELED);

		setContentView(R.layout.widget_config);

		editText = (EditText) findViewById(R.id.edit_text);
		findViewById(R.id.btn_ok).setOnClickListener(this);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		// If they gave us an intent without the widget id, just bail.
		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}
	}



	@Override
	public void onClick(View arg0) {
		if(!editText.getText().toString().isEmpty()) {
			final String username = editText.getText().toString();
			OSRSHelperDataSource osrsHelperDataSource = new OSRSHelperDataSource(this);
			osrsHelperDataSource.open();
			osrsHelperDataSource.setUsernameForWidget(mAppWidgetId, username);
			osrsHelperDataSource.close();
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			setResult(RESULT_OK, resultValue);
			finish();
		}
	}

}