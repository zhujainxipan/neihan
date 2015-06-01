package com.ht.util;

/**
 * Created by annuo on 2015/5/30.
 */

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 文件缓存的工具类，包含网址到文件名称的映射
 */
public final class CacheUtil {

    private CacheUtil() {

    }

    /**
     * 将网址映射成一个特定的字符串信息，确保唯一
     * @param url
     * @return
     */
    public static String md5(String url) {
        String ret = null;
        if (url != null) {
            //针对数据进行算法处理，形成一个唯一的内容，每一个数据生成的内容都不同
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("MD5");
                //生成了url对应的唯一的信息
                byte[] data = digest.digest(url.getBytes());
                ret = toHex(data);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }


    public static String toHex(byte[] data) {
        String ret = null;
        if (data != null) {
            StringBuilder sb = new StringBuilder();
            for (byte b : data) {
                int h, l;
                //低四位
                l = b & 0x0f;
                //高四位
                h = (b >> 4) & 0x0f;
                sb.append(Integer.toHexString(h));
                sb.append(Integer.toHexString(l));
            }
            ret = sb.toString();
        }
        return ret;
    }
}
