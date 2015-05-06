package com.infonuascape.osrshelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.infonuascape.osrshelper.hiscore.HiscoreHelper;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.SkillsEnum.SkillType;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;

public class CombatCalcActivity extends Activity implements TextWatcher, OnClickListener {
	private TextView combatText;
	private EditText hitpointEdit;
	private EditText attackEdit;
	private EditText strengthEdit;
	private EditText defenceEdit;
	private EditText magicEdit;
	private EditText rangingEdit;
	private EditText prayerEdit;
	private EditText usernameEdit;
	private String username;

	public static void show(final Context context) {
		Intent intent = new Intent(context, CombatCalcActivity.class);
		context.startActivity(intent);
	}
	
	public static void showWithPrefillUSername(final Context context, final String username) {
		Intent intent = new Intent(context, CombatCalcActivity.class);
		intent.putExtra("username", username);
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
		
		usernameEdit = ((EditText) findViewById(R.id.edit_username));
		findViewById(R.id.load_btn).setOnClickListener(this);

		changeCombatText();
		username = getIntent().getStringExtra("username");
		if(username != null){
			usernameEdit.setText(username);
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
				
				combatText.setText(getString(R.string.combat_lvl, Utils.getCombatLvl(playerSkills)));
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
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						findViewById(R.id.load_btn).setEnabled(false);
					}});
				playerSkills = hiscoreHelper.getPlayerStats();
			} catch (PlayerNotFoundException e) {
				e.printStackTrace();
				eraseAndChangeHint(getString(R.string.not_existing_player, username));
			} catch (Exception uhe) {
				uhe.printStackTrace();
				eraseAndChangeHint(getString(R.string.network_error));
			}
			return playerSkills;
		}

		@Override
		protected void onPostExecute(final PlayerSkills playerSkillsCallback) {
			if (playerSkillsCallback != null) {
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						findViewById(R.id.load_btn).setEnabled(true);
						usernameEdit.setHint(R.string.prompt_username);
						hitpointEdit.setText(playerSkillsCallback.hitpoints.getLevel()+"");
						attackEdit.setText(playerSkillsCallback.attack.getLevel()+"");
						strengthEdit.setText(playerSkillsCallback.strength.getLevel()+"");
						defenceEdit.setText(playerSkillsCallback.defence.getLevel()+"");
						magicEdit.setText(playerSkillsCallback.magic.getLevel()+"");
						rangingEdit.setText(playerSkillsCallback.ranged.getLevel()+"");
						prayerEdit.setText(playerSkillsCallback.prayer.getLevel()+"");
						changeCombatText();
					}});
			}
		}
	}
	
	public void eraseAndChangeHint(final String text){
		runOnUiThread(new Runnable(){

			@Override
			public void run() {
				usernameEdit.setText("");
				usernameEdit.setHint(text);
				findViewById(R.id.load_btn).setEnabled(true);
			}});
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		changeCombatText();
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.load_btn) {
			String text = usernameEdit.getText().toString();
			if(text != null && !text.isEmpty()){
				username = text;
				new PopulateTable().execute();
			}
		}
	}
}
