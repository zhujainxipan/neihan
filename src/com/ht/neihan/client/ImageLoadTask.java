package com.ht.neihan.client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;
import com.ht.util.CacheUtil;
import com.ht.util.CircleImageUtils;
import com.ht.util.CommonHttpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EventListener;

/**
 * Created by annuo on 2015/5/30.
 */

/**
 * 图片下载器
 */
public class ImageLoadTask extends AsyncTask<String, Integer, Bitmap> {

    ImageView imageView;
    private String url;
    private String imgType;

    /**
     * 加载图片给特定的imageview
     * @param imageView
     */
    public ImageLoadTask(ImageView imageView, String imgType) {
        this.imageView = imageView;
        this.imgType = imgType;
    }

    public ImageLoadTask(ImageView imageView) {
        this.imageView = imageView;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap ret = null;
        if (params != null && params.length > 0) {
            //url作为网址请求，同时还作为imageview tag的检查
            url = params[0];
            //todo 文件缓存的检测
            String sdcardState = Environment.getExternalStorageState();
            //找到的缓存的文件
            File targetFile = null;
            if (sdcardState.equals(Environment.MEDIA_MOUNTED)) {
                //存储卡根目录
                File storageDirectory = Environment.getExternalStorageDirectory();
                //对应的文件目录地址：/mnt/sdcard/.neihan/images/
                File imageCacheRoot = new File(storageDirectory, "/neihan/images");
                boolean b = true;
                if (!imageCacheRoot.exists()) {
                    //递归的创建指定的文件目录,确保创建成功
                    b = imageCacheRoot.mkdirs();
                    //只创建当前目录，如果父目录不存在，就报错
                    //imageCacheRoot.mkdir();
                }
                if (b) {
                    //进行文件的查找。根据URL查找
                    //进行映射
                    String fileName = CacheUtil.md5(url);
                    targetFile = new File(imageCacheRoot, fileName);
                }
            }


            //检查缓存文件
            if (targetFile.exists()) {
                //todo 加载图片，返回结果
                //说明缓存存在，就不需要联网了
                ret = BitmapFactory.decodeFile(targetFile.getAbsolutePath());
            } else {
                //加载网络图片,说明缓存不存在，需要联网加载
                try {
                    URL u = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                    conn.setConnectTimeout(3000);
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("connection", "keep-alive");
                    conn.connect();
                    //获取输入流
                    InputStream inputStream = conn.getInputStream();
                    //读取图片数据到字节数组，便于后续的文件缓存操作，我们缓存的是二进制文件，而不是图片文件
                    byte[] data = CommonHttpUtil.readStream(inputStream);
                    inputStream.close();
                    //断开网络连接
                    conn.disconnect();
                    //todo 要针对图片进行本地文件的缓存

                    if (targetFile != null) {
                        //能够获取到sd卡的路径文件对象，但是文件不存在
                        boolean b = targetFile.createNewFile();
                        //创建文件成功
                        if (b) {
                            FileOutputStream fout = null;
                            try {
                                fout = new FileOutputStream(targetFile);
                                fout.write(data);
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            } finally {
                                if (fout != null) {
                                    try {
                                        fout.close();
                                    } catch (IOException ioe) {
                                        ioe.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    ret = BitmapFactory.decodeByteArray(data, 0, data.length);
                    //当头像创建成功，bitmap会包含一份图像的数据
                    //bytes也是一份图像的数据，需要释放bytes
                    data = null;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //网络加载结束
            }
        }
        return ret;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            if (imageView != null) {

                if (imgType != null && imgType.equals("userico")) {
                    bitmap =CircleImageUtils.createCircleImage(bitmap);
                }
                Object tag = imageView.getTag();
                if (tag != null && tag instanceof String) {
                    String s = (String) tag;
                    if (s.equals(url)) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        }
    }
}
