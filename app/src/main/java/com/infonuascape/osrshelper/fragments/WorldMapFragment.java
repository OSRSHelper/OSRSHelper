package com.infonuascape.osrshelper.fragments;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.android.material.navigation.NavigationView;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.PoIAdapter;
import com.infonuascape.osrshelper.models.PointOfInterest;
import com.infonuascape.osrshelper.models.PointOfInterestHeader;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.Utils;

public class WorldMapFragment extends OSRSFragment implements OnClickListener, PoIAdapter.OnItemClickListener {
    private static final String TAG = "WorldMapFragment";
    private static final String KEY_X = "X";
    private static final String KEY_Y = "Y";
    private static final String MAP_FILE_NAME = "osrs.jpg";
    private DrawerLayout drawerLayout;
    private RecyclerView poICitiesRecyclerView;
    private PoIAdapter adapterCities;
    private NavigationView navigationView;


    private SubsamplingScaleImageView imageSurfaceView;

    public static WorldMapFragment newInstance() {
        WorldMapFragment fragment = new WorldMapFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.world_map, null);

        imageSurfaceView = view.findViewById(R.id.world_map_image);
        drawerLayout = view.findViewById(R.id.drawer_map_layout);
        poICitiesRecyclerView = view.findViewById(R.id.poi_cities);
        navigationView = view.findViewById(R.id.navigation);

        view.findViewById(R.id.world_map_open).setOnClickListener(this);

        initWorldMap(savedInstanceState);
        initPoT();
        return view;
    }

    private void initWorldMap(final Bundle savedInstanceState) {
        Logger.add(TAG, ": initWorldMap: savedInstanceState=", savedInstanceState);
        try {
            imageSurfaceView.setImage(ImageSource.asset(MAP_FILE_NAME));
            imageSurfaceView.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
                @Override
                public void onReady() {
                    // Setup/restore state
                    if (savedInstanceState != null) {
                        Logger.add(TAG, "restoring state");
                        float x = savedInstanceState.getFloat(KEY_X);
                        float y = savedInstanceState.getFloat(KEY_Y);
                        animateMap(x, y);
                    } else {
                        animateMap(Utils.VARROCK_POINT.x, Utils.VARROCK_POINT.y);
                    }
                }

                @Override
                public void onImageLoaded() {
                }

                @Override
                public void onPreviewLoadError(Exception e) {
                }

                @Override
                public void onImageLoadError(Exception e) {
                }

                @Override
                public void onTileLoadError(Exception e) {
                }

                @Override
                public void onPreviewReleased() {

                }
            });
        } catch (Exception e) {
            Logger.addException(TAG, e);
        }
    }

    private void animateMap(final float x, final float y) {
        Logger.add(TAG, ": animateMap: x=", x, ", y=", y);
        if (imageSurfaceView.isReady()) {
            imageSurfaceView.animateScaleAndCenter(imageSurfaceView.getMaxScale() * 0.7f, new PointF(x, y)).start();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers();
            return true;
        }

        return false;
    }

    private void initPoT() {
        Logger.add(TAG, ": initPoT");
        //Cities
        adapterCities = new PoIAdapter(Utils.getCitiesPoI());
        adapterCities.setOnItemClickListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        poICitiesRecyclerView.setAdapter(adapterCities);
        poICitiesRecyclerView.setLayoutManager(llm);
    }

    public void zoomToPoT(Point point) {
        Logger.add(TAG, ": zoomToPoT: point=", point);
        animateMap(point.x, point.y);
    }

    @Override
    public void onResume() {
        super.onResume();
        imageSurfaceView.animateCenter(new PointF(Utils.VARROCK_POINT.x, Utils.VARROCK_POINT.y));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imageSurfaceView != null && imageSurfaceView.isReady()) {
            imageSurfaceView.recycle();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (imageSurfaceView != null) {
            PointF p = imageSurfaceView.getCenter();
            if (p != null) {
                outState.putFloat(KEY_X, p.x);
                outState.putFloat(KEY_Y, p.y);
            }
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.world_map_open) {
            drawerLayout.openDrawer(navigationView);
        }
    }

    @Override
    public void onItemClicked(int position) {
        Logger.add(TAG, ": onItemClicked: position=", position);
        PointOfInterest pointOfInterest = adapterCities.getItem(position);
        if (pointOfInterest != null && !(pointOfInterest instanceof PointOfInterestHeader)) {
            zoomToPoT(pointOfInterest.getPoint());
            drawerLayout.closeDrawers();
        }
    }
}
