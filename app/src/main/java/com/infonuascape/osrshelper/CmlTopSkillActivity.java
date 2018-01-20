package com.infonuascape.osrshelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.views.RSView;

public class CmlTopSkillActivity extends AppCompatActivity {
    private static final String TAG = "CmlTopSkillActivity";
    private final static String EXTRA_SKILLTYPE = "EXTRA_SKILLTYPE";

    private SkillType skillType;

    public static void show(final Context context, SkillType skillType) {
        Intent i = new Intent(context, CmlTopSkillActivity.class);
        i.putExtra(EXTRA_SKILLTYPE, skillType);
        context.startActivity(i);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cml_top_skill);

        skillType = (SkillType) getIntent().getSerializableExtra(EXTRA_SKILLTYPE);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CmlTopSkillFragmentAdapter(getSupportFragmentManager(),
                CmlTopSkillActivity.this, skillType));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}