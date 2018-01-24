package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.fragments.CmlXPTrackerPeriodFragment;
import com.infonuascape.osrshelper.models.Account;

import java.util.ArrayList;

public class CmlXpTrackerFragmentAdapter extends FragmentStatePagerAdapter{

    private Context context;
    private ArrayList<CmlXPTrackerPeriodFragment> fragments;

    public CmlXpTrackerFragmentAdapter(final FragmentManager fm, final Context context, final Account account) {
        super(fm);
        this.context = context;
        fragments = new ArrayList<>();
        fragments.add(CmlXPTrackerPeriodFragment.newInstance(account, TrackerTime.Hour));
        fragments.add(CmlXPTrackerPeriodFragment.newInstance(account, TrackerTime.Day));
        fragments.add(CmlXPTrackerPeriodFragment.newInstance(account, TrackerTime.Week));
        fragments.add(CmlXPTrackerPeriodFragment.newInstance(account, TrackerTime.Month));
        fragments.add(CmlXPTrackerPeriodFragment.newInstance(account, TrackerTime.Year));
        fragments.add(CmlXPTrackerPeriodFragment.newInstance(account, TrackerTime.All));
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CmlXPTrackerPeriodFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int textResId;
        switch (position) {
            case 0:
                textResId = R.string.hour;
                break;
            case 1:
                textResId = R.string.day;
                break;
            case 2:
                textResId = R.string.week;
                break;
            case 3:
                textResId = R.string.month;
                break;
            case 4:
                textResId = R.string.year;
                break;
            default:
                textResId = R.string.all;
                break;
        }

        return context.getResources().getString(textResId);
    }
}