package com.ht.neihan.fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ht.neihan.Constants;
import com.ht.neihan.R;
import com.ht.neihan.adapter.CommonFragmentAdapter;
import com.ht.util.CircleImageUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by annuo on 2015/5/26.
 */

/**
 * 整个应用程序首页部分，内部包含viewpager显示不同的列表
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private View indicator;
    private LinearLayout titleContainer;
    private ViewPager pager;
    private Animation animation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //因为动画的加载也非常耗时，所以放在这里
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_rotate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //设置头像为圆形头像
        ImageView myico = (ImageView) view.findViewById(R.id.myico);
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.myico);
        Bitmap circleBp = CircleImageUtils.createCircleImage(bmp);
        myico.setImageBitmap(circleBp);

        //获取分类的内容
        indicator = view.findViewById(R.id.home_category_indicator);
        titleContainer = (LinearLayout) view.findViewById(R.id.home_category_titles);

        //得到屏幕的宽度和高度
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）

        //设置indicator的默认位置
        indicator.setLeft(5);
        indicator.setRight(width/5-5);


        //TODO 获取linearlayout中的textview，指定点击事件

        TextView recommend = (TextView) view.findViewById(R.id.home_category_recommend);
        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(0);
                indicator.setLeft(5);
                indicator.setRight(width/5-5);
            }
        });
        TextView video = (TextView) view.findViewById(R.id.home_category_video);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(1);
                indicator.setLeft(width*1/5+5);
                indicator.setRight(width*2/5-5);
            }
        });
        TextView text = (TextView) view.findViewById(R.id.home_category_text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(3);
                indicator.setLeft(width*3/5+5);
                indicator.setRight(width*4/5-5);

            }
        });
        TextView image = (TextView) view.findViewById(R.id.home_category_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(2);
                indicator.setLeft(width*2/5+5);
                indicator.setRight(width*3/5-5);
            }
        });



        //进行刷新图片的获取的操作
        ImageView imgRefresh = (ImageView) view.findViewById(R.id.home_refresh_btn);
        imgRefresh.setOnClickListener(this);
        //获取viewpager设置内部分类列表
        pager = (ViewPager) view.findViewById(R.id.home_pager);

        //对viewpager设置监听器
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        indicator.setLeft(5);
                        indicator.setRight(width/5-5);
                        break;
                    case 1:
                        indicator.setLeft(width*1/5+5);
                        indicator.setRight(width*2/5-5);
                        break;
                    case 2:
                        indicator.setLeft(width*2/5+5);
                        indicator.setRight(width*3/5-5);
                        break;
                    case 3:
                        indicator.setLeft(width*3/5+5);
                        indicator.setRight(width*4/5-5);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //！！！
        //如果在fragment内部，使用viewpager，这个viewpager又需要加载fragment，那么这个fragmentpageradapter在
        //构造的时候，必须指定getChildFragmentManger
        List<Fragment> fragments = new ArrayList<>();
        //推荐
        ContentListFragment recommendFragment = new ContentListFragment();
        Bundle args1 = new Bundle();
        args1.putString(Constants.ARG_CONTENT_LIST_TYPE, Constants.CONTENT_TYPE_ESSAY_RECOMMOND);
        recommendFragment.setArguments(args1);
        fragments.add(recommendFragment);
        //视频
        ContentListFragment movieFragment = new ContentListFragment();
        Bundle args2 = new Bundle();
        args2.putString(Constants.ARG_CONTENT_LIST_TYPE, Constants.CONTENT_TYPE_ESSAY_VEDIO);
        movieFragment.setArguments(args2);
        fragments.add(movieFragment);
        //图片
        ContentListFragment imageFragment = new ContentListFragment();
        Bundle args3 = new Bundle();
        args3.putString(Constants.ARG_CONTENT_LIST_TYPE, Constants.CONTENT_TYPE_ESSAY_IMAGE);
        imageFragment.setArguments(args3);
        fragments.add(imageFragment);
        //段子
        ContentListFragment textFragment = new ContentListFragment();
        Bundle args4 = new Bundle();
        args4.putString(Constants.ARG_CONTENT_LIST_TYPE, Constants.CONTENT_TYPE_ESSAY_TEXT);
        textFragment.setArguments(args4);
        fragments.add(textFragment);

        CommonFragmentAdapter adapter = new CommonFragmentAdapter(getChildFragmentManager(), fragments);
        pager.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClick(View view) {
        //旋转imageview
        int id = view.getId();
        if (id == R.id.home_refresh_btn) {
            Animation an = view.getAnimation();
            if (an != null) {
                view.clearAnimation();
            } else {
                view.startAnimation(animation);
            }
        }
    }
}