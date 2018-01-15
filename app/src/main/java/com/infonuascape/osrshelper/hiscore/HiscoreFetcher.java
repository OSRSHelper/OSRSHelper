package com.infonuascape.osrshelper.hiscore;

import android.content.Context;

import com.android.volley.Request;
import com.infonuascape.osrshelper.enums.AccountType;
import com.infonuascape.osrshelper.enums.SkillType;
import com.infonuascape.osrshelper.models.Skill;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.HTTPRequest.StatusCode;
import com.infonuascape.osrshelper.utils.http.NetworkStack;
import com.infonuascape.osrshelper.models.players.PlayerSkills;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maden on 9/9/14.
 */
public class HiscoreFetcher {
	private Context context;
	private String userName;
    private AccountType accountType;

	public HiscoreFetcher(final Context context, String userName, AccountType accountType) {
		this.context = context;
		this.userName = userName.replace(" ", "%20");
		this.accountType = accountType;
	}

	public String getUserName() {
		return userName;
	}

    public AccountType getAccountType() {
        return accountType;
    }

    public PlayerSkills getPlayerSkills() throws PlayerNotFoundException {
		String APIOutput = getDataFromAPI();
		PlayerSkills ps = new PlayerSkills();
		List<Skill> skillList = new ArrayList<Skill>();
		skillList.add(ps.overall);
		skillList.add(ps.attack);
		skillList.add(ps.defence);
		skillList.add(ps.strength);
		skillList.add(ps.hitpoints);
		skillList.add(ps.ranged);
		skillList.add(ps.prayer);
		skillList.add(ps.magic);
		skillList.add(ps.cooking);
		skillList.add(ps.woodcutting);
		skillList.add(ps.fletching);
		skillList.add(ps.fishing);
		skillList.add(ps.firemaking);
		skillList.add(ps.crafting);
		skillList.add(ps.smithing);
		skillList.add(ps.mining);
		skillList.add(ps.herblore);
		skillList.add(ps.agility);
		skillList.add(ps.thieving);
		skillList.add(ps.slayer);
		skillList.add(ps.farming);
		skillList.add(ps.runecraft);
		skillList.add(ps.hunter);
		skillList.add(ps.construction);
		String[] skillArray = APIOutput.split("\n");
		String[] dataSeparator;
		for (int i = 0; i <= skillList.size() - 1; i++) {
			dataSeparator = skillArray[i].split(",");
			skillList.get(i).setRank(Integer.parseInt(dataSeparator[0]));
			skillList.get(i).setLevel(Short.parseShort(dataSeparator[1]));
			skillList.get(i).setExperience(Long.parseLong(dataSeparator[2]));
			if(skillList.get(i).getSkillType() != SkillType.Overall) {
				skillList.get(i).calculateLevels();
			}
		}

		//compute total level
		short totalLevel = 0;
		short totalVirtualLevel = 0;
		for (Skill s : skillList) {
			if (s.getSkillType() != SkillType.Overall) {
				totalLevel += s.getLevel();
				totalVirtualLevel += s.getVirtualLevel();
			}
		}

		//add total level to appropriate Skill entry
		Skill overallSkill = skillList.get(0); //always first indexed
		overallSkill.setLevel(totalLevel);
		overallSkill.setVirtualLevel(totalVirtualLevel);

        ps.calculateIfVirtualLevelsNecessary();

		return ps;
	}

	public PlayerSkills mapDataSet(String dataSet) {
		// split dataset, map to skills enum
		return new PlayerSkills(); // dummy return
	}

	private String getAPIEndpoint() {
	    switch (this.accountType) {
            case REGULAR:
                return "http://services.runescape.com/m=hiscore_oldschool/index_lite.ws?player=";
            case IRONMAN:
                return "http://services.runescape.com/m=hiscore_oldschool_ironman/index_lite.ws?player=";
            case ULTIMATE_IRONMAN:
                return "http://services.runescape.com/m=hiscore_oldschool_ultimate/index_lite.ws?player=";
            case HARDCORE_IRONMAN:
                return "http://services.runescape.com/m=hiscore_oldschool_hardcore_ironman/index_lite.ws?player=";
        }
        return "http://services.runescape.com/m=hiscore_oldschool/index_lite.ws?player=";
    }

	private String getDataFromAPI() throws PlayerNotFoundException {
		HTTPRequest httpRequest = NetworkStack.getInstance(context).performRequest(getAPIEndpoint() + getUserName(), Request.Method.GET);
		if (httpRequest.getStatusCode() == StatusCode.FOUND) {
			return httpRequest.getOutput();
		} else {
			throw new PlayerNotFoundException(getUserName());
		}
	}
}
