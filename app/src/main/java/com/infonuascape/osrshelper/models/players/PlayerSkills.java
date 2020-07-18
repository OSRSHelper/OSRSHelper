package com.infonuascape.osrshelper.models.players;

import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.models.HiscoreBoss;
import com.infonuascape.osrshelper.models.HiscoreBountyHunter;
import com.infonuascape.osrshelper.models.HiscoreClueScroll;
import com.infonuascape.osrshelper.models.HiscoreLms;
import com.infonuascape.osrshelper.models.Skill;

import java.util.ArrayList;
import java.util.List;

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
	public Skill runecraft = new Skill(SkillType.Runecrafting);
	public Skill hunter = new Skill(SkillType.Hunter);
	public Skill construction = new Skill(SkillType.Construction);

	public String lastUpdate;

	public List<Skill> skillList = new ArrayList<>();
	public List<HiscoreBoss> bossesList = new ArrayList<>();
	public List<HiscoreClueScroll> clueScrollsList = new ArrayList<>();
	public List<HiscoreBountyHunter> bountyHunterList = new ArrayList<>();
	public HiscoreLms hiscoreLms;

	public PlayerSkills() {
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

	public int getCount() {
		return skillList.size() + bountyHunterList.size() + clueScrollsList.size() + bossesList.size() + (hiscoreLms != null ? 1 : 0);
	}

	public Object getItem(int position) {
		int currentPosition = position;
		if (currentPosition < skillList.size()) {
			return skillList.get(currentPosition);
		}
		currentPosition -= skillList.size();
		if (currentPosition < bountyHunterList.size()) {
			return bountyHunterList.get(currentPosition);
		}
		currentPosition -= bountyHunterList.size();
		if (currentPosition < clueScrollsList.size()) {
			return clueScrollsList.get(currentPosition);
		}
		currentPosition -= clueScrollsList.size();
		if (currentPosition < bossesList.size()) {
			return bossesList.get(currentPosition);
		}
		return hiscoreLms;
	}
}
