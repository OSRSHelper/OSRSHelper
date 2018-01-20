package com.infonuascape.osrshelper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.models.Skill;

public class CmlTopSkillPeriodFragment extends Fragment {
    public static final String ARG_SKILL = "ARG_SKILL";
    public static final String ARG_POSITION = "ARG_POSITION";
    private int position;
    private SkillType skillType;

    //constructor, sort of
    public static CmlTopSkillPeriodFragment newInstance(SkillType skillType, int position) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SKILL, skillType);
        args.putInt(ARG_POSITION, position);
        CmlTopSkillPeriodFragment fragment = new CmlTopSkillPeriodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        skillType = (SkillType) getArguments().getSerializable(ARG_SKILL);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cml_top_skill_period, container, false);
        TextView textView = (TextView) view;
        textView.setText("Fragment #" + position + " for skill " + skillType.name());
        return view;
    }
}