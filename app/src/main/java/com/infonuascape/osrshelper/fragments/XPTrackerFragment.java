package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.XpTrackerFragmentAdapter;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.listeners.TrackerFetcherListener;
import com.infonuascape.osrshelper.listeners.TrackerUpdateListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.tasks.TrackerUpdateTask;
import com.infonuascape.osrshelper.tasks.TrackerFetcherTask;

import java.util.Map;

public class XPTrackerFragment extends OSRSFragment implements OnClickListener, TrackerUpdateListener, ViewPager.OnPageChangeListener, TrackerFetcherListener {
	private static final String TAG = "XPTrackerFragment";

	private final static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
	private final static String EXTRA_TRACKER_TIME = "EXTRA_TRACKER_TIME";
	private Account account;
	private TextView title;
	private TextView description;
	private XpTrackerFragmentAdapter adapter;
	private ViewPager viewPager;
	private ProfileHeaderFragment profileHeaderFragment;
	private Map<TrackerTime, PlayerSkills> trackings;

	public static XPTrackerFragment newInstance(final Account account) {
		XPTrackerFragment fragment = new XPTrackerFragment();
		Bundle b = new Bundle();
		b.putSerializable(EXTRA_ACCOUNT, account);
		fragment.setArguments(b);
		return fragment;
	}

	public static XPTrackerFragment newInstance(final Account account, final TrackerTime trackerTime) {
		XPTrackerFragment fragment = new XPTrackerFragment();
		Bundle b = new Bundle();
		b.putSerializable(EXTRA_ACCOUNT, account);
		b.putSerializable(EXTRA_TRACKER_TIME, trackerTime);
		fragment.setArguments(b);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.xp_tracker, null);

		account = (Account) getArguments().getSerializable(EXTRA_ACCOUNT);

		profileHeaderFragment = (ProfileHeaderFragment) getChildFragmentManager().findFragmentById(R.id.profile_header);
		profileHeaderFragment.refreshProfile(account);
		profileHeaderFragment.setTitle(R.string.xptracker);

		title = view.findViewById(R.id.track_metadata_title);
		description = view.findViewById(R.id.track_metadata_desc);

		viewPager = view.findViewById(R.id.viewpager);
		adapter = new XpTrackerFragmentAdapter(getChildFragmentManager(), getContext());
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(this);
		viewPager.setOffscreenPageLimit(5);
		TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
		tabLayout.setSelectedTabIndicatorColor(getContext().getResources().getColor(R.color.text_normal));
		tabLayout.setTabTextColors(getContext().getResources().getColor(R.color.text_light), getContext().getResources().getColor(R.color.text_normal));
		tabLayout.setupWithViewPager(viewPager);

		view.findViewById(R.id.update).setOnClickListener(this);

		TrackerTime defaultTime = (TrackerTime) getArguments().getSerializable(EXTRA_TRACKER_TIME);
		if(defaultTime != null && defaultTime != TrackerTime.Day) {
			viewPager.setCurrentItem(defaultTime.ordinal(), true);
		} else {
			adapter.getItem(0).onPageVisible();
		}

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadTracking();
	}

	private void loadTracking() {
		killAsyncTaskIfStillRunning();
		asyncTask = new TrackerFetcherTask(getActivity(), this, account);
		asyncTask.execute();
	}

	@Override
	public void refreshDataOnPreferencesChanged() {
		super.refreshDataOnPreferencesChanged();
		for(int i=0; i < adapter.getCount(); i++) {
			((XPTrackerPeriodFragment) adapter.getItem(i)).reloadData();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.update) {
			updateAccount();
		}
	}

	private void updateAccount() {
		description.setText(null);
		title.setText(R.string.updating);
		profileHeaderFragment.showProgressBar();
		asyncTask = new TrackerUpdateTask(this, account);
		asyncTask.execute();
	}

	@Override
	public void onTrackingFetched(Map<TrackerTime, PlayerSkills> trackings, String lastUpdate, int combatLvl) {
		this.trackings = trackings;
		if (trackings.size() > 0) {
			if (getView() != null) {
				description.setVisibility(View.VISIBLE);
				if (lastUpdate != null) {
					account.combatLvl = combatLvl;
					profileHeaderFragment.showCombatLvl(combatLvl);
					title.setText(R.string.last_update);
					description.setText(lastUpdate);
				}

				for(int i=0; i < adapter.getCount(); i++) {
					((XPTrackerPeriodFragment) adapter.getItem(i)).onForceRepopulate();
				}
			}
		}
	}

	@Override
	public void onTrackingError(final String errorMessage) {
		if(getActivity() != null) {
			getActivity().runOnUiThread(() -> {
				if(getView() != null) {
					title.setText(R.string.error);
					description.setVisibility(View.VISIBLE);
					description.setText(errorMessage);

					for (int i = 0; i < adapter.getCount(); i++) {
						((XPTrackerPeriodFragment) adapter.getItem(i)).onTrackingError();
					}
				}
			});
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		adapter.getItem(position).onPageVisible();
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onUpdatingDone(boolean isSuccess) {
		profileHeaderFragment.hideProgressBar();
		if (isSuccess) {
			for (int i = 0; i < adapter.getCount(); i++) {
				((XPTrackerPeriodFragment) adapter.getItem(i)).onUpdatingSuccessful();
			}
			loadTracking();
		} else {
			title.setText(R.string.updating_failed);
		}
	}

	public PlayerSkills getPlayerSkills(TrackerTime time) {
		return trackings != null ? trackings.get(time) : null;
	}
}
