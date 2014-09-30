package com.infonuascape.osrshelper.utils.players;

import com.infonuascape.osrshelper.utils.Skill;

import static com.infonuascape.osrshelper.utils.SkillsEnum.SkillType;

public class PlayerSkills {
	public Skill overall = new Skill(SkillType.Overall);
	public Skill attack = new Skill(SkillType.Attack);
	public Skill defence = new Skill(SkillType.Defence);
	public Skill strength = new Skill(SkillType.Strength);
	public Skill hitpoints = new Skill(SkillType.Hitpoints);
	public Skill ranged = new Skill(SkillType.Ranged);
	public Skill prayer = new Skill(SkillType.Prayer);
	public Skill magic = new Skill(SkillType.Magic);
	public Skill cooking = new Skill(SkillType.Cooking);
	public Skill woodcutting = new Skill(SkillType.Woodcutting);
	public Skill fletching = new Skill(SkillType.Fletching);
	public Skill fishing = new Skill(SkillType.Fishing);
	public Skill firemaking = new Skill(SkillType.Firemaking);
	public Skill crafting = new Skill(SkillType.Crafting);
	public Skill smithing = new Skill(SkillType.Smithing);
	public Skill mining = new Skill(SkillType.Mining);
	public Skill herblore = new Skill(SkillType.Herblore);
	public Skill agility = new Skill(SkillType.Agility);
	public Skill thieving = new Skill(SkillType.Thieving);
	public Skill slayer = new Skill(SkillType.Slayer);
	public Skill farming = new Skill(SkillType.Farming);
	public Skill runecraft = new Skill(SkillType.Runecraft);
	public Skill hunter = new Skill(SkillType.Hunter);
	public Skill construction = new Skill(SkillType.Construction);
	public String sinceWhen;

	public void setSinceWhen(String sinceWhen) {
		this.sinceWhen = sinceWhen;
	}
}
