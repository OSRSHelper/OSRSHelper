package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.fragments.CmlXPTrackerPeriodFragment;
import com.infonuascape.osrshelper.fragments.OSRSPagerFragment;
import com.infonuascape.osrshelper.models.Account;

public class CmlXpTrackerFragmentAdapter extends OSRSNestedViewPagerAdapter {

    private Account account;

    public CmlXpTrackerFragmentAdapter(final FragmentManager fm, final Context context, final Account account) {
        super(fm, context);
        this.account = account;
    }

    @Override
    public int getCount() {
        return TrackerTime.values().length;
    }

    @Override
    public OSRSPagerFragment createFragment(int position) {
        return CmlXPTrackerPeriodFragment.newInstance(account, TrackerTime.values()[position]);
    }

    @Override
    public int getTitle(int position) {
        switch (position) {
            case 0:
                return R.string.day;
            case 1:
                return R.string.week;
            case 2:
                return R.string.month;
            case 3:
                return R.string.year;
            default:
                return R.string.all;
        }
    }
}