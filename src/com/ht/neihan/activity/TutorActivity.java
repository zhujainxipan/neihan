package com.ht.neihan.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.ht.neihan.Constants;
import com.ht.neihan.R;
import com.ht.neihan.adapter.TutorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annuo on 2015/5/26.
 */
public class TutorActivity extends Activity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);
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
        SharedPreferences sp = getSharedPreferences(Constants.SP_NAME_APP, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //根据版本来进行存储
        editor.putBoolean(Constants.SP_KEY_SHOW_TUTOR + versionCode, true);
        editor.commit();

        ////////////////////////////////
        ViewPager viewPager = (ViewPager) findViewById(R.id.tutor_pager);
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.bg_intros1);
        images.add(R.drawable.bg_intros2);
        images.add(R.drawable.bg_intros3);
        images.add(R.drawable.bg_intros4);
        TutorAdapter adapter = new TutorAdapter(this, images);
        //设置最后一页按钮点击事件
        adapter.setOnClickListener(this);
        viewPager.setAdapter(adapter);

    }

    /**
     * 实现tutoradapter中的接口方法
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}