package com.infonuascape.osrshelper.models.players;

import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.models.Skill;

import java.util.ArrayList;
import java.util.List;

public class PlayerSkills {
	public Skill overall = new Skill(SkillType.Overall ,0);
	public Skill attack = new Skill(SkillType.Attack,1);
	public Skill defence = new Skill(SkillType.Defence,2);
	public Skill strength = new Skill(SkillType.Strength,3);
	public Skill hitpoints = new Skill(SkillType.Hitpoints,4);
	public Skill ranged = new Skill(SkillType.Ranged,5);
	public Skill prayer = new Skill(SkillType.Prayer,6);
	public Skill magic = new Skill(SkillType.Magic,7);
	public Skill cooking = new Skill(SkillType.Cooking,8);
	public Skill woodcutting = new Skill(SkillType.Woodcutting,9);
	public Skill fletching = new Skill(SkillType.Fletching,10);
	public Skill fishing = new Skill(SkillType.Fishing,11);
	public Skill firemaking = new Skill(SkillType.Firemaking,12);
	public Skill crafting = new Skill(SkillType.Crafting,13);
	public Skill smithing = new Skill(SkillType.Smithing,14);
	public Skill mining = new Skill(SkillType.Mining,15);
	public Skill herblore = new Skill(SkillType.Herblore,16);
	public Skill agility = new Skill(SkillType.Agility,17);
	public Skill thieving = new Skill(SkillType.Thieving,18);
	public Skill slayer = new Skill(SkillType.Slayer,19);
	public Skill farming = new Skill(SkillType.Farming,20);
	public Skill runecraft = new Skill(SkillType.Runecrafting,21);
	public Skill hunter = new Skill(SkillType.Hunter,22);
	public Skill construction = new Skill(SkillType.Construction,23);
	public Skill Vorkath = new Skill(SkillType.Vorkath,75);
	public Skill CoX = new Skill(SkillType.CoX, 41);
	public Skill ToB = new Skill(SkillType.ToB, 69);

	public String lastUpdate;

	public List<Skill> skillList = new ArrayList<Skill>();



	public PlayerSkills() {
		int a = 0;
		skillList.add(this.overall);
		skillList.add(this.attack);
		skillList.add(this.defence);
		skillList.add(this.strength);
		skillList.add(this.hitpoints);
		skillList.add(this.ranged);
		skillList.add(this.prayer);
		skillList.add(this.magic);
		skillList.add(this.cooking);
		skillList.add(this.woodcutting);
		skillList.add(this.fletching);
		skillList.add(this.fishing);
		skillList.add(this.firemaking);
		skillList.add(this.crafting);
		skillList.add(this.smithing);
		skillList.add(this.mining);
		skillList.add(this.herblore);
		skillList.add(this.agility);
		skillList.add(this.thieving);
		skillList.add(this.slayer);
		skillList.add(this.farming);
		skillList.add(this.runecraft);
		skillList.add(this.hunter);
		skillList.add(this.construction);
		skillList.add(this.Vorkath);
		skillList.add(this.CoX);
		skillList.add(this.ToB);

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
		skills.add(playerSkills.Vorkath);
		skills.add(playerSkills.CoX);
		skills.add(playerSkills.ToB);

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
		skills.add(playerSkills.Vorkath);
		skills.add(playerSkills.CoX);
		skills.add(playerSkills.ToB);
		//TODO add MORE

		return skills;
	}
}
