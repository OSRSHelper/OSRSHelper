package com.infonuascape.osrshelper.bubble;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.HiscoreAdapter;
import com.infonuascape.osrshelper.listeners.HiscoresFetcherListener;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.tasks.HiscoresFetcherTask;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.views.RSView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.zip.Inflater;

import io.mattcarroll.hover.Content;

public class HoverHiscoreSection implements Content, View.OnClickListener, HiscoresFetcherListener, RecyclerItemClickListener, TextView.OnEditorActionListener {
    private static final String TAG = "HoverWikiSection";

    private Context context;
    private View container;
    private HiscoreAdapter hiscoreAdapter;
    private RecyclerView recyclerView;
    private RSView rsView;
    private TextView errorView;
    private EditText usernameView;
    private HiscoresFetcherTask asyncTask;

    private PlayerSkills playerSkills;

    public HoverHiscoreSection(final Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public View getView() {
        if (container == null) {
            container = LayoutInflater.from(context).inflate(R.layout.bubble_hiscores, null);

            rsView = container.findViewById(R.id.rs_view);
            errorView = container.findViewById(R.id.error_view);
            usernameView = container.findViewById(R.id.username_edit);
            usernameView.setOnEditorActionListener(this);

            recyclerView = container.findViewById(R.id.hiscore_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setNestedScrollingEnabled(false);
            hiscoreAdapter = new HiscoreAdapter(context);
            recyclerView.setAdapter(hiscoreAdapter);
            final StickyRecyclerHeadersDecoration stickyRecyclerHeadersDecoration = new StickyRecyclerHeadersDecoration(hiscoreAdapter);
            recyclerView.addItemDecoration(stickyRecyclerHeadersDecoration);
            hiscoreAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    stickyRecyclerHeadersDecoration.invalidateHeaders();
                }
            });

            container.findViewById(R.id.continue_btn).setOnClickListener(this);
        }
        return container;
    }

    @Override
    public boolean isFullscreen() {
        return true;
    }

    @Override
    public void onShown() {
        Logger.add(TAG, ": onShown");
    }

    @Override
    public void onHidden() {
        Logger.add(TAG, ": onHidden");
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.continue_btn) {
            loadHiscore();
        }
    }

    private void loadHiscore() {
        Logger.add(TAG, ": loadHiscore");
        if (!TextUtils.isEmpty(usernameView.getText())) {
            Account account = new Account(usernameView.getText().toString());
            asyncTask = new HiscoresFetcherTask(context, this, account);
            asyncTask.execute();
            errorView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onHiscoresCacheFetched(PlayerSkills playerSkills) {
        Logger.add(TAG, ": onHiscoresCacheFetched: playerSkills=", playerSkills);
        this.playerSkills = playerSkills;
        rsView.populateViewForHiscores(playerSkills, this, false);
        hiscoreAdapter.setHiscoreItems(playerSkills.getHiscoresItems());
        hiscoreAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHiscoresFetched(PlayerSkills playerSkills) {
        Logger.add(TAG, ": onHiscoresFetched: playerSkills=", playerSkills);
        this.playerSkills = playerSkills;
        rsView.populateViewForHiscores(playerSkills, this, false);
        hiscoreAdapter.setHiscoreItems(playerSkills.getHiscoresItems());
        hiscoreAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHiscoresError(String errorMessage) {
        Logger.add(TAG, ": onHiscoresError: errorMessage=", errorMessage);
        if (errorView != null) {
            errorView.setText(errorMessage);
            errorView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClicked(int position) {
        Logger.add(TAG, ": onItemClicked: position=", position);
    }

    @Override
    public void onItemLongClicked(int position) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            loadHiscore();
            return true;
        }
        return false;
    }
}
