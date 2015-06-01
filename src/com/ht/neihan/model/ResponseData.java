package com.ht.neihan.model;

/**
 * Created by annuo on 2015/5/27.
 */

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 从服务器返回的数据的总体对象
 */
public class ResponseData {

    /**
     * 如果是success，成功
     */
    private String message;
    private EssayListData data;

    public void parseJSON(JSONObject json) {
        //TODO 进行变量的解析
        try {
            message = json.getString("message");
            //创建对象，对象自己解析json
            data = new EssayListData();
            JSONObject object = json.getJSONObject("data");
            data.parseJSON(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public String getMessage() {
        return message;
    }

    public EssayListData getData() {
        return data;
    }
}
