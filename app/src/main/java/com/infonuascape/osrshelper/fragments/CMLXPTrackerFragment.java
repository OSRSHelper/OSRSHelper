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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.listeners.TrackerFetcherListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.tasks.CMLTrackerFetcherTask;
import com.infonuascape.osrshelper.utils.tablesfiller.CMLTrackerTableFiller;

import org.w3c.dom.Text;

public class CMLXPTrackerFragment extends OSRSFragment implements OnItemSelectedListener, OnClickListener, CompoundButton.OnCheckedChangeListener, TrackerFetcherListener {
	private final static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
	private Account account;
	private Spinner spinner;
	private CheckBox virtualLevelsCB;
	private PlayerSkills playerSkills;
	private TableLayout tableLayout;
	private CMLTrackerTableFiller tableFiller;
	private ProfileHeaderFragment profileHeaderFragment;
	private TextView header;

	public static CMLXPTrackerFragment newInstance(final Account account) {
		Answers.getInstance().logContentView(new ContentViewEvent()
				.putContentName("CML XP Tracker"));
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

		profileHeaderFragment = (ProfileHeaderFragment) getChildFragmentManager().findFragmentById(R.id.profile_header);
		profileHeaderFragment.refreshProfile(account);
		profileHeaderFragment.setTitle(R.string.cml_xptracker);

		header = view.findViewById(R.id.header);

		tableLayout = view.findViewById(R.id.table_tracking);
		tableFiller = new CMLTrackerTableFiller(getContext(), tableLayout);

		virtualLevelsCB = (CheckBox) view.findViewById(R.id.cb_virtual_levels);
		virtualLevelsCB.setOnCheckedChangeListener(this);
		virtualLevelsCB.setChecked(PreferencesController.getBooleanPreference(getContext(), PreferencesController.USER_PREF_SHOW_VIRTUAL_LEVELS, false));
		virtualLevelsCB.setVisibility(View.GONE);

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

	private void changeHeaderText(final String text, final int progressBarVisibility) {
		if(getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					getView().findViewById(R.id.progressbar).setVisibility(progressBarVisibility);
					header.setText(text);
				}
			});
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		PreferencesController.setPreference(getContext(), PreferencesController.USER_PREF_SHOW_VIRTUAL_LEVELS, isChecked);
		if(playerSkills != null) {
			populateTable();
		}
	}



	private void populateTable() {
		changeHeaderText(getString(R.string.showing_tracking), View.GONE);

		((TextView) getView().findViewById(R.id.track_metadata)).setVisibility(View.VISIBLE);
		if (playerSkills.sinceWhen != null) {
			((TextView) getView().findViewById(R.id.track_metadata)).setText(getString(R.string.tracking_since,
					playerSkills.sinceWhen));
		} else if (playerSkills.lastUpdate != null) {
			((TextView) getView().findViewById(R.id.track_metadata)).setText(getString(R.string.last_update,
					playerSkills.lastUpdate));
		} else {
			((TextView) getView().findViewById(R.id.track_metadata)).setText(getString(R.string.tracking_starting));
		}

		tableFiller.fill(playerSkills);

		virtualLevelsCB.setVisibility(playerSkills.hasOneAbove99 ? View.VISIBLE : View.GONE);
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
			((TextView) getView().findViewById(R.id.track_metadata)).setText("");
			changeHeaderText(getString(R.string.loading_tracking), View.VISIBLE);
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
	public void onTrackingError(String errorMessage) {
		changeHeaderText(errorMessage, View.GONE);
	}
}
