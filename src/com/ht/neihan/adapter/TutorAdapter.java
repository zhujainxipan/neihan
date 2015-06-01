package com.ht.neihan.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.ht.neihan.R;

import java.util.List;

/**
 * Created by annuo on 2015/5/26.
 */
public class TutorAdapter extends PagerAdapter {

    /**
     * 包含了哪些图片要显示
     */
    private List<Integer> images;

    private Context context;
    //引导页最后一页增加一个按钮，按钮点击的事件回调
    private View.OnClickListener onClickListener;

    /**
     * @param onClickListener
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public TutorAdapter(Context context, List<Integer> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (images != null) {
            ret = images.size();
        }
        return ret;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    ///////////////////////////////

    /**
     * 初始化对象
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View ret = null;
        //最后一张图片需要增加按钮
        if (position == images.size() - 1) {
            //创建布局，增加按钮点击事件
            FrameLayout layout = new FrameLayout(context);
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(images.get(position));
            //设置imageview排版的宽高:LayoutParams来指定宽高
            //放到那个容器中，就是用那个容器的xxxLayout.LayoutParams类
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            );
            imageView.setLayoutParams(lp);
            layout.addView(imageView);

            //添加右下角的按钮，进行点击启动主界面
            ImageButton button = new ImageButton(context);
            button.setBackgroundResource(R.drawable.bg_enter_main_btn);
            //指定按钮所在位置右下角
            FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.RIGHT | Gravity.BOTTOM
            );
            lp1.setMargins(0, 0, 30, 50);
            button.setLayoutParams(lp1);
            //TODO 设置按钮的点击事件
            //采用回调的形式来避免代码写死业务逻辑，提高扩展性
            button.setOnClickListener(onClickListener);

            layout.addView(button);
            ret = layout;
        } else {
            //创建图片
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(images.get(position));
            ret = imageView;
        }
        //TODO 添加ret到container
        container.addView(ret);
        return ret;
    }

    /**
     * 销毁对象
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
