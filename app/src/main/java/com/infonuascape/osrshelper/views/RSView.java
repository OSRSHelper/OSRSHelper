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
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

/**
 * Created by marc-antoinehinse on 2018-01-13.
 */

public class RSView extends RelativeLayout implements RecyclerItemClickListener {
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

        populateTable(new PlayerSkills());
    }

    public void populateTable(final PlayerSkills playerSkills) {
        adapter = new RSViewAdapter(getContext(), playerSkills, this);
        recyclerView.setAdapter(adapter);
    }

    public void populateTable(final PlayerSkills playerSkills, final String username) {
        populateTable(playerSkills);

        usernameTextView.setText(username);
        combatLvlTextView.setText(getResources().getString(R.string.combat_lvl, Utils.getCombatLvl(playerSkills)));
        headerLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClicked(int position) {
        Skill skill = adapter.getItem(position);
        RSViewDialog.showDialog(getContext(), skill);
    }

    @Override
    public void onItemLongClicked(int position) {

    }
}
