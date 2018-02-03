package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.CmlXpTrackerFragmentAdapter;
import com.infonuascape.osrshelper.listeners.TrackerUpdateListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.tasks.CmlTrackerUpdateTask;

public class CmlXPTrackerFragment extends OSRSFragment implements OnClickListener, TrackerUpdateListener, ViewPager.OnPageChangeListener {
	private final static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
	private Account account;
	private PlayerSkills playerSkills;
	private TextView title;
	private TextView description;
	private CmlXpTrackerFragmentAdapter adapter;
	private ViewPager viewPager;

	public static CmlXPTrackerFragment newInstance(final Account account) {
		CmlXPTrackerFragment fragment = new CmlXPTrackerFragment();
		Bundle b = new Bundle();
		b.putSerializable(EXTRA_ACCOUNT, account);
		fragment.setArguments(b);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.xptracker, null);

		account = (Account) getArguments().getSerializable(EXTRA_ACCOUNT);

		ProfileHeaderFragment profileHeaderFragment = (ProfileHeaderFragment) getChildFragmentManager().findFragmentById(R.id.profile_header);
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

		updateAccount();

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
		this.playerSkills = playerSkills;

		if (playerSkills != null) {
			if (getView() != null) {
				description.setVisibility(View.VISIBLE);
				if (playerSkills.sinceWhen != null) {
					title.setText(R.string.tracking_since);
					description.setText(playerSkills.sinceWhen);
				} else if (playerSkills.lastUpdate != null) {
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
