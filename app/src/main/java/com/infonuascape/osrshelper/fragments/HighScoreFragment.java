package com.infonuascape.osrshelper.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.HiscoreAdapter;
import com.infonuascape.osrshelper.listeners.HiscoresFetcherListener;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.tasks.HiscoresFetcherTask;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.ShareImageUtils;
import com.infonuascape.osrshelper.views.RSView;
import com.infonuascape.osrshelper.views.RSViewDialog;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

public class HighScoreFragment extends OSRSFragment implements View.OnClickListener, RecyclerItemClickListener, HiscoresFetcherListener {
	private static final String TAG = "HighScoreFragment";

	private final static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
	private static final int WRITE_PERMISSION_REQUEST_CODE = 9001;
	private Account account;
	private PlayerSkills playerSkills;
	private View errorView;
	private HiscoreAdapter hiscoreAdapter;
	private RSView rsView;
	private RecyclerView recyclerView;
	private ProfileHeaderFragment profileHeaderFragment;

	public static HighScoreFragment newInstance(final Account account) {
    	HighScoreFragment fragment = new HighScoreFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(EXTRA_ACCOUNT, account);
		fragment.setArguments(bundle);
		return fragment;
    }

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Logger.add(TAG, ": onCreateView");
		View view = inflater.inflate(R.layout.hiscores, null);

		account = (Account) getArguments().getSerializable(EXTRA_ACCOUNT);
		if(account == null) {
			return view;
		}

		profileHeaderFragment = (ProfileHeaderFragment) getChildFragmentManager().findFragmentById(R.id.profile_header);
		profileHeaderFragment.refreshProfile(account);
		profileHeaderFragment.setTitle(R.string.highscores);

		rsView = view.findViewById(R.id.rs_view);

		recyclerView = view.findViewById(R.id.hiscore_list);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setNestedScrollingEnabled(false);
		hiscoreAdapter = new HiscoreAdapter(getContext());
		recyclerView.setAdapter(hiscoreAdapter);
		final StickyRecyclerHeadersDecoration stickyRecyclerHeadersDecoration = new StickyRecyclerHeadersDecoration(hiscoreAdapter);
		recyclerView.addItemDecoration(stickyRecyclerHeadersDecoration);
		hiscoreAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
				stickyRecyclerHeadersDecoration.invalidateHeaders();
			}
		});
		errorView = view.findViewById(R.id.error_view);

		view.findViewById(R.id.share_btn).setOnClickListener(this);

		profileHeaderFragment.showProgressBar();
		asyncTask = new HiscoresFetcherTask(getContext(), this, account);
		asyncTask.execute();
		errorView.setVisibility(View.GONE);

		return view;
	}

	@Override
	public void refreshDataOnPreferencesChanged() {
		super.refreshDataOnPreferencesChanged();
		if(playerSkills != null) {
			populateTable();
		}
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.share_btn) {
			if(playerSkills != null) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST_CODE);
				} else {
					ShareImageUtils.shareHiscores(getContext(), account.username, playerSkills);
				}
			}
		}
	}

	@Override
	public void onItemClicked(int position) {
		Skill skill = rsView.getItem(position);
		RSViewDialog.showDialog(getContext(), skill);
	}

	@Override
	public void onItemLongClicked(int position) {

	}

	private void populateTable() {
		if(getActivity() != null) {
			getActivity().runOnUiThread(() -> {
				profileHeaderFragment.showCombatLvl(account.combatLvl);
				if(getView() != null) {
					getView().findViewById(R.id.share_btn).setVisibility(View.VISIBLE);
					rsView.populateViewForHiscores(playerSkills, this, false);
					hiscoreAdapter.setHiscoreItems(playerSkills.getHiscoresItems());
					hiscoreAdapter.notifyDataSetChanged();
				}
			});
		}
	}

	@Override
	public void onHiscoresCacheFetched(PlayerSkills playerSkills) {
		Log.d(TAG, "onHiscoresCacheFetched() called with: playerSkills = [" + playerSkills + "]");
		this.playerSkills = playerSkills;
		if (playerSkills != null) {
			populateTable();
		}
	}

	@Override
	public void onHiscoresFetched(PlayerSkills playerSkills) {
		Log.d(TAG, "onHiscoresFetched() called with: playerSkills = [" + playerSkills + "]");
		profileHeaderFragment.hideProgressBar();
		this.playerSkills = playerSkills;
		if (playerSkills != null) {
			populateTable();
		}
	}

	@Override
	public void onHiscoresError(String errorMessage) {
		if(errorView != null) {
			errorView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode == WRITE_PERMISSION_REQUEST_CODE) {
			for(int i=0 ; i < permissions.length; i++) {
				if(TextUtils.equals(permissions[i], Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
					if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
						ShareImageUtils.shareHiscores(getContext(), account.username, playerSkills);
					}
				}
			}
		}
	}
}
