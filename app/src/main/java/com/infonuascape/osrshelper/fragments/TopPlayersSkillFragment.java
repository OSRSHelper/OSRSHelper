package com.infonuascape.osrshelper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.TopPlayersSkillFragmentAdapter;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.enums.SkillType;

public class TopPlayersSkillFragment extends OSRSFragment implements ViewPager.OnPageChangeListener {
    private static final String TAG = "TopPlayersSkillFragment";
    private final static String EXTRA_ACCOUNT_TYPE = "EXTRA_ACCOUNT_TYPE";
    private final static String EXTRA_SKILLTYPE = "EXTRA_SKILLTYPE";

    private TopPlayersSkillFragmentAdapter adapter;
    private ViewPager viewPager;

    public static TopPlayersSkillFragment newInstance(AccountType accountType, SkillType skillType) {
        TopPlayersSkillFragment fragment = new TopPlayersSkillFragment();
        Bundle b = new Bundle();
        b.putInt(EXTRA_SKILLTYPE, skillType.ordinal());
        b.putInt(EXTRA_ACCOUNT_TYPE, accountType.ordinal());
        fragment.setArguments(b);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.top_players_skill, null);

        SkillType skillType = SkillType.values()[getArguments().getInt(EXTRA_SKILLTYPE, SkillType.Overall.ordinal())];
        AccountType accountType = AccountType.values()[getArguments().getInt(EXTRA_ACCOUNT_TYPE, AccountType.REGULAR.ordinal())];

        ((TextView) view.findViewById(R.id.title)).setText(getResources().getString(R.string.top_players_for, skillType.getSkillName()));
        ((TextView) view.findViewById(R.id.account_type)).setText(accountType.displayName);

        viewPager = view.findViewById(R.id.viewpager);
        adapter = new TopPlayersSkillFragmentAdapter(getChildFragmentManager(), getContext(), skillType, accountType);
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