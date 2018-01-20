package com.infonuascape.osrshelper;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.Toast;

import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.models.players.PlayerExp;
import com.infonuascape.osrshelper.top.TopFetcher;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;
import com.infonuascape.osrshelper.views.RSView;

import java.util.List;
public class CmlTopSkillFragmentAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "Day", "Week", "Month", "Year" };
    private Context context;
    private SkillType skillType;

    public CmlTopSkillFragmentAdapter(FragmentManager fm, Context context, SkillType skillType) {
        super(fm);
        this.context = context;
        this.skillType = skillType;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return CmlTopSkillPeriodFragment.newInstance(skillType, position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}