package com.infonuascape.osrshelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.infonuascape.osrshelper.hiscore.HiscoreHelper;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.SkillsEnum.SkillType;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

public class CombatCalcActivity extends Activity implements TextWatcher {
	private static final String EXTRA_USERNAME = "EXTRA_USERNAME";

	private TextView combatText;
	private TextView lvlNeededText;
	private TextView attStrText;
	private TextView hpDefText;
	private TextView rangeText;
	private TextView mageText;
	private TextView prayerText;
	private EditText hitpointEdit;
	private EditText attackEdit;
	private EditText strengthEdit;
	private EditText defenceEdit;
	private EditText magicEdit;
	private EditText rangingEdit;
	private EditText prayerEdit;

	private String username;
	
	public static void show(final Context context, final String username) {
		Intent intent = new Intent(context, CombatCalcActivity.class);
		intent.putExtra(EXTRA_USERNAME, username);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.combat_lvl_calc);
		
		combatText = (TextView) findViewById(R.id.combat_lvl);
		lvlNeededText = (TextView) findViewById(R.id.number_lvl_needed);
		attStrText = (TextView) findViewById(R.id.attack_strength_needed);
		hpDefText = (TextView) findViewById(R.id.hitpoint_defence_needed);
		rangeText = (TextView) findViewById(R.id.ranging_needed);
		mageText = (TextView) findViewById(R.id.magic_needed);
		prayerText = (TextView) findViewById(R.id.prayer_needed);

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

		changeCombatText();
		username = getIntent().getStringExtra(EXTRA_USERNAME);
		if(username != null){
			new PopulateTable().execute();
		}
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
				
				boolean isOneShown = false;
				combatText.setText(getString(R.string.combat_lvl, Utils.getCombatLvl(playerSkills)));
				
				int missingAttStr = Utils.getMissingAttackStrengthUntilNextCombatLvl(playerSkills);
				if(playerSkills.attack.getLevel() + playerSkills.strength.getLevel() + missingAttStr < 199) {
					attStrText.setText(getString(R.string.attack_strength_lvl_needed, missingAttStr));
					attStrText.setVisibility(View.VISIBLE);
					isOneShown = true;
				} else {
					attStrText.setVisibility(View.GONE);
				}
				
				int missingHpDef = Utils.getMissingHPDefenceUntilNextCombatLvl(playerSkills);
				
				if(playerSkills.hitpoints.getLevel() + playerSkills.defence.getLevel() + missingHpDef < 199) {
					hpDefText.setText(getString(R.string.hitpoint_defence_lvl_needed, missingHpDef));
					hpDefText.setVisibility(View.VISIBLE);
					isOneShown = true;
				} else {
					hpDefText.setVisibility(View.GONE);
				}
				
				int missingMage = Utils.getMissingMagicUntilNextCombatLvl(playerSkills);
				if(playerSkills.magic.getLevel() + missingMage <= 99) {
					mageText.setText(getString(R.string.magic_lvl_needed, missingMage));
					mageText.setVisibility(View.VISIBLE);
					isOneShown = true;
				} else {
					mageText.setVisibility(View.GONE);
				}
				
				int missingRanged = Utils.getMissingRangingUntilNextCombatLvl(playerSkills);
				if(playerSkills.ranged.getLevel() + missingRanged <= 99) {
					rangeText.setText(getString(R.string.ranging_lvl_needed, missingRanged));
					rangeText.setVisibility(View.VISIBLE);
					isOneShown = true;
				} else {
					rangeText.setVisibility(View.GONE);
				}
				
				int missingPrayer = Utils.getMissingPrayerUntilNextCombatLvl(playerSkills);
				if(playerSkills.prayer.getLevel() + missingPrayer <= 99) {
					prayerText.setText(getString(R.string.prayer_lvl_needed, missingPrayer));
					prayerText.setVisibility(View.VISIBLE);
					isOneShown = true;
				} else {
					prayerText.setVisibility(View.GONE);
				}
				
				if(!isOneShown) {
					lvlNeededText.setText(R.string.maxed_out);
				} else {
					lvlNeededText.setText(R.string.lvl_need);
				}
			}
		});
	}
	
	private class PopulateTable extends AsyncTask<String, Void, PlayerSkills> {

		@Override
		protected PlayerSkills doInBackground(String... urls) {
			HiscoreHelper hiscoreHelper = new HiscoreHelper();
			hiscoreHelper.setUserName(username);
			PlayerSkills playerSkills = null;

			try {
				playerSkills = hiscoreHelper.getPlayerStats();
			} catch (PlayerNotFoundException e) {
				e.printStackTrace();
				changeHint(getString(R.string.not_existing_player, username));
			} catch (Exception uhe) {
				uhe.printStackTrace();
				changeHint(getString(R.string.network_error));
			}
			return playerSkills;
		}

		@Override
		protected void onPostExecute(final PlayerSkills playerSkillsCallback) {
			if (playerSkillsCallback != null) {
				hitpointEdit.setText(playerSkillsCallback.hitpoints.getLevel()+"");
				attackEdit.setText(playerSkillsCallback.attack.getLevel()+"");
				strengthEdit.setText(playerSkillsCallback.strength.getLevel()+"");
				defenceEdit.setText(playerSkillsCallback.defence.getLevel()+"");
				magicEdit.setText(playerSkillsCallback.magic.getLevel()+"");
				rangingEdit.setText(playerSkillsCallback.ranged.getLevel()+"");
				prayerEdit.setText(playerSkillsCallback.prayer.getLevel()+"");
				changeCombatText();
			}
		}
	}

	private void changeHint(final String text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				combatText.setText(text);
			}
		});
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		changeCombatText();
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
}
