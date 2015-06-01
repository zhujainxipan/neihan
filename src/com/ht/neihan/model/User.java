package com.ht.neihan.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by annuo on 2015/5/27.
 */
public class User {
    private boolean isFollowing;
    private String avatarUrl;
    private String userName;
    private boolean verified;
    private long userId;

    public void parseJSON(JSONObject json) {
        //TODO 进行变量的解析
        if (json != null) {
            try {
                isFollowing = json.getBoolean("is_following");
                avatarUrl = json.getString("avatar_url");
                userName = json.getString("name");
                userId = json.getLong("user_id");
                verified = json.getBoolean("user_verified");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isVerified() {
        return verified;
    }

    public long getUserId() {
        return userId;
    }
}
