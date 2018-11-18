package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.CmlXpTrackerFragmentAdapter;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.listeners.TrackerUpdateListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.tasks.CmlTrackerUpdateTask;
import com.infonuascape.osrshelper.utils.Utils;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CmlXPTrackerFragment extends OSRSFragment implements OnClickListener, TrackerUpdateListener, ViewPager.OnPageChangeListener {
	private static final String TAG = "CmlXPTrackerFragment";

	private final static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
	private final static String EXTRA_TRACKER_TIME = "EXTRA_TRACKER_TIME";
	private Account account;
	private TextView title;
	private TextView description;
	private CmlXpTrackerFragmentAdapter adapter;
	private ViewPager viewPager;
	private ProfileHeaderFragment profileHeaderFragment;

	public static CmlXPTrackerFragment newInstance(final Account account) {
		CmlXPTrackerFragment fragment = new CmlXPTrackerFragment();
		Bundle b = new Bundle();
		b.putSerializable(EXTRA_ACCOUNT, account);
		fragment.setArguments(b);
		return fragment;
	}

	public static CmlXPTrackerFragment newInstance(final Account account, final TrackerTime trackerTime) {
		CmlXPTrackerFragment fragment = new CmlXPTrackerFragment();
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

		View view = inflater.inflate(R.layout.xptracker, null);

		account = (Account) getArguments().getSerializable(EXTRA_ACCOUNT);

		profileHeaderFragment = (ProfileHeaderFragment) getChildFragmentManager().findFragmentById(R.id.profile_header);
		profileHeaderFragment.refreshProfile(account);
		profileHeaderFragment.setTitle(R.string.cml_xptracker);

		title = view.findViewById(R.id.track_metadata_title);
		description = view.findViewById(R.id.track_metadata_desc);

		viewPager = view.findViewById(R.id.viewpager);
		adapter = new CmlXpTrackerFragmentAdapter(getChildFragmentManager(), getContext(), account);
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(this);
		viewPager.setOffscreenPageLimit(5);
		TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
		tabLayout.setSelectedTabIndicatorColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
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
	public void refreshDataOnPreferencesChanged() {
		super.refreshDataOnPreferencesChanged();
		for(int i=0; i < adapter.getCount(); i++) {
			((CmlXPTrackerPeriodFragment) adapter.getItem(i)).reloadData();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.update) {
			updateAccount();
		}
	}

	private void updateAccount() {
		title.setText(R.string.loading);
		description.setText(null);
		asyncTask = new CmlTrackerUpdateTask(getContext(), this, account);
		asyncTask.execute();
	}

	public void onTrackingFetched(final PlayerSkills playerSkills) {
		if (playerSkills != null) {
			account.combatLvl = Utils.getCombatLvl(playerSkills);
			DBController.setCombatLvlForAccount(getContext(), account);
			if (getView() != null) {
				profileHeaderFragment.showCombatLvl(Utils.getCombatLvl(playerSkills));
				description.setVisibility(View.VISIBLE);
				if (playerSkills.lastUpdate != null) {
					title.setText(R.string.last_update);
					description.setText(playerSkills.lastUpdate);
				} else {
					title.setText(R.string.now_tracking);
					description.setText(R.string.tracking_starting);
				}
			}
		}
	}

	public void onTrackingError(final String errorMessage) {
		if(getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(getView() != null) {
						title.setText(R.string.error);
						description.setText(errorMessage);
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
		for(int i=0; i < adapter.getCount(); i++) {
			((CmlXPTrackerPeriodFragment) adapter.getItem(i)).onForceRepopulate();
		}
		final int position = viewPager.getCurrentItem();
		adapter.getItem(position).onPageVisible();
	}
}
