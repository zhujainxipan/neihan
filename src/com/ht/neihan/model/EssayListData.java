package com.ht.neihan.model;

/**
 * Created by annuo on 2015/5/27.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * 整体的段子列表对象
 */
public class EssayListData {

    /**
     * 是否有更多：点击加载更多
     */
    private boolean hasMore;

    /**
     * 下一次加载数据（加载旧数据使用的参数）
     */
    private long maxTime;

    /**
     * 下一次刷新数据（加载新数据）使用的参数
     */
    private long minTime;
    private String tip;
    private boolean hasNewMessage;

    /**
     * 实际的段子列表
     */
    private List<CommonEssay> data;

    public void parseJSON(JSONObject json) {
        //TODO 进行变量的解析

        if (json != null) {
            try {
                hasMore = json.getBoolean("has_more");
                maxTime = json.optLong("max_time", 0);
                minTime = json.optLong("min_time", 0);
                tip = json.getString("tip");
                hasNewMessage = json.optBoolean("has_new_message");
                //进行段子列表的处理
                //段子数据
                JSONArray array = json.getJSONArray("data");
                int length = array.length();
                if (length > 0) {
                    //有数据，处理解析
                    data = new LinkedList<CommonEssay>();
                    for (int i = 0; i < length; i++) {
                        JSONObject object = array.getJSONObject(i);
                        JSONObject group = object.getJSONObject("group");
                        int is_vedio = group.optInt("is_video", 0);
                        int image_status = group.optInt("image_status", -1);
                        CommonEssay essay = null;
                        if (is_vedio == 1) {
                            essay = new MovieEssay();
                        } else if (image_status != -1) {
                            essay = new ImageEssay();//图片
                        } else {
                            essay = new CommonEssay();//文本
                        }
                        essay.parseJSON(object);
                        data.add(essay);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isHasMore() {
        return hasMore;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public long getMinTime() {
        return minTime;
    }

    public String getTip() {
        return tip;
    }

    public boolean isHasNewMessage() {
        return hasNewMessage;
    }

    public List<CommonEssay> getData() {
        return data;
    }
}
