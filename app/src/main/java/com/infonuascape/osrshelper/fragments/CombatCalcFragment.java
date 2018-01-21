package com.infonuascape.osrshelper.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.hiscore.HiscoreFetcher;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.utils.Utils;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.models.players.PlayerSkills;

public class CombatCalcFragment extends OSRSFragment implements TextWatcher {
	private static final String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";

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

	private Account account;
	private ProfileHeaderFragment profileHeaderFragment;

	public static CombatCalcFragment newInstance(final Account account) {
		Answers.getInstance().logContentView(new ContentViewEvent()
				.putContentName("Combat Calculator"));
		CombatCalcFragment fragment = new CombatCalcFragment();
		Bundle b = new Bundle();
		b.putSerializable(EXTRA_ACCOUNT, account);
		fragment.setArguments(b);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.combat_lvl_calc, null);

		account = (Account) getArguments().getSerializable(EXTRA_ACCOUNT);

		profileHeaderFragment = (ProfileHeaderFragment) getChildFragmentManager().findFragmentById(R.id.profile_header);
		profileHeaderFragment.refreshProfile(account);
		profileHeaderFragment.setTitle(R.string.combat_lvl_calculator);

		combatText = (TextView) view.findViewById(R.id.combat_lvl);
		lvlNeededText = (TextView) view.findViewById(R.id.number_lvl_needed);
		attStrText = (TextView) view.findViewById(R.id.attack_strength_needed);
		hpDefText = (TextView) view.findViewById(R.id.hitpoint_defence_needed);
		rangeText = (TextView) view.findViewById(R.id.ranging_needed);
		mageText = (TextView) view.findViewById(R.id.magic_needed);
		prayerText = (TextView) view.findViewById(R.id.prayer_needed);

		hitpointEdit = ((EditText) view.findViewById(R.id.edit_hitpoints));
		hitpointEdit.addTextChangedListener(this);

		attackEdit = ((EditText) view.findViewById(R.id.edit_attack));
		attackEdit.addTextChangedListener(this);

		strengthEdit = ((EditText) view.findViewById(R.id.edit_strength));
		strengthEdit.addTextChangedListener(this);

		defenceEdit = ((EditText) view.findViewById(R.id.edit_defence));
		defenceEdit.addTextChangedListener(this);

		rangingEdit = ((EditText) view.findViewById(R.id.edit_ranging));
		rangingEdit.addTextChangedListener(this);

		prayerEdit = ((EditText) view.findViewById(R.id.edit_prayer));
		prayerEdit.addTextChangedListener(this);

		magicEdit = ((EditText) view.findViewById(R.id.edit_magic));
		magicEdit.addTextChangedListener(this);

		changeCombatText();
		if(account != null){
			new PopulateTable().execute();
		}

		return view;
	}
	
	private void changeCombatText(){
		if(getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
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

					playerSkills.hitpoints = hitpoint.isEmpty() ? new Skill(SkillType.Hitpoints, 0, (short) 0) : new Skill(SkillType.Hitpoints, 0, Short.valueOf(hitpoint));
					playerSkills.attack = attack.isEmpty() ? new Skill(SkillType.Attack, 0, (short) 0) : new Skill(SkillType.Attack, 0, Short.valueOf(attack));
					playerSkills.defence = defence.isEmpty() ? new Skill(SkillType.Defence, 0, (short) 0) : new Skill(SkillType.Defence, 0, Short.valueOf(defence));
					playerSkills.strength = strength.isEmpty() ? new Skill(SkillType.Strength, 0, (short) 0) : new Skill(SkillType.Strength, 0, Short.valueOf(strength));
					playerSkills.ranged = ranging.isEmpty() ? new Skill(SkillType.Ranged, 0, (short) 0) : new Skill(SkillType.Ranged, 0, Short.valueOf(ranging));
					playerSkills.prayer = prayer.isEmpty() ? new Skill(SkillType.Prayer, 0, (short) 0) : new Skill(SkillType.Prayer, 0, Short.valueOf(prayer));
					playerSkills.magic = magic.isEmpty() ? new Skill(SkillType.Magic, 0, (short) 0) : new Skill(SkillType.Magic, 0, Short.valueOf(magic));

					boolean isOneShown = false;
					combatText.setText(getString(R.string.combat_lvl, Utils.getCombatLvl(playerSkills)));

					int missingAttStr = Utils.getMissingAttackStrengthUntilNextCombatLvl(playerSkills);
					if (playerSkills.attack.getLevel() + playerSkills.strength.getLevel() + missingAttStr < 199) {
						attStrText.setText(getString(R.string.attack_strength_lvl_needed, missingAttStr));
						attStrText.setVisibility(View.VISIBLE);
						isOneShown = true;
					} else {
						attStrText.setVisibility(View.GONE);
					}

					int missingHpDef = Utils.getMissingHPDefenceUntilNextCombatLvl(playerSkills);

					if (playerSkills.hitpoints.getLevel() + playerSkills.defence.getLevel() + missingHpDef < 199) {
						hpDefText.setText(getString(R.string.hitpoint_defence_lvl_needed, missingHpDef));
						hpDefText.setVisibility(View.VISIBLE);
						isOneShown = true;
					} else {
						hpDefText.setVisibility(View.GONE);
					}

					int missingMage = Utils.getMissingMagicUntilNextCombatLvl(playerSkills);
					if (playerSkills.magic.getLevel() + missingMage <= 99) {
						mageText.setText(getString(R.string.magic_lvl_needed, missingMage));
						mageText.setVisibility(View.VISIBLE);
						isOneShown = true;
					} else {
						mageText.setVisibility(View.GONE);
					}

					int missingRanged = Utils.getMissingRangingUntilNextCombatLvl(playerSkills);
					if (playerSkills.ranged.getLevel() + missingRanged <= 99) {
						rangeText.setText(getString(R.string.ranging_lvl_needed, missingRanged));
						rangeText.setVisibility(View.VISIBLE);
						isOneShown = true;
					} else {
						rangeText.setVisibility(View.GONE);
					}

					int missingPrayer = Utils.getMissingPrayerUntilNextCombatLvl(playerSkills);
					if (playerSkills.prayer.getLevel() + missingPrayer <= 99) {
						prayerText.setText(getString(R.string.prayer_lvl_needed, missingPrayer));
						prayerText.setVisibility(View.VISIBLE);
						isOneShown = true;
					} else {
						prayerText.setVisibility(View.GONE);
					}

					if (!isOneShown) {
						lvlNeededText.setText(R.string.maxed_out);
					} else {
						lvlNeededText.setText(R.string.lvl_need);
					}
				}
			});
		}
	}
	
	private class PopulateTable extends AsyncTask<String, Void, PlayerSkills> {

		@Override
		protected PlayerSkills doInBackground(String... urls) {
			PlayerSkills playerSkills = null;

			try {
				playerSkills = new HiscoreFetcher(getContext(), account.username, account.type).getPlayerSkills();
			} catch (PlayerNotFoundException e) {
				e.printStackTrace();
				changeHint(getString(R.string.not_existing_player, account.username));
			} catch (Exception uhe) {
				uhe.printStackTrace();
				changeHint(getString(R.string.internal_error));
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
		if(getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					combatText.setText(text);
				}
			});
		}
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
