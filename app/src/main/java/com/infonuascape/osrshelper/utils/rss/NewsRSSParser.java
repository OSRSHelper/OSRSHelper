package com.infonuascape.osrshelper.utils.rss;

import android.text.TextUtils;
import android.util.Xml;

import com.infonuascape.osrshelper.models.OSRSNews;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marc_ on 2018-01-20.
 */

public class NewsRSSParser {

    private static final int TAG_TITLE = 1;
    private static final int TAG_DESCRIPTION = 2;
    private static final int TAG_PUBLISHED = 3;
    private static final int TAG_LINK = 4;
    private static final int TAG_IMAGE_LINK = 5;
    private static final int TAG_CATEGORY = 6;

    private static final String ns = null;

    public List<OSRSNews> parse(final String xml)
            throws XmlPullParserException, IOException, ParseException {
        InputStream in = new ByteArrayInputStream(xml.getBytes());
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<OSRSNews> readFeed(XmlPullParser parser)
            throws XmlPullParserException, IOException, ParseException {
        List<OSRSNews> news = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("channel")) {
                parser.require(XmlPullParser.START_TAG, ns, "channel");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    name = parser.getName();
                    if (name.equals("item")) {
                        news.add(readNews(parser));
                    } else {
                        skip(parser);
                    }
                }
            } else {
                skip(parser);
            }
        }
        return news;
    }

    private OSRSNews readNews(XmlPullParser parser)
            throws XmlPullParserException, IOException, ParseException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        OSRSNews news = new OSRSNews();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")){
                news.title = readTag(parser, TAG_TITLE);
            } else if (name.equals("description")) {
                news.description = readTag(parser, TAG_DESCRIPTION);
            } else if (name.equals("link")) {
                news.url = readTag(parser, TAG_LINK);
            } else if (name.equals("enclosure")) {
                news.imageUrl = readTag(parser, TAG_IMAGE_LINK);
            } else if (name.equals("category")) {
                news.category = readTag(parser, TAG_CATEGORY);
            } else if (name.equals("pubDate")) {
                news.publicationDate = readTag(parser, TAG_PUBLISHED);
            } else {
                skip(parser);
            }
        }
        return news;
    }

    private String readTag(XmlPullParser parser, int tagType)
            throws IOException, XmlPullParserException {

        switch (tagType) {
            case TAG_LINK:
                return readBasicTag(parser, "link");
            case TAG_TITLE:
                return readBasicTag(parser, "title");
            case TAG_DESCRIPTION:
                return readBasicTag(parser, "description");
            case TAG_PUBLISHED:
                return readBasicTag(parser, "pubDate");
            case TAG_CATEGORY:
                return readBasicTag(parser, "category");
            case TAG_IMAGE_LINK:
                return readEnclosureImage(parser);
            default:
                throw new IllegalArgumentException("Unknown tag type: " + tagType);
        }
    }

    private String readBasicTag(XmlPullParser parser, String tag)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String result = readText(parser);
        if(!TextUtils.isEmpty(result)) {
            result = result.trim();
        }
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return result;
    }

    private String readEnclosureImage(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        String link = null;
        parser.require(XmlPullParser.START_TAG, ns, "enclosure");
        link = parser.getAttributeValue(null, "url");
        if(!TextUtils.isEmpty(link)) {
            link = link.trim();
        }
        while (true) {
            if (parser.nextTag() == XmlPullParser.END_TAG) break;
        }
        return link;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
