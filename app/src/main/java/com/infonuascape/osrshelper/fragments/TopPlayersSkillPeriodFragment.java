package com.infonuascape.osrshelper.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.TopPlayersSkillPeriodAdapter;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.listeners.TopPlayersListener;
import com.infonuascape.osrshelper.models.players.PlayerExp;
import com.infonuascape.osrshelper.tasks.TopPlayersFetcherTask;
import com.infonuascape.osrshelper.utils.Logger;

import java.util.List;

public class TopPlayersSkillPeriodFragment extends OSRSPagerFragment implements TopPlayersListener {
    private static final String TAG = "TopPlayersSkillPeriodFragment";

    public static final String EXTRA_SKILL = "EXTRA_SKILL";
    public static final String EXTRA_PERIOD = "EXTRA_PERIOD";
    public static final String EXTRA_ACCOUNT_TYPE = "EXTRA_ACCOUNT_TYPE";
    private TrackerTime period;
    private SkillType skillType;
    private AccountType accountType;
    private List<PlayerExp> playerExp;
    private ProgressBar progressBar;
    private View emptyView;

    private RecyclerView recyclerView;
    private TopPlayersSkillPeriodAdapter adapter;

    public static TopPlayersSkillPeriodFragment newInstance(SkillType skillType, AccountType accountType, TrackerTime period) {
        Logger.add(TAG, ": newInstance");
        TopPlayersSkillPeriodFragment fragment = new TopPlayersSkillPeriodFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_SKILL, skillType.ordinal());
        args.putInt(EXTRA_PERIOD, period.ordinal());
        args.putInt(EXTRA_ACCOUNT_TYPE, accountType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.add(TAG, ": onCreateView: arguments=" + getArguments());
        View view = inflater.inflate(R.layout.top_players_skill_period, null);

        progressBar = view.findViewById(R.id.progress_bar);
        emptyView = view.findViewById(R.id.empty_view);
        recyclerView = view.findViewById(R.id.top_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onPageVisible() {
        if (skillType == null) {
            skillType = SkillType.values()[getArguments().getInt(EXTRA_SKILL, SkillType.Overall.ordinal())];
            period = TrackerTime.values()[getArguments().getInt(EXTRA_PERIOD, TrackerTime.Day.ordinal())];
            accountType = AccountType.values()[getArguments().getInt(EXTRA_ACCOUNT_TYPE, AccountType.REGULAR.ordinal())];
        }
        Logger.add(TAG, ": onPageVisible: period=" + period.name());
        if (playerExp == null) {
            if (asyncTask == null) {
                asyncTask = new TopPlayersFetcherTask(this, skillType, period, accountType);
                asyncTask.execute();
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                if (emptyView != null) {
                    emptyView.setVisibility(View.GONE);
                }
            }
        } else {
            if (recyclerView != null && recyclerView.getAdapter() == null) {
                if (adapter == null) {
                    adapter = new TopPlayersSkillPeriodAdapter(this, playerExp, period);
                }
                recyclerView.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onPlayersFetched(List<PlayerExp> playerList) {
        Logger.add(TAG, ": onPlayersFetched: playerList=", playerList);
        asyncTask = null;
        this.playerExp = playerList;
        if (getView() != null) {
            progressBar.setVisibility(View.GONE);

            if (playerList != null) {
                adapter = new TopPlayersSkillPeriodAdapter(this, playerList, period);
                recyclerView.setAdapter(adapter);
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }
}