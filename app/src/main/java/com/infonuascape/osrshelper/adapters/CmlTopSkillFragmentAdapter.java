package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import androidx.fragment.app.FragmentManager;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.fragments.CmlTopSkillPeriodFragment;
import com.infonuascape.osrshelper.fragments.OSRSPagerFragment;

public class CmlTopSkillFragmentAdapter extends OSRSNestedViewPagerAdapter {
    private SkillType skillType;

    public CmlTopSkillFragmentAdapter(final FragmentManager fm, final Context context, final SkillType skillType) {
        super(fm, context);
        this.skillType = skillType;
    }

    @Override
    public int getCount() {
        return TrackerTime.values().length -1;
    }

    @Override
    public OSRSPagerFragment createFragment(int position) {
        return CmlTopSkillPeriodFragment.newInstance(skillType, TrackerTime.values()[position]);
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
            default:
                return R.string.year;
        }
    }
}