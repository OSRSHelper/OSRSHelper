package com.infonuascape.osrshelper.adapters;

import android.content.Context;
import androidx.fragment.app.FragmentManager;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.enums.TrackerTime;
import com.infonuascape.osrshelper.fragments.TopPlayersSkillPeriodFragment;
import com.infonuascape.osrshelper.fragments.OSRSPagerFragment;

public class TopPlayersSkillFragmentAdapter extends OSRSNestedViewPagerAdapter {
    private SkillType skillType;
    private AccountType accountType;

    public TopPlayersSkillFragmentAdapter(final FragmentManager fm, final Context context, final SkillType skillType, final AccountType accountType) {
        super(fm, context);
        this.skillType = skillType;
        this.accountType = accountType;
    }

    @Override
    public int getCount() {
        return TrackerTime.values().length;
    }

    @Override
    public OSRSPagerFragment createFragment(int position) {
        return TopPlayersSkillPeriodFragment.newInstance(skillType, accountType, TrackerTime.values()[position]);
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