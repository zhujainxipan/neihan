package com.ht.neihan.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.RadioGroup;
import com.ht.neihan.R;
import com.ht.neihan.fragment.HomeFragment;

public class MainActivity extends FragmentActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉activity标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        HomeFragment homeFragment = new HomeFragment();
        //默认显示首页界面
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_fragment_container, homeFragment);
        transaction.commit();

        //radiogroup的选中事件
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.main_tab_bar);

        //默认首页选中
        radioGroup.check(R.id.tab_home);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.tab_home:
                        //TODO
                        break;
                    case R.id.tab_discover:
                        //TODO

                        break;
                    case R.id.tab_message:
                        //TODO

                        break;
                    case R.id.tab_shenhe:
                        //TODO

                        break;
                }
            }
        });
    }
}
