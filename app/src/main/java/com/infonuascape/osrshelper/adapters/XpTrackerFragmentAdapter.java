package com.infonuascape.osrshelper.adapters;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.fragments.OSRSPagerFragment;
import com.infonuascape.osrshelper.fragments.XPTrackerPeriodFragment;

public class XpTrackerFragmentAdapter extends OSRSNestedViewPagerAdapter {

    public XpTrackerFragmentAdapter(final FragmentManager fm, final Context context) {
        super(fm, context);
    }

    @Override
    public int getCount() {
        return TrackerTime.values().length;
    }

    @Override
    public OSRSPagerFragment createFragment(int position) {
        return XPTrackerPeriodFragment.newInstance(TrackerTime.values()[position]);
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
            default:
                return R.string.year;
        }
    }
}