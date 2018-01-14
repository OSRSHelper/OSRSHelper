package com.infonuascape.osrshelper.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

/**
 * Created by marc-antoinehinse on 2018-01-13.
 */

public class RSView extends RelativeLayout {
    private TableLayout tableLayout;
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
        tableLayout = findViewById(R.id.table_rs_view);
        headerLayout = findViewById(R.id.rs_view_header);
        usernameTextView = findViewById(R.id.username_rs_view);
        combatLvlTextView = findViewById(R.id.combat_lvl_rs_view);

        populateTable(new PlayerSkills());
    }

    public void populateTable(final PlayerSkills playerSkills) {
        RSViewPopulate rsViewPopulate = new RSViewPopulate(getContext(), playerSkills);
        tableLayout = rsViewPopulate.populate(tableLayout);
    }

    public void populateTable(final PlayerSkills playerSkills, final String username) {
        RSViewPopulate rsViewPopulate = new RSViewPopulate(getContext(), playerSkills);
        tableLayout = rsViewPopulate.populate(tableLayout);

        usernameTextView.setText(username);
        combatLvlTextView.setText(getResources().getString(R.string.combat_lvl, Utils.getCombatLvl(playerSkills)));
        headerLayout.setVisibility(View.VISIBLE);
    }
}
