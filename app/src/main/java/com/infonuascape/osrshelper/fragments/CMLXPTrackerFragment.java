package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.listeners.TrackerFetcherListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.tasks.CMLTrackerFetcherTask;
import com.infonuascape.osrshelper.adapters.CMLTrackerTableAdapter;

public class CMLXPTrackerFragment extends OSRSFragment implements OnItemSelectedListener, OnClickListener, TrackerFetcherListener {
	private final static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
	private Account account;
	private Spinner spinner;
	private PlayerSkills playerSkills;
	private CMLTrackerTableAdapter tableFiller;
	private TextView header;

	public static CMLXPTrackerFragment newInstance(final Account account) {
		CMLXPTrackerFragment fragment = new CMLXPTrackerFragment();
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

		header = view.findViewById(R.id.track_metadata);

		TableLayout tableLayout = view.findViewById(R.id.table_tracking);
		tableFiller = new CMLTrackerTableAdapter(getContext(), tableLayout);

		spinner = (Spinner) view.findViewById(R.id.time_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.time_array,
				R.layout.spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(3);
		spinner.setOnItemSelectedListener(this);

		view.findViewById(R.id.update).setOnClickListener(this);

		return view;
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
			getView().findViewById(R.id.progressbar).setVisibility(View.GONE);
			header.setVisibility(View.VISIBLE);
			if (playerSkills.sinceWhen != null) {
				header.setText(getString(R.string.tracking_since,
						playerSkills.sinceWhen));
			} else if (playerSkills.lastUpdate != null) {
				header.setText(getString(R.string.last_update,
						playerSkills.lastUpdate));
			} else {
				header.setText(getString(R.string.tracking_starting));
			}

			tableFiller.fill(playerSkills);
		}
	}

	private void createAsyncTaskToPopulate(String selectedTime, boolean isUpdating) {
		TrackerTime time = null;

		if (selectedTime.equals("Hour")) {
			time = TrackerTime.Hour;
		} else if (selectedTime.equals("Day")) {
			time = TrackerTime.Day;
		} else if (selectedTime.equals("Week")) {
			time = TrackerTime.Week;
		} else if (selectedTime.equals("Month")) {
			time = TrackerTime.Month;
		} else if (selectedTime.equals("Year")) {
			time = TrackerTime.Year;
		}

		if (time != null) {
			((TableLayout) getView().findViewById(R.id.table_tracking)).removeAllViews();
			header.setText(R.string.loading_tracking);
			getView().findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
			new CMLTrackerFetcherTask(getContext(), this, account, time, isUpdating).execute();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(spinner.getSelectedItem() instanceof String) {
			createAsyncTaskToPopulate((String) spinner.getSelectedItem(), false);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.update) {
			createAsyncTaskToPopulate((String) spinner.getSelectedItem(), true);
		}
	}

	@Override
	public void onTrackingFetched(PlayerSkills playerSkills) {
		this.playerSkills = playerSkills;
		if (playerSkills != null) {
			populateTable();
		}
	}

	@Override
	public void onTrackingError(final String errorMessage) {
		if(getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					getView().findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
					header.setText(errorMessage);
				}
			});
		}
	}
}
