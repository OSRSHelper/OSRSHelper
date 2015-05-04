package com.infonuascape.osrshelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.SkillsEnum.SkillType;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

public class CombatCalcActivity extends Activity implements TextWatcher {
	private TextView combatText;
	private EditText hitpointEdit;
	private EditText attackEdit;
	private EditText strengthEdit;
	private EditText defenceEdit;
	private EditText magicEdit;
	private EditText rangingEdit;
	private EditText prayerEdit;

	public static void show(final Context context) {
		Intent intent = new Intent(context, CombatCalcActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.combat_lvl_calc);
		
		combatText = (TextView) findViewById(R.id.combat_lvl);

		hitpointEdit = ((EditText) findViewById(R.id.edit_hitpoints));
		hitpointEdit.addTextChangedListener(this);
		
		attackEdit = ((EditText) findViewById(R.id.edit_attack));
		attackEdit.addTextChangedListener(this);
		
		strengthEdit = ((EditText) findViewById(R.id.edit_strength));
		strengthEdit.addTextChangedListener(this);
		
		defenceEdit = ((EditText) findViewById(R.id.edit_defence));
		defenceEdit.addTextChangedListener(this);
		
		rangingEdit = ((EditText) findViewById(R.id.edit_ranging));
		rangingEdit.addTextChangedListener(this);
		
		prayerEdit = ((EditText) findViewById(R.id.edit_prayer));
		prayerEdit.addTextChangedListener(this);
		
		magicEdit = ((EditText) findViewById(R.id.edit_magic));
		magicEdit.addTextChangedListener(this);


	}
	
	private void changeCombatText(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				PlayerSkills playerSkills = new PlayerSkills();
				String hitpoint = hitpointEdit.getText().toString();
				String attack = attackEdit.getText().toString();
				String strength = strengthEdit.getText().toString();
				String defence = defenceEdit.getText().toString();
				String ranging = rangingEdit.getText().toString();
				String prayer = prayerEdit.getText().toString();
				String magic = magicEdit.getText().toString();
				
				playerSkills.hitpoints = hitpoint.isEmpty() ? new Skill(SkillType.Hitpoints, 0, (short)0) : new Skill(SkillType.Hitpoints, 0, Short.valueOf(hitpoint));
				playerSkills.attack = attack.isEmpty() ? new Skill(SkillType.Attack, 0, (short)0) : new Skill(SkillType.Attack, 0, Short.valueOf(attack));
				playerSkills.defence = defence.isEmpty() ? new Skill(SkillType.Defence, 0, (short)0) : new Skill(SkillType.Defence, 0, Short.valueOf(defence));
				playerSkills.strength = strength.isEmpty() ? new Skill(SkillType.Strength, 0, (short)0) : new Skill(SkillType.Strength, 0, Short.valueOf(strength));
				playerSkills.ranged = ranging.isEmpty() ? new Skill(SkillType.Ranged, 0, (short)0) : new Skill(SkillType.Ranged, 0, Short.valueOf(ranging));
				playerSkills.prayer = prayer.isEmpty() ? new Skill(SkillType.Prayer, 0, (short)0) : new Skill(SkillType.Prayer, 0, Short.valueOf(prayer));
				playerSkills.magic = magic.isEmpty() ? new Skill(SkillType.Magic, 0, (short)0) : new Skill(SkillType.Magic, 0, Short.valueOf(magic));
				
				combatText.setText(getString(R.string.combat_lvl, Utils.getCombatLvl(playerSkills)));
			}
		});
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		changeCombatText();
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
}
