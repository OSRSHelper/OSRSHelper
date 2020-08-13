package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.TrackerTableAdapter;
import com.infonuascape.osrshelper.controllers.MainFragmentController;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.utils.Logger;

public class XPTrackerPeriodFragment extends OSRSPagerFragment {
    private static final String TAG = "XPTrackerPeriodFragment";

    private final static String EXTRA_TRACKER_TIME = "EXTRA_TRACKER_TIME";
    private TrackerTime time;
    private TrackerTableAdapter tableFiller;
    private TableLayout tableLayout;
    private View progressBar;
    private TextView emptyView;

    public static XPTrackerPeriodFragment newInstance(final TrackerTime period) {
        XPTrackerPeriodFragment fragment = new XPTrackerPeriodFragment();
        Bundle b = new Bundle();
        b.putSerializable(EXTRA_TRACKER_TIME, period);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.xp_tracker_period, null);

        progressBar = view.findViewById(R.id.progressbar);
        emptyView = view.findViewById(R.id.empty_view);

        tableLayout = view.findViewById(R.id.table_tracking);
        tableFiller = new TrackerTableAdapter(getContext(), tableLayout);

        return view;
    }

    @Override
    public void onPageVisible() {
        Logger.add(TAG, ": onPageVisible");
        if (time == null) {
            time = (TrackerTime) getArguments().getSerializable(EXTRA_TRACKER_TIME);
        }
        XPTrackerFragment xpTrackerFragment = getXPTrackerFragment();
        if (tableFiller != null && tableFiller.isEmpty() && xpTrackerFragment != null && xpTrackerFragment.getPlayerSkills(time) != null) {
            populateTable();
        }
    }

    public void onTrackingError() {
        Logger.add(TAG, ": onTrackingError");
        if (getView() != null) {
            tableLayout.removeAllViews();
            progressBar.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(R.string.xp_tracker_error_period);
        }
    }

    public void onUpdatingSuccessful() {
        Logger.add(TAG, ": onUpdatingSuccessful");
        if (getView() != null) {
            tableLayout.removeAllViews();
            progressBar.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    public void onForceRepopulate() {
        populateTable();
    }

    @Override
    public void refreshDataOnPreferencesChanged() {
        super.refreshDataOnPreferencesChanged();
        populateTable();
    }

    private void populateTable() {
        Logger.add(TAG, ": populateTable");
        XPTrackerFragment xpTrackerFragment = getXPTrackerFragment();
        PlayerSkills playerSkills = xpTrackerFragment != null ? xpTrackerFragment.getPlayerSkills(time) : null;
        if (getView() != null) {
            progressBar.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            if (playerSkills != null && !TextUtils.isEmpty(playerSkills.lastUpdate)) {
                tableFiller.fill(playerSkills);
            } else {
                emptyView.setText(R.string.xp_tracker_no_gain_period);
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    private XPTrackerFragment getXPTrackerFragment() {
        final OSRSFragment currentFragment = MainFragmentController.getInstance().getCurrentFragment();
        if (currentFragment instanceof XPTrackerFragment) {
            return ((XPTrackerFragment) currentFragment);
        }
        return null;
    }

    public void reloadData() {
        Logger.add(TAG, ": reloadData");
        populateTable();
    }
}
