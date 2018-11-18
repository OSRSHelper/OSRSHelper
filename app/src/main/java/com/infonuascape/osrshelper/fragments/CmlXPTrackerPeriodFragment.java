package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.CmlTrackerTableAdapter;
import com.infonuascape.osrshelper.controllers.MainFragmentController;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.listeners.TrackerFetcherListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.tasks.CmlTrackerFetcherTask;
import com.infonuascape.osrshelper.utils.Logger;

import androidx.annotation.Nullable;

public class CmlXPTrackerPeriodFragment extends OSRSPagerFragment implements TrackerFetcherListener {
	private static final String TAG = "CmlXPTrackerPeriodFragm";

	private final static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
	private final static String EXTRA_TRACKER_TIME = "EXTRA_TRACKER_TIME";
	private Account account;
	private TrackerTime time;
	private PlayerSkills playerSkills;
	private CmlTrackerTableAdapter tableFiller;
	private TableLayout tableLayout;
	private View progressBar;
	private View emptyView;
	private boolean forceRepopulate;

	public static CmlXPTrackerPeriodFragment newInstance(final Account account, final TrackerTime period) {
		CmlXPTrackerPeriodFragment fragment = new CmlXPTrackerPeriodFragment();
		Bundle b = new Bundle();
		b.putSerializable(EXTRA_ACCOUNT, account);
		b.putSerializable(EXTRA_TRACKER_TIME, period);
		fragment.setArguments(b);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.cml_xp_tracker_period, null);

		progressBar = view.findViewById(R.id.progressbar);
		emptyView = view.findViewById(R.id.empty_view);

		tableLayout = view.findViewById(R.id.table_tracking);
		tableFiller = new CmlTrackerTableAdapter(getContext(), tableLayout);

		return view;
	}

	@Override
	public void onPageVisible() {
		if(time == null) {
			account = (Account) getArguments().getSerializable(EXTRA_ACCOUNT);
			time = (TrackerTime) getArguments().getSerializable(EXTRA_TRACKER_TIME);
		}
		if(forceRepopulate || playerSkills == null) {
			if(time != null) {
				forceRepopulate = false;
				if(getView() != null) {
					tableLayout.removeAllViews();
					progressBar.setVisibility(View.VISIBLE);
					emptyView.setVisibility(View.GONE);
				}
				killAsyncTaskIfStillRunning();
				asyncTask = new CmlTrackerFetcherTask(getActivity(), this, account, time);
				asyncTask.execute();
			}
		} else {
			populateTable();
		}
	}

	public void onForceRepopulate() {
		forceRepopulate = true;
	}

	@Override
	public void refreshDataOnPreferencesChanged() {
		super.refreshDataOnPreferencesChanged();
		if(playerSkills != null) {
			populateTable();
		}
	}

	private void populateTable() {
		if (getView() != null) {
			progressBar.setVisibility(View.GONE);
			tableFiller.fill(playerSkills);
		}
	}

	@Override
	public void onTrackingFetched(final PlayerSkills playerSkills) {
		Logger.add(TAG, ": onTrackingFetched");
		this.playerSkills = playerSkills;
		if (playerSkills != null) {
			populateTable();
		} else if (getView() != null) {
			emptyView.setVisibility(View.VISIBLE);
		}

		final OSRSFragment currentFragment = MainFragmentController.getInstance().getCurrentFragment();
		if(currentFragment instanceof CmlXPTrackerFragment) {
			((CmlXPTrackerFragment) currentFragment).onTrackingFetched(playerSkills);
		}
	}

	@Override
	public void onTrackingError(final String errorMessage) {
		Logger.add(TAG, ": onTrackingError: errorMessage=" + errorMessage);
		if(getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(getView() != null) {
						progressBar.setVisibility(View.GONE);
						emptyView.setVisibility(View.VISIBLE);
					}

				}
			});
		}

		final OSRSFragment currentFragment = MainFragmentController.getInstance().getCurrentFragment();
		if(currentFragment instanceof CmlXPTrackerFragment) {
			((CmlXPTrackerFragment) currentFragment).onTrackingError(errorMessage);
		}
	}

	public void reloadData() {
		if(playerSkills != null) {
			populateTable();
		}
	}
}
