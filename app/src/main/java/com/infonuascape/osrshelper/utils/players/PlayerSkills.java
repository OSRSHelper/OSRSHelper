package com.infonuascape.osrshelper.utils.players;

import java.util.ArrayList;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.SkillsEnum.SkillType;

public class PlayerSkills {
	public Skill overall = new Skill(SkillType.Overall, R.drawable.overall);
	public Skill attack = new Skill(SkillType.Attack, R.drawable.attack);
	public Skill defence = new Skill(SkillType.Defence, R.drawable.defence);
	public Skill strength = new Skill(SkillType.Strength, R.drawable.strength);
	public Skill hitpoints = new Skill(SkillType.Hitpoints, R.drawable.constitution);
	public Skill ranged = new Skill(SkillType.Ranged, R.drawable.ranged);
	public Skill prayer = new Skill(SkillType.Prayer, R.drawable.prayer);
	public Skill magic = new Skill(SkillType.Magic, R.drawable.magic);
	public Skill cooking = new Skill(SkillType.Cooking, R.drawable.cooking);
	public Skill woodcutting = new Skill(SkillType.Woodcutting, R.drawable.woodcutting);
	public Skill fletching = new Skill(SkillType.Fletching, R.drawable.fletching);
	public Skill fishing = new Skill(SkillType.Fishing, R.drawable.fishing);
	public Skill firemaking = new Skill(SkillType.Firemaking, R.drawable.firemaking);
	public Skill crafting = new Skill(SkillType.Crafting, R.drawable.crafting);
	public Skill smithing = new Skill(SkillType.Smithing, R.drawable.smithing);
	public Skill mining = new Skill(SkillType.Mining, R.drawable.mining);
	public Skill herblore = new Skill(SkillType.Herblore, R.drawable.herblore);
	public Skill agility = new Skill(SkillType.Agility, R.drawable.agility);
	public Skill thieving = new Skill(SkillType.Thieving, R.drawable.thieving);
	public Skill slayer = new Skill(SkillType.Slayer, R.drawable.slayer);
	public Skill farming = new Skill(SkillType.Farming, R.drawable.farming);
	public Skill runecraft = new Skill(SkillType.Runecraft, R.drawable.runecrafting);
	public Skill hunter = new Skill(SkillType.Hunter, R.drawable.hunter);
	public Skill construction = new Skill(SkillType.Construction, R.drawable.construction);
	public String sinceWhen;

	public void setSinceWhen(String sinceWhen) {
		this.sinceWhen = sinceWhen;
	}

	public static ArrayList<Skill> getSkillsInOrder(PlayerSkills playerSkills) {
		ArrayList<Skill> skills = new ArrayList<Skill>();

		skills.add(playerSkills.overall);
		skills.add(playerSkills.attack);
		skills.add(playerSkills.defence);
		skills.add(playerSkills.strength);
		skills.add(playerSkills.hitpoints);
		skills.add(playerSkills.ranged);
		skills.add(playerSkills.prayer);
		skills.add(playerSkills.magic);
		skills.add(playerSkills.cooking);
		skills.add(playerSkills.woodcutting);
		skills.add(playerSkills.fletching);
		skills.add(playerSkills.fishing);
		skills.add(playerSkills.firemaking);
		skills.add(playerSkills.crafting);
		skills.add(playerSkills.smithing);
		skills.add(playerSkills.mining);
		skills.add(playerSkills.herblore);
		skills.add(playerSkills.agility);
		skills.add(playerSkills.thieving);
		skills.add(playerSkills.slayer);
		skills.add(playerSkills.farming);
		skills.add(playerSkills.runecraft);
		skills.add(playerSkills.hunter);
		skills.add(playerSkills.construction);

		return skills;
	}

	public static ArrayList<Skill> getSkillsInOrderForRSView(PlayerSkills playerSkills) {
		ArrayList<Skill> skills = new ArrayList<Skill>();

		skills.add(playerSkills.attack);
		skills.add(playerSkills.hitpoints);
		skills.add(playerSkills.mining);
		skills.add(playerSkills.strength);
		skills.add(playerSkills.agility);
		skills.add(playerSkills.smithing);
		skills.add(playerSkills.defence);
		skills.add(playerSkills.herblore);
		skills.add(playerSkills.fishing);
		skills.add(playerSkills.ranged);
		skills.add(playerSkills.thieving);
		skills.add(playerSkills.cooking);
		skills.add(playerSkills.prayer);
		skills.add(playerSkills.crafting);
		skills.add(playerSkills.firemaking);
		skills.add(playerSkills.magic);
		skills.add(playerSkills.fletching);
		skills.add(playerSkills.woodcutting);
		skills.add(playerSkills.runecraft);
		skills.add(playerSkills.slayer);
		skills.add(playerSkills.farming);
		skills.add(playerSkills.construction);
		skills.add(playerSkills.hunter);
		skills.add(playerSkills.overall);

		return skills;
	}
}
