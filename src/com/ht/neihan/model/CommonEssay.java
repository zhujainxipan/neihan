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
 * 段子的公共数据，同时也代表了文本的段子
 */
public class CommonEssay {

    private long groupId;
    private long onlineTime;
    private long displayTime;

    /**
     * 段子（图片、视频、文本）都是1
     */
    private int type;

    private List<Comment> comments;

    //////////////////////////一下段子基本信息
    private long createTime;
    private int favoriteCount;
    /**
     * 当前用户是否收藏了
     */
    private boolean userFavorite;
    /**
     * 当前用户是否踩了
     */
    private boolean userBury;

    private int buryCount;
    private boolean isCanShare;
    private int commentCount;
    private String shareUrl;

    /**
     * 段子内容
     */
    private String content;
    private int categoryType;

    /**
     * 分享数量
     */
    private int shareCount;

    private boolean hasComments;

    private User user;

    private boolean userDigg;

    /**
     * 段子所属分类的名称
     */
    private String categoryName;

    /**
     * 猜测是热评
     */
    private int repinCount;

    /**
     * 顶数
     */
    private int diggCount;

    /**
     * 用户是否热评
     */
    private boolean userRepin;

    //////////////////////////////////////
    //解析部分

    public void parseJSON(JSONObject json) {
        //TODO 进行变量的解析
        if (json != null) {
            try {
                onlineTime = json.getLong("online_time");
                displayTime = json.getLong("display_time");
                type = json.getInt("type");
                //////////////////////////////////////////
                JSONObject group = json.getJSONObject("group");
                groupId = group.getLong("group_id");
                content = group.getString("content");
                //视频部分无user
                JSONObject object = group.optJSONObject("user");
                if (object != null) {
                    user = new User();
                    user.parseJSON(object);
                }
                //获取数量信息
                diggCount = group.getInt("digg_count");
                buryCount = group.getInt("bury_count");
                favoriteCount = group.getInt("favorite_count");
                shareCount = group.getInt("share_count");

                ////////////////////////////////////////////////
                commentCount = group.getInt("comment_count");
                hasComments = group.getInt("has_comments") != 0;
                if (hasComments) {
                    comments = new LinkedList<Comment>();
                    JSONArray array = json.getJSONArray("comments");
                    int len = array.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        Comment comment = new Comment();
                        comment.parseJSON(jsonObject);
                        comments.add(comment);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public long getGroupId() {
        return groupId;
    }

    public long getOnlineTime() {
        return onlineTime;
    }

    public long getDisplayTime() {
        return displayTime;
    }

    public int getType() {
        return type;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public long getCreateTime() {
        return createTime;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public boolean isUserFavorite() {
        return userFavorite;
    }

    public boolean isUserBury() {
        return userBury;
    }

    public int getBuryCount() {
        return buryCount;
    }

    public boolean isCanShare() {
        return isCanShare;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getContent() {
        return content;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public int getShareCount() {
        return shareCount;
    }

    public boolean isHasComments() {
        return hasComments;
    }

    public User getUser() {
        return user;
    }

    public boolean isUserDigg() {
        return userDigg;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getRepinCount() {
        return repinCount;
    }

    public int getDiggCount() {
        return diggCount;
    }

    public boolean isUserRepin() {
        return userRepin;
    }
}
