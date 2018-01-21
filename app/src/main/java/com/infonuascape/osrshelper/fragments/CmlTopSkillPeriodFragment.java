package com.infonuascape.osrshelper.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.Period;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.listeners.TopPlayersListener;
import com.infonuascape.osrshelper.models.players.PlayerExp;
import com.infonuascape.osrshelper.tasks.CMLTopFetcherTask;

import java.util.List;

public class CmlTopSkillPeriodFragment extends Fragment implements TopPlayersListener {
    private static final String TAG = "CmlTopSkillPeriodFragme";

    public static final String ARG_SKILL = "ARG_SKILL";
    public static final String ARG_POSITION = "ARG_POSITION";
    private Period period;
    private SkillType skillType;
    private List<PlayerExp> playerExp;
    private CMLTopFetcherTask fetcherTask;

    //constructor, sort of
    public static CmlTopSkillPeriodFragment newInstance(SkillType skillType, Period period) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SKILL, skillType);
        args.putSerializable(ARG_POSITION, period);
        CmlTopSkillPeriodFragment fragment = new CmlTopSkillPeriodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        skillType = (SkillType) getArguments().getSerializable(ARG_SKILL);
        period = (Period) getArguments().getSerializable(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cml_top_skill_period, container, false);
        TextView textView = (TextView) view;
        textView.setText("Fragment #" + period.name() + " for skill " + skillType.name());
        return view;
    }

    public void onPageVisible() {
        Log.i(TAG, "onPageVisible");
        if(playerExp == null && fetcherTask == null) {
            fetcherTask = new CMLTopFetcherTask(getContext(), this, skillType, period);
            fetcherTask.execute();
        }
    }

    @Override
    public void onPlayersFetched(List<PlayerExp> playerList) {
        Log.i(TAG, "onPlayersFetched");
        this.playerExp = playerList;
        fetcherTask = null;

        //ToDo fill table
    }
}