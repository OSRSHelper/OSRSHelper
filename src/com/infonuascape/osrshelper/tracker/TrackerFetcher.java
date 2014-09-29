package com.infonuascape.osrshelper.tracker;

import com.infonuascape.osrshelper.utils.exceptions.ParserErrorException;
import com.infonuascape.osrshelper.utils.exceptions.PlayerNotFoundException;
import com.infonuascape.osrshelper.utils.Skill;
import com.infonuascape.osrshelper.utils.http.HTTPRequest;
import com.infonuascape.osrshelper.utils.http.HTTPRequest.StatusCode;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;
import org.xml.sax.Parser;

import static com.infonuascape.osrshelper.tracker.TrackerTimeEnum.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maden on 9/14/14.
 */
public class TrackerFetcher {
    final String API_URL = "http://rscript.org/lookup.php?type=track&flag=07track&skill=all";

    private String userName;
    private int lookupTime;

    public TrackerFetcher(String userName, int lookupTime) {
        this.userName = userName.replace(" ", "%20");
        this.lookupTime = lookupTime;
    }

    public TrackerFetcher(String userName, TrackerTime trackerTime) {
        this.userName = userName.replace(" ", "%20");
        this.lookupTime = trackerTime.getSeconds();
    }

    public String getUserName() {
        return userName;
    }

    public int getLookupTime() {
        return lookupTime;
    }

    public PlayerSkills getPlayerTracker() throws PlayerNotFoundException, ParserErrorException {
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
        for (String line : skillArray) {
            String[] tokenizerSkillLine = line.split(":");
            if (tokenizerSkillLine.length > 3) {
                if (tokenizerSkillLine[0] == "gain") {
                    if (tokenizerSkillLine.length < 3) {
                        for (int i = 0; i < skillList.size() - 1; i++) {
                            if (tokenizerSkillLine[2].toLowerCase().equals(skillList.get(i).toString())) {
                                skillList.get(i).setExperience(Long.parseLong(tokenizerSkillLine[3]));
                            }
                        }
                    } else {
                        throw new ParserErrorException("Error while parsing Zybez response");
                    }
                }
            }
        }
        return ps;
    }

    public PlayerSkills mapDataSet(String dataSet) {
        String[] dataSetArray;

        // split dataset, map to skills enum
        return new PlayerSkills(); // dummy return
    }

    private String getDataFromAPI() throws PlayerNotFoundException {
        String connectionString = API_URL + "&user=" + this.userName + "&time=" + this.lookupTime;

        HTTPRequest httpREquest = new HTTPRequest(connectionString, HTTPRequest.RequestType.GET);
        if (httpREquest.getStatusCode() == StatusCode.FOUND) {
            String output = httpREquest.getOutput();
            for (String line : output.split("\n")) {
                if (line == "0:-1") { //unknown name
                    throw new PlayerNotFoundException(getUserName());
                }
            }
        } //assume all went well if this point reached
        return httpREquest.getOutput();
    }
}
