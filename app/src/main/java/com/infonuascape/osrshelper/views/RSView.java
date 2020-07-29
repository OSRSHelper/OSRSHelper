package com.infonuascape.osrshelper.views;

import android.content.Context;
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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by marc-antoinehinse on 2018-01-13.
 */

public class RSView extends RelativeLayout {
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private RSViewAdapter adapter;
    private View container;

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
        container = findViewById(R.id.container);
        recyclerView = findViewById(R.id.list_rs_view);
        headerLayout = findViewById(R.id.rs_view_header);
        usernameTextView = findViewById(R.id.username_rs_view);
        combatLvlTextView = findViewById(R.id.combat_lvl_rs_view);

        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        populateViewWithZeros(null);
    }

    public void populateViewForHiscores(final PlayerSkills playerSkills, final RecyclerItemClickListener listener) {
        populateViewForHiscores(playerSkills, listener, true);
    }

    public void populateViewForHiscores(final PlayerSkills playerSkills, final RecyclerItemClickListener listener, final boolean withBackground) {
        container.setBackgroundResource(withBackground ? R.drawable.rs_view_frame : 0);
        container.getLayoutParams().width = withBackground ? getResources().getDimensionPixelSize(R.dimen.rs_view_width) : LayoutParams.MATCH_PARENT;
        container.requestLayout();
        adapter = new RSViewAdapter(getContext(), playerSkills, listener, withBackground);
        recyclerView.setAdapter(adapter);
    }

    public void populateViewForImageShare(final PlayerSkills playerSkills, final String username, final RecyclerItemClickListener listener) {
        populateViewForHiscores(playerSkills, listener);

        usernameTextView.setText(username);
        combatLvlTextView.setText(getResources().getString(R.string.combat_lvl_for_share, Utils.getCombatLvl(playerSkills)));
        headerLayout.setVisibility(View.VISIBLE);
    }

    public void populateViewWithZeros(final RecyclerItemClickListener listener) {
        adapter = new RSViewAdapter(getContext(), new PlayerSkills(), listener, false);
        recyclerView.setAdapter(adapter);
    }

    public Skill getItem(int position) {
        if(adapter != null) {
            return adapter.getItem(position);
        }

        return null;
    }

    public void populateViewWithoutLevel() {
        adapter = new RSViewAdapter(getContext(), new PlayerSkills(), null, false, false);
        recyclerView.setAdapter(adapter);
    }
}
