package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.activities.MainActivity;
import com.infonuascape.osrshelper.adapters.DataPointsAdapter;
import com.infonuascape.osrshelper.listeners.DataPointsListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.Delta;
import com.infonuascape.osrshelper.tasks.DatapointsFetcherTask;
import com.infonuascape.osrshelper.utils.Logger;

import java.util.ArrayList;

/**
 * Created by marc_ on 2018-01-20.
 */

public class DataPointsFragment extends OSRSFragment implements DataPointsListener {
    private static final String TAG = "DataPointsFragment";
    private static final String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
    private Account account;
    private ProfileHeaderFragment profileHeaderFragment;
    private ArrayList<Delta> deltas;
    private DataPointsAdapter adapter;
    private RecyclerView recyclerView;

    public static DataPointsFragment newInstance(final Account account) {
        DataPointsFragment fragment = new DataPointsFragment();
        Bundle b = new Bundle();
        b.putSerializable(EXTRA_ACCOUNT, account);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.data_points, null);

        account = (Account) getArguments().getSerializable(EXTRA_ACCOUNT);

        if(account == null) {
            return view;
        }

        recyclerView = view.findViewById(R.id.profile_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profileHeaderFragment = (ProfileHeaderFragment) getChildFragmentManager().findFragmentById(R.id.profile_header);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshScreen();
    }

    private void refreshScreen() {
        if(getView() != null) {
            profileHeaderFragment.setTitle(R.string.data_points);
            profileHeaderFragment.refreshProfile(account);
            loadDeltas();
        }
    }

    private void loadDeltas() {
        Logger.add(TAG, ": loadDeltas");
        if(deltas == null) {
            profileHeaderFragment.showProgressBar();
            killAsyncTaskIfStillRunning();
            asyncTask = new DatapointsFetcherTask(this, account);
            asyncTask.execute();
        } else {
            adapter = new DataPointsAdapter(getContext(), deltas);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onDataPointsLoaded(ArrayList<Delta> deltas) {
        profileHeaderFragment.hideProgressBar();
        this.deltas = deltas;
        asyncTask = null;
        if(deltas != null) {
            if (deltas.size() > 0) {
                adapter = new DataPointsAdapter(getContext(), deltas);
                recyclerView.setAdapter(adapter);
            } else {
                ((TextView) getView().findViewById(R.id.profile_gains)).setText(R.string.profile_recent_gains_empty);
            }
        } else {
            getView().findViewById(R.id.profile_gains).setVisibility(View.GONE);
        }
    }
}
