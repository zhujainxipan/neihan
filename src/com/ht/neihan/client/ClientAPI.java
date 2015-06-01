package com.ht.neihan.client;

/**
 * Created by annuo on 2015/5/27.
 */

import android.os.Build;
import android.util.Log;
import com.ht.util.CommonHttpUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 内涵段子网络请求的接口实现类
 */
public class ClientAPI {

    /**
     * 封装的方式，加载数据还是原来的形式，不带mintype和maxtype
     * @param contentType
     * @param count
     * @return
     */
    public static JSONObject getEssayList(String contentType, int count) {
        return getEssayList(contentType, count, 0, 0);
    }

    //获取段子部分的接口

    /**
     * 获取段子的列表，根据contentType的类型，请求接口
     * contentType可选值：-101推荐 -104视频 -103图片 -102段子
     * @param count 一次返回的条目录
     * @param loadType 内容类型 0：不指定 1：加载新数据 2：加载旧数据
     * @param time
     * @param contentType
     * @return
     */
    public static JSONObject getEssayList(String contentType, int count, int loadType, int time) {
        //1、ping网址
        //2、联网，调接口
        //3、生成JSON
        JSONObject ret = null;
        StringBuilder sb = new StringBuilder("http://ic.snssdk.com/neihan/stream/mix/v1/");
        //拼参数
        sb.append("?content_type=").append(contentType);
        try {
            sb.append("&").append("message_cursor=-1");
            sb.append("&city=").append(URLEncoder.encode("北京", "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        sb.append("&count=").append(count);

        //增加加载类型的支持 by annuo 150529
        switch (loadType) {
            //不设置参数
            case 0:
                break;
            //取新数据，用mintime
            case 1:
                sb.append("&min_time=").append(time);
                break;
            //取旧数据，使用maxtime
            case 2:
                sb.append("&max_time=").append(time);
                break;
        }

        //设置屏幕宽度的操作
        //TODO 屏幕宽度需要上下文
        sb.append("&screen_width=640");
        sb.append("&ad=wifi");
        /**
         * channel=baidu2
         aid=7
         app_name=joke_essay
         version_code=400
         device_platform=android
         device_type=KFTT
         os_api=15
         os_version=4.0.3
         */
        sb.append("&channel=baidu2");
        sb.append("&aid=7");
        sb.append("&app_name=joke_essay");
        sb.append("&version_code=400");
        sb.append("&device_platform=android");
        //android手机sdk的级别
        sb.append("&os_api=").append(Build.VERSION.SDK_INT);
        sb.append("&device_type=KFTT");
        //
        sb.append("&os_version=").append(Build.VERSION.RELEASE);
        //获取手机的唯一序列号，手机唯一的序列号，在android api9以上才有
        sb.append("&openudid=").append(Build.SERIAL);

        String url = sb.toString();
        Log.d("myurl", url);
        // TODO 联网
        byte[] data = CommonHttpUtil.doGet(url);
        if (data != null) {
            try {
                String jsonStr = new String(data, "UTF-8");
                ret = new JSONObject(jsonStr);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return ret;
    }
}
