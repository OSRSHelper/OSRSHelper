package com.infonuascape.osrshelper.bubble;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.mattcarroll.hover.HoverMenu;

public class BubbleMenu extends HoverMenu {
    private Context context;
    private List<Section> sections;

    public BubbleMenu(final Context context) {
        this.context = context;

        sections = new ArrayList<>();
        sections.add(new Section(
                new SectionId("hiscore"),
                HiscoreContent.createTabView(context),
                new HiscoreContent(context)
        ));
    }
    @Override
    public String getId() {
        return "bubblemenu";
    }

    @Override
    public int getSectionCount() {
        return sections.size();
    }

    @Override
    public Section getSection(int index) {
        return sections.get(index);
    }

    @Override
    public Section getSection(SectionId sectionId) {
        for(Section section : sections) {
            if(sectionId.equals(section.getId())) {
                return section;
            }
        }

        return null;
    }

    @Override
    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }
}
