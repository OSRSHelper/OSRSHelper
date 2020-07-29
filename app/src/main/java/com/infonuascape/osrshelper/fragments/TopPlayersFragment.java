package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.TopPlayersAdapter;
import com.infonuascape.osrshelper.controllers.MainFragmentController;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.Skill;

public class TopPlayersFragment extends OSRSFragment implements RecyclerItemClickListener, View.OnClickListener {
    private static final String TAG = "TopPlayersFragment";
    private TopPlayersAdapter adapter;
    private View ironmanBtn, hardcoreIronmanBtn, ultimateIronmanBtn;

    public static TopPlayersFragment newInstance() {
        TopPlayersFragment fragment = new TopPlayersFragment();
        Bundle b = new Bundle();
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.top_players, null);

        RecyclerView recyclerView = view.findViewById(R.id.list_rs_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new TopPlayersAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);

        ironmanBtn = view.findViewById(R.id.ironman);
        ironmanBtn.setOnClickListener(this);
        hardcoreIronmanBtn = view.findViewById(R.id.hc_ironman);
        hardcoreIronmanBtn.setOnClickListener(this);
        ultimateIronmanBtn = view.findViewById(R.id.ult_ironman);
        ultimateIronmanBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onItemClicked(int position) {
        Skill skill = adapter.getItem(position);
        MainFragmentController.getInstance().showFragment(TopPlayersSkillFragment.newInstance(getSelectedAccountType(), skill.getSkillType()));
    }

    @Override
    public void onItemLongClicked(int position) {
        //Ignore
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.ironman) {
            ultimateIronmanBtn.setSelected(false);
            hardcoreIronmanBtn.setSelected(false);
            v.setSelected(!v.isSelected());
        } else if (id == R.id.ult_ironman) {
            ironmanBtn.setSelected(false);
            hardcoreIronmanBtn.setSelected(false);
            v.setSelected(!v.isSelected());
        } else if (id == R.id.hc_ironman) {
            ironmanBtn.setSelected(false);
            ultimateIronmanBtn.setSelected(false);
            v.setSelected(!v.isSelected());
        }
    }

    private AccountType getSelectedAccountType() {
        if (ironmanBtn.isSelected())
            return AccountType.IRONMAN;
        else if (ultimateIronmanBtn.isSelected())
            return AccountType.ULTIMATE_IRONMAN;
        else if (hardcoreIronmanBtn.isSelected())
            return AccountType.HARDCORE_IRONMAN;
        else
            return AccountType.REGULAR;
    }
}
