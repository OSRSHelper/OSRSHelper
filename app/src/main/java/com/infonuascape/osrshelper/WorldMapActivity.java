package com.infonuascape.osrshelper;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.infonuascape.osrshelper.adapters.PoIAdapter;
import com.infonuascape.osrshelper.utils.Utils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sigseg.android.io.RandomAccessFileInputStream;
import com.sigseg.android.map.ImageSurfaceView;

public class WorldMapActivity extends Activity implements OnItemClickListener, OnClickListener {
	private static final String TAG = "WorldMapActivity";
	private static final String KEY_X = "X";
	private static final String KEY_Y = "Y";
	private static final String KEY_FN = "FN";
	private static final Point DEFAULT_POINT = new Point(3050, 2580);
	private static final String MAP_FILE_NAME = "osrs.png";
	private SlidingMenu slidingMenu;
	private ListView poICitiesListView;
	private PoIAdapter adapterCities;
	private long lastTimeZoomed = 0;


	private ImageSurfaceView imageSurfaceView;
	private String filename = null;

	public static void show(final Context context){
		Intent i = new Intent(context, WorldMapActivity.class);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.world_map_container);

		imageSurfaceView = (ImageSurfaceView) findViewById(R.id.world_map_image);
		slidingMenu = (SlidingMenu) findViewById(R.id.slidingmenulayout);
		poICitiesListView = (ListView) findViewById(R.id.poi_cities);
		
		findViewById(R.id.world_map_open).setOnClickListener(this);

		// Setup/restore state
		if (savedInstanceState != null && savedInstanceState.containsKey(KEY_X) && savedInstanceState.containsKey(KEY_Y)) {
			Log.d(TAG, "restoring state");
			int x = (Integer) savedInstanceState.get(KEY_X);
			int y = (Integer) savedInstanceState.get(KEY_Y);

			String fn = null;
			if (savedInstanceState.containsKey(KEY_FN))
				fn = (String) savedInstanceState.get(KEY_FN);

			try {
				if (fn == null || fn.length()==0) {
					imageSurfaceView.setInputStream(getAssets().open(MAP_FILE_NAME));
				} else {
					imageSurfaceView.setInputStream(new RandomAccessFileInputStream(fn));
				}
				imageSurfaceView.setViewport(new Point(x, y));
			} catch (java.io.IOException e) {
				Log.e(TAG, e.getMessage());
			}
		} else {
			// Centering the map to start
			Intent intent = getIntent();
			try {
				Uri uri = null;
				if (intent!=null)
					uri = getIntent().getData();

				InputStream is;
				if (uri != null) {
					filename = uri.getPath();
					is = new RandomAccessFileInputStream(uri.getPath());
				} else {
					is = getAssets().open(MAP_FILE_NAME);
				}

				imageSurfaceView.setInputStream(is);
			} catch (java.io.IOException e) {
				Log.e(TAG, e.getMessage());
			}

			PointF center = getCenterScreen();
			imageSurfaceView.setViewport(new Point(DEFAULT_POINT.x - (int)center.x, DEFAULT_POINT.y - (int)center.y));
		}

		initPoT();
	}
	
	

	@Override
	public void onBackPressed() {
		if(slidingMenu.isMenuShowing()){
			slidingMenu.toggle(true);
		} else{
			super.onBackPressed();
		}
	}

	private void initPoT() {
		//Cities
		adapterCities = new PoIAdapter(Utils.getCitiesPoI());
		poICitiesListView.setAdapter(adapterCities);
		poICitiesListView.setOnItemClickListener(this);
	}

	public void zoomToPoT(Point point){
		PointF center = getCenterScreen();
		if(imageSurfaceView.getLastScaleTime() >= lastTimeZoomed){
			imageSurfaceView.setViewport(new Point(point.x - (int)center.x, point.y - (int)center.y));
			imageSurfaceView.zoom(0.4f, center);
		}
		imageSurfaceView.setViewport(new Point(point.x - (int)(center.x/2.5), point.y - (int)(center.y/2.5)));	
		
		imageSurfaceView.zoom(0.4f, center);
		lastTimeZoomed = System.currentTimeMillis();
	}

	@Override
	protected void onResume() {
		super.onResume();

		PointF center = getCenterScreen();
		imageSurfaceView.setViewportCenter();
		imageSurfaceView.setViewport(new Point(DEFAULT_POINT.x - (int)center.x, DEFAULT_POINT.y - (int)center.y));
	}

	public PointF getCenterScreen(){
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		PointF p = new PointF(size.x/2, size.y/2);
		return p;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Point p = new Point();
		imageSurfaceView.getViewport(p);
		outState.putInt(KEY_X, p.x);
		outState.putInt(KEY_Y, p.y);
		if (filename!=null)
			outState.putString(KEY_FN, filename);
		super.onSaveInstanceState(outState);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		zoomToPoT(adapterCities.getItem(position).getPoint());
		slidingMenu.toggle(true);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.world_map_open){
			slidingMenu.toggle(true);
		}
	}
}
