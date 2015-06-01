package com.ht.neihan.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.ht.neihan.Constants;
import com.ht.neihan.R;

/**
 * Created by annuo on 2015/5/26.
 */
public class SplashActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //获取sharedxxx，找一下是否显示过引导教程
                SharedPreferences sp = getSharedPreferences(Constants.SP_NAME_APP, MODE_PRIVATE);
                //保存当前版本的引导页已经显示了
                //获取versionCode
                PackageManager manager = getPackageManager();
                int versionCode = 1;
                try {
                    PackageInfo packageInfo = manager.getPackageInfo(getPackageName()
                            , PackageManager.GET_ACTIVITIES);
                    versionCode = packageInfo.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                boolean aBoolean = sp.getBoolean(Constants.SP_KEY_SHOW_TUTOR + versionCode, false);
                if (aBoolean) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, TutorActivity.class);
                    startActivity(intent);
                    finish();
                }
                super.run();
            }
        };
        thread.start();
    }
}