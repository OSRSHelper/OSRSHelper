package com.infonuascape.osrshelper.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.CmlTopSkillPeriodAdapter;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.listeners.TopPlayersListener;
import com.infonuascape.osrshelper.models.players.PlayerExp;
import com.infonuascape.osrshelper.tasks.CmlTopFetcherTask;
import com.infonuascape.osrshelper.utils.Logger;

import java.util.List;

public class CmlTopSkillPeriodFragment extends OSRSPagerFragment implements TopPlayersListener {
    private static final String TAG = "CmlTopSkillPeriodFragme";

    public static final String ARG_SKILL = "ARG_SKILL";
    public static final String ARG_POSITION = "ARG_POSITION";
    private TrackerTime period;
    private SkillType skillType;
    private List<PlayerExp> playerExp;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private CmlTopSkillPeriodAdapter adapter;

    public static CmlTopSkillPeriodFragment newInstance(SkillType skillType, TrackerTime period) {
        Logger.add(TAG, ": newInstance");
        CmlTopSkillPeriodFragment fragment = new CmlTopSkillPeriodFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SKILL, skillType);
        args.putSerializable(ARG_POSITION, period);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.add(TAG, ": onCreateView: arguments=" + getArguments());
        View view = inflater.inflate(R.layout.cml_top_skill_period, null);

        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.cml_top_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onPageVisible() {
        if(skillType == null) {
            skillType = (SkillType) getArguments().getSerializable(ARG_SKILL);
            period = (TrackerTime) getArguments().getSerializable(ARG_POSITION);
        }
        Logger.add(TAG, ": onPageVisible: period=" + period.name());
        if(playerExp == null) {
            if(asyncTask== null) {
                asyncTask = new CmlTopFetcherTask(getContext(), this, skillType, period);
                asyncTask.execute();
                if(progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if(recyclerView.getAdapter() == null) {
                if(adapter == null) {
                    adapter = new CmlTopSkillPeriodAdapter(this, playerExp, period);
                }
                recyclerView.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onPlayersFetched(List<PlayerExp> playerList) {
        Logger.add(TAG, ": onPlayersFetched");
        asyncTask = null;
        this.playerExp = playerList;
        if(getView() != null) {
            progressBar.setVisibility(View.GONE);

            if (playerList != null) {
                adapter = new CmlTopSkillPeriodAdapter(this, playerList, period);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}