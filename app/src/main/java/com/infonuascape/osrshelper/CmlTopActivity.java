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
import android.widget.TableLayout;
import android.widget.Toast;

import com.infonuascape.osrshelper.top.TopFetcher;
import com.infonuascape.osrshelper.utils.SkillsEnum;
import com.infonuascape.osrshelper.utils.exceptions.APIError;
import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;
import com.infonuascape.osrshelper.utils.players.PlayerExp;

import java.util.List;

public class CmlTopActivity extends Activity implements OnClickListener {
    private static final String TAG = "CmlTopActivity";
    private TableLayout rsView;

    public static void show(final Context context) {
        Intent i = new Intent(context, CmlTopActivity.class);
        context.startActivity(i);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cml_top);
        new TopFetcherNetwork().execute();
    }

    private class TopFetcherNetwork extends AsyncTask<String, Void, List<PlayerExp>> {

        @Override
        protected List<PlayerExp> doInBackground(String... urls) {
            try {
                TopFetcher tf = new TopFetcher(getApplicationContext(), SkillsEnum.SkillType.Agility, TopFetcher.Period.Day);
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
