package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.SkillType;

public class CmlTopSkillFragment extends OSRSFragment implements ViewPager.OnPageChangeListener {
    private static final String TAG = "CmlTopSkillFragment";
    private final static String EXTRA_SKILLTYPE = "EXTRA_SKILLTYPE";

    private SkillType skillType;
    private CmlTopSkillFragmentAdapter adapter;

    public static CmlTopSkillFragment newInstance(SkillType skillType) {
        CmlTopSkillFragment fragment = new CmlTopSkillFragment();
        Bundle b = new Bundle();
        b.putSerializable(EXTRA_SKILLTYPE, skillType);
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.cml_top_skill, null);

        skillType = (SkillType) getArguments().getSerializable(EXTRA_SKILLTYPE);

        ((TextView) view.findViewById(R.id.title)).setText(getResources().getString(R.string.top_players_for_cml, skillType.getSkillName()));

        ViewPager viewPager = view.findViewById(R.id.viewpager);
        adapter = new CmlTopSkillFragmentAdapter(getChildFragmentManager(), getContext(), skillType);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(3);
        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public boolean onBackPressed() {
        getParentFragment().getChildFragmentManager().beginTransaction().remove(this).commit();
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        adapter.getItem(position).onPageVisible();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}