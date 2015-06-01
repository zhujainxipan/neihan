package com.ht.neihan.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by annuo on 2015/5/27.
 */
public class ResourceInfo {
    private int width;
    private int height;
    private String uri;
    private List<String> urlList;

    public void parseJSON(JSONObject json) {
        //TODO 进行变量的解析
        if (json != null) {
            width = json.optInt("width");
            height = json.optInt("height");
            try {
                uri = json.getString("uri");
                JSONArray array = json.getJSONArray("url_list");
                int len = array.length();
                urlList = new ArrayList<String>();
                for (int i = 0; i < len; i++) {
                    JSONObject object = array.getJSONObject(i);
                    String url = object.getString("url");
                    urlList.add(url);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getUri() {
        return uri;
    }

    public List<String> getUrlList() {
        return urlList;
    }
}
