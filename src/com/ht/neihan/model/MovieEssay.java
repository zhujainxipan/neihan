package com.ht.neihan.model;

/**
 * Created by annuo on 2015/5/27.
 */

import org.json.JSONObject;

/**
 * 视频部分
 */
public class MovieEssay extends CommonEssay {
    private String mp4Url;
    private ResourceInfo video720p;
    private String keyword;
    private ResourceInfo getVideo480;

    /**
     * 视频的时长
     */
    private int duration;

    /**
     * m3u8是苹果视频播放器使用的播放列表，用于流媒体的播放
     */
    private String m3uUrl;

    /**
     * 视频大的封面
     */
    private ResourceInfo largerCover;

    /**
     * 视频的标题
     */
    private String title;

    /**
     * 原始视频
     */
    private ResourceInfo originVideo;

    private int videoHeight;

    private String publishTime;

    /**
     * 播放次数
     */
    private int playCount;
    private String flashUrl;

    /**
     * 封面图像的唯一地址
     */
    private String coverImageUri;

    public void parseJSON(JSONObject json) {
        //TODO 进行变量的解析
        super.parseJSON(json);

    }


}
