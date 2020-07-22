package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.CmlTopSkillFragmentAdapter;
import com.infonuascape.osrshelper.enums.SkillType;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CmlTopSkillFragment extends OSRSFragment implements ViewPager.OnPageChangeListener {
    private static final String TAG = "CmlTopSkillFragment";
    private final static String EXTRA_SKILLTYPE = "EXTRA_SKILLTYPE";

    private SkillType skillType;
    private CmlTopSkillFragmentAdapter adapter;
    private ViewPager viewPager;

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

        viewPager = view.findViewById(R.id.viewpager);
        adapter = new CmlTopSkillFragmentAdapter(getChildFragmentManager(), getContext(), skillType);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(4);
        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        tabLayout.setSelectedTabIndicatorColor(getContext().getResources().getColor(R.color.text_normal));
        tabLayout.setTabTextColors(getContext().getResources().getColor(R.color.text_light), getContext().getResources().getColor(R.color.text_normal));
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter.getItem(viewPager.getCurrentItem()).onPageVisible();
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