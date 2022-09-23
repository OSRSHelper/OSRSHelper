package com.infonuascape.osrshelper.network;

import com.infonuascape.osrshelper.models.HTTPResult;
import com.infonuascape.osrshelper.models.News;
import com.infonuascape.osrshelper.models.StatusCode;
import com.infonuascape.osrshelper.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsApi {
    private static final String TAG = "NewsApi";

    private static final String API_URL = NetworkStack.ENDPOINT + "/news/%s";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_LINK = "link";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_CATEGORY = "category";

    public static List<News> fetch(final int pageNum) {
        Logger.add(TAG, ": fetch: pageNum=", pageNum);
        final String url = String.format(API_URL, pageNum);
        HTTPResult httpResult = NetworkStack.getInstance().performGetRequest(url);

        List<News> newsList = new ArrayList<>();
        if (httpResult.statusCode == StatusCode.FOUND) {
            try {
                JSONArray jsonArray = new JSONArray(httpResult.output);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    News news = new News();
                    news.title = object.getString(KEY_TITLE);
                    news.description = object.getString(KEY_DESCRIPTION);
                    news.category = object.getString(KEY_CATEGORY);
                    news.url = object.getString(KEY_LINK);
                    news.imageUrl = object.getString(KEY_IMAGE_URL);
                    news.publicationDate = object.getString(KEY_DATE);
                    newsList.add(news);
                }
            } catch (JSONException e) {
                Logger.addException(e);
            }
        }

        return newsList;
    }
}
