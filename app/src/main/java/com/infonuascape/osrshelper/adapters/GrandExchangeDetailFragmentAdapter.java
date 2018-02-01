package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.GrandExchangePeriods;
import com.infonuascape.osrshelper.fragments.GrandExchangePeriodFragment;

import java.util.ArrayList;

public class GrandExchangeDetailFragmentAdapter extends FragmentStatePagerAdapter{

    private Context context;
    private ArrayList<GrandExchangePeriodFragment> fragments;

    public GrandExchangeDetailFragmentAdapter(final FragmentManager fm, final Context context) {
        super(fm);
        this.context = context;
        fragments = new ArrayList<>();
        fragments.add(GrandExchangePeriodFragment.newInstance(GrandExchangePeriods.WEEK));
        fragments.add(GrandExchangePeriodFragment.newInstance(GrandExchangePeriods.MONTH));
        fragments.add(GrandExchangePeriodFragment.newInstance(GrandExchangePeriods.THREE_MONTHS));
        fragments.add(GrandExchangePeriodFragment.newInstance(GrandExchangePeriods.SIX_MONTHS));
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public GrandExchangePeriodFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int textResId;
        switch (position) {
            case 0:
                textResId = R.string.week;
                break;
            case 1:
                textResId = R.string.month;
                break;
            case 2:
                textResId = R.string.three_months;
                break;
            default:
                textResId = R.string.six_months;
                break;
        }

        return context.getResources().getString(textResId);
    }
}