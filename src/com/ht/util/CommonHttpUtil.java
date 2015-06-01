package com.ht.util;

/**
 * Created by annuo on 2015/5/27.
 */

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * 网络工具类
 */
public final class CommonHttpUtil {

    /**
     * 避免创建
     */
    private CommonHttpUtil() {

    }

    /**
     * 读取输入流，返回数据
     *
     * @param stream
     * @return
     */
    public static byte[] readStream(InputStream stream) throws IOException {
        byte[] ret = null;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buf = new byte[128];
        int len;
        while (true) {
            len = stream.read(buf);
            if (len == -1) {
                break;
            }
            bout.write(buf, 0, len);
        }
        //强制释放临时的资源
        buf = null;
        ret = bout.toByteArray();
        bout.close();
        return ret;
    }

    /**
     * get请求方法
     *
     * @param url 网址
     * @return
     */
    public static byte[] doGet(String url) {
        byte[] ret = null;
        HttpGet request = new HttpGet(url);
        HttpClient client = new DefaultHttpClient();
        try {
            HttpResponse response = client.execute(request);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                //返回的实际数据
                ret = readStream(stream);
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 进行网络post请求的方式，参数采用key-value形式
     *
     * @param url
     * @param params
     * @return
     */
    public static byte[] doPost(String url, HashMap<String, String> params) {
        return null;
    }

    /**
     * 进行put请求的方法，实际的操作和post类似
     *
     * @param url
     * @return
     */
    public static byte[] doPut(String url, HashMap<String, String> params) {
        return null;
    }

    /**
     * 进行delete请求的方法，实际的操作和get类似
     *
     * @param url
     * @return
     */
    public static byte[] doDelete(String url) {
        return null;
    }
}
