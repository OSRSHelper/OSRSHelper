package com.infonuascape.osrshelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.infonuascape.osrshelper.adapters.PoIAdapter;
import com.infonuascape.osrshelper.utils.Utils;

public class WorldMapActivity extends Activity implements OnItemClickListener, OnClickListener {
	private static final String TAG = "WorldMapActivity";
	private static final String KEY_X = "X";
	private static final String KEY_Y = "Y";
	private static final String MAP_FILE_NAME = "osrs.jpg";
	private DrawerLayout drawerLayout;
	private ListView poICitiesListView;
	private PoIAdapter adapterCities;
	private NavigationView navigationView;


	private SubsamplingScaleImageView imageSurfaceView;

	public static void show(final Context context){
		Intent i = new Intent(context, WorldMapActivity.class);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.world_map);

		imageSurfaceView = (SubsamplingScaleImageView) findViewById(R.id.world_map_image);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		poICitiesListView = (ListView) findViewById(R.id.poi_cities);
		navigationView = (NavigationView) findViewById(R.id.navigation);
		
		findViewById(R.id.world_map_open).setOnClickListener(this);

		initWorldMap(savedInstanceState);
		initPoT();
	}

	private void initWorldMap(final Bundle savedInstanceState) {
		try {
			imageSurfaceView.setImage(ImageSource.asset(MAP_FILE_NAME));
			imageSurfaceView.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
				@Override
				public void onReady() {
					// Setup/restore state
					if (savedInstanceState != null) {
						Log.d(TAG, "restoring state");
						float x = savedInstanceState.getFloat(KEY_X);
						float y = savedInstanceState.getFloat(KEY_Y);
						animateMap(x, y);
					} else {
						animateMap(Utils.VARROCK_POINT.x, Utils.VARROCK_POINT.y);
					}
				}

				@Override
				public void onImageLoaded() {}

				@Override
				public void onPreviewLoadError(Exception e) {
					finish();
				}

				@Override
				public void onImageLoadError(Exception e) {
					finish();
				}

				@Override
				public void onTileLoadError(Exception e) {
					finish();
				}

				@Override
				public void onPreviewReleased() {

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			finish();
		}
	}

	private void animateMap(final float x, final float y) {
		if(imageSurfaceView.isReady()) {
			imageSurfaceView.animateScaleAndCenter(imageSurfaceView.getMaxScale() * 0.7f, new PointF(x, y)).start();
		}
	}

	@Override
	public void onBackPressed() {
		if(drawerLayout.isDrawerOpen(navigationView)){
			drawerLayout.closeDrawers();
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
		animateMap(point.x, point.y);
	}

	@Override
	protected void onResume() {
		super.onResume();
		imageSurfaceView.animateCenter(new PointF(Utils.VARROCK_POINT.x, Utils.VARROCK_POINT.y));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(imageSurfaceView != null && imageSurfaceView.isReady()) {
			imageSurfaceView.recycle();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(imageSurfaceView != null) {
			PointF p = imageSurfaceView.getCenter();
			if(p != null) {
				outState.putFloat(KEY_X, p.x);
				outState.putFloat(KEY_Y, p.y);
			}
		}
		super.onSaveInstanceState(outState);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		zoomToPoT(adapterCities.getItem(position).getPoint());
		drawerLayout.closeDrawers();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.world_map_open){
			drawerLayout.openDrawer(navigationView);
		}
	}
}
