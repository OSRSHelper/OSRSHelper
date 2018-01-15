package com.infonuascape.osrshelper.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.RSViewAdapter;
import com.infonuascape.osrshelper.listeners.RecyclerItemClickListener;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.models.players.PlayerSkills;

/**
 * Created by marc-antoinehinse on 2018-01-13.
 */

public class RSView extends RelativeLayout {
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private RSViewAdapter adapter;

    private View headerLayout;
    private TextView usernameTextView;
    private TextView combatLvlTextView;

    public RSView(Context context) {
        super(context);
        init();
    }

    public RSView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RSView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RSView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.rs_view, this);
        recyclerView = findViewById(R.id.list_rs_view);
        headerLayout = findViewById(R.id.rs_view_header);
        usernameTextView = findViewById(R.id.username_rs_view);
        combatLvlTextView = findViewById(R.id.combat_lvl_rs_view);

        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        populateViewForCMLTop(null);
    }

    public void populateView(final PlayerSkills playerSkills, final RecyclerItemClickListener listener) {
        adapter = new RSViewAdapter(getContext(), playerSkills, listener);
        recyclerView.setAdapter(adapter);
    }

    public void populateViewForImageShare(final PlayerSkills playerSkills, final String username, final RecyclerItemClickListener listener) {
        populateView(playerSkills, listener);

        usernameTextView.setText(username);
        combatLvlTextView.setText(getResources().getString(R.string.combat_lvl, Utils.getCombatLvl(playerSkills)));
        headerLayout.setVisibility(View.VISIBLE);
    }

    public void populateViewForCMLTop(final RecyclerItemClickListener listener) {
        adapter = new RSViewAdapter(getContext(), new PlayerSkills(), listener, false);
        recyclerView.setAdapter(adapter);
    }

    public Skill getItem(int position) {
        if(adapter != null) {
            return adapter.getItem(position);
        }

        return null;
    }
}
