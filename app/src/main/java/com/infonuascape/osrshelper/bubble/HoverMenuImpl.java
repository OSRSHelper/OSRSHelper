package com.infonuascape.osrshelper.bubble;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.mattcarroll.hover.HoverMenu;

public class HoverMenuImpl extends HoverMenu {
    private Context context;
    private List<Section> sections;

    public HoverMenuImpl(@NonNull Context context) {
        this.context = context;

        sections = new ArrayList<>();
        Section sectionWiki = new Section(
                new SectionId(context.getString(R.string.wiki)),
                createTabView(R.drawable.wiki),
                new HoverWikiSection(context)
        );
        sections.add(sectionWiki);

        Section sectionHiscores = new Section(
                new SectionId(context.getString(R.string.hiscore_title)),
                createTabView(R.drawable.hiscore),
                new HoverHiscoreSection(context)
        );
        sections.add(sectionHiscores);
    }

    private View createTabView(final int drawableId) {
        ImageView imageView = new ImageView(context);
        imageView.setBackgroundResource(R.drawable.circle_white);
        int padding = (int) Utils.convertDpToPixel(4, context);
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setImageResource(drawableId);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        return imageView;
    }

    @Override
    public String getId() {
        return context.getString(R.string.app_name);
    }

    @Override
    public int getSectionCount() {
        return sections.size();
    }

    @Nullable
    @Override
    public Section getSection(int index) {
        return sections.get(index);
    }

    @Nullable
    @Override
    public Section getSection(@NonNull SectionId sectionId) {
        for (Section section : sections) {
            if (sectionId.equals(section.getId())) {
                return section;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public List<Section> getSections() {
        return sections;
    }

}
