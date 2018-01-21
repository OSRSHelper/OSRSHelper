package com.infonuascape.osrshelper;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.models.players.PlayerExp;
import com.infonuascape.osrshelper.top.TopFetcher;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;
import com.infonuascape.osrshelper.views.RSView;

import org.json.JSONException;

import java.util.List;

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

    private class TopFetcherNetwork extends AsyncTask<String, Void, List<PlayerExp>> {
        private Skill skill;

        public TopFetcherNetwork(Skill skill) {
            this.skill = skill;
        }

        @Override
        protected List<PlayerExp> doInBackground(String... urls) {
            try {
                TopFetcher tf = new TopFetcher(getApplicationContext(), SkillType.Agility, TopFetcher.Period.Day);
                return tf.processAPI();
            } catch (ParserErrorException e) {
                e.printStackTrace();
            } catch (APIError apiError) {
                apiError.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<PlayerExp> playerList) {
            if (playerList != null) {
                for (PlayerExp pe : playerList) {
                    Log.i(TAG, pe.playerName + " : " + pe.experience);
                }
            }
        }
    }
}