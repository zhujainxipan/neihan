package com.ht.neihan.model;

/**
 * Created by annuo on 2015/5/27.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 图片段子的定义
 */
public class ImageEssay extends CommonEssay {

    /**
     * 图片最大能显示屏幕的百分比
     */
    private double maxScreenWidthPercent;

    /**
     * 图片最小的能够显示屏幕的百分比
     */
    private double minScreenWidthPercent;

    /**
     * 保存大图的信息
     */
    private ResourceInfo largerImages;

    /**
     * 保存小图的信息
     */
    private ResourceInfo middleImages;

    /**
     * 图片状态
     */
    private int imageStatus;


    public void parseJSON(JSONObject json) {
        //TODO 进行变量的解析
        super.parseJSON(json);
        if (json != null) {
            try {
                JSONObject groupJson = json.getJSONObject("group");
                largerImages  = new ResourceInfo();
                JSONObject largerobject = groupJson.getJSONObject("large_image");
                if (largerobject != null) {
                    largerImages.parseJSON(largerobject);
                }

                middleImages  = new ResourceInfo();
                JSONObject midObject = groupJson.getJSONObject("middle_image");
                if (midObject != null) {
                    middleImages.parseJSON(midObject);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }



    public ResourceInfo getLargerImages() {
        return largerImages;
    }

    public ResourceInfo getMiddleImages() {
        return middleImages;
    }

    public int getImageStatus() {
        return imageStatus;
    }
}
