package com.infonuascape.osrshelper.fragments;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.Period;
import com.infonuascape.osrshelper.enums.SkillType;

import java.util.ArrayList;

public class CmlTopSkillFragmentAdapter extends FragmentStatePagerAdapter{

    private Context context;
    private ArrayList<CmlTopSkillPeriodFragment> fragments;

    public CmlTopSkillFragmentAdapter(final FragmentManager fm, final Context context, final SkillType skillType) {
        super(fm);
        this.context = context;
        fragments = new ArrayList<>();
        fragments.add(CmlTopSkillPeriodFragment.newInstance(skillType, Period.Day));
        fragments.add(CmlTopSkillPeriodFragment.newInstance(skillType, Period.Week));
        fragments.add(CmlTopSkillPeriodFragment.newInstance(skillType, Period.Month));
        fragments.add(CmlTopSkillPeriodFragment.newInstance(skillType, Period.Year));
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CmlTopSkillPeriodFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int textResId;
        switch (position) {
            case 0:
                textResId = R.string.cml_top_day;
                break;
            case 1:
                textResId = R.string.cml_top_week;
                break;
            case 2:
                textResId = R.string.cml_top_month;
                break;
            default:
                textResId = R.string.cml_top_year;
                break;
        }

        return context.getResources().getString(textResId);
    }
}