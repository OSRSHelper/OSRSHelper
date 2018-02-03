package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.GrandExchangePeriods;
import com.infonuascape.osrshelper.fragments.GrandExchangePeriodFragment;
import com.infonuascape.osrshelper.fragments.OSRSFragment;
import com.infonuascape.osrshelper.fragments.OSRSPagerFragment;

public class GrandExchangeDetailFragmentAdapter extends OSRSNestedViewPagerAdapter{

    public GrandExchangeDetailFragmentAdapter(final FragmentManager fm, final Context context) {
        super(fm, context);
    }

    @Override
    public int getCount() {
        return GrandExchangePeriods.values().length;
    }

    @Override
    public OSRSPagerFragment createFragment(int position) {
        return GrandExchangePeriodFragment.newInstance(GrandExchangePeriods.values()[position]);
    }

    @Override
    public int getTitle(int position) {
        switch (position) {
            case 0:
                return R.string.week;
            case 1:
                return R.string.month;
            case 2:
                return R.string.three_months;
            default:
                return R.string.six_months;

        }
    }
}