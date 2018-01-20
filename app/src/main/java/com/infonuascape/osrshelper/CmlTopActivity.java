package com.infonuascape.osrshelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.Toast;

import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.listeners.HiscoresFetcherListener;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.top.TopFetcher;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;
import com.infonuascape.osrshelper.models.players.PlayerExp;
import com.infonuascape.osrshelper.views.RSView;
import com.infonuascape.osrshelper.views.RSViewDialog;

import java.util.List;

public class CmlTopActivity extends Activity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, RecyclerItemClickListener {
    private static final String TAG = "CmlTopActivity";
    private RSView rsView;

    public static void show(final Context context) {
        Intent i = new Intent(context, CmlTopActivity.class);
        context.startActivity(i);
    }

    @Override
    public void onItemClicked(int position) {
        Skill skill = rsView.getItem(position);
        CmlTopSkillActivity.show(this, skill.getSkillType());
    }

    @Override
    public void onItemLongClicked(int position) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cml_top);

        rsView = findViewById(R.id.rs_view);
        rsView.populateViewForCMLTop(this);


        new TopFetcherNetwork().execute();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    private class TopFetcherNetwork extends AsyncTask<String, Void, List<PlayerExp>> {

        @Override
        protected List<PlayerExp> doInBackground(String... urls) {
            try {
                TopFetcher tf = new TopFetcher(getApplicationContext(), SkillType.Agility, TopFetcher.Period.Day);
                return tf.processAPI();
            } catch (ParserErrorException e) {
                e.printStackTrace();
            } catch (APIError apiError) {
                apiError.printStackTrace();
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
