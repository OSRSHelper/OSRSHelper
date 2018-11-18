package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.CMLTopAdapter;
import com.infonuascape.osrshelper.controllers.MainFragmentController;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.Skill;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CmlTopFragment extends OSRSFragment implements RecyclerItemClickListener {
    private static final String TAG = "CmlTopFragment";
    private static final String FRAGMENT_TAG = "CML_TOP_FRAGMENT";
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CMLTopAdapter adapter;

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

        recyclerView = view.findViewById(R.id.list_rs_view);

        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CMLTopAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClicked(int position) {
        Skill skill = adapter.getItem(position);
        MainFragmentController.getInstance().showFragment(CmlTopSkillFragment.newInstance(skill.getSkillType()));
    }

    @Override
    public void onItemLongClicked(int position) {
        //Ignore
    }
}
