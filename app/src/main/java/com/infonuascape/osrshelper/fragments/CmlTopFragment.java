package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.listeners.TopPlayersListener;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.models.players.PlayerExp;
import com.infonuascape.osrshelper.tasks.TopFetcherNetwork;
import com.infonuascape.osrshelper.views.RSView;

import java.util.List;

public class CmlTopFragment extends OSRSFragment implements RecyclerItemClickListener, TopPlayersListener {
    private static final String TAG = "CmlTopFragment";
    private RSView rsView;

    public static CmlTopFragment newInstance() {
        CmlTopFragment fragment = new CmlTopFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.cml_top, null);

        rsView = view.findViewById(R.id.rs_view);
        rsView.populateViewForCMLTop(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new TopFetcherNetwork(getContext(), this).execute();
    }

    @Override
    public void onItemClicked(int position) {
        Skill skill = rsView.getItem(position);
    }

    @Override
    public void onItemLongClicked(int position) {

    }

    @Override
    public void onPlayersFetched(List<PlayerExp> playerList) {
        if (playerList != null) {
            for (PlayerExp pe : playerList) {
                Log.i(TAG, pe.playerName + " : " + pe.experience);
            }
        }
    }
}
