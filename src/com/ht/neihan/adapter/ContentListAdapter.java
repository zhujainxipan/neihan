package com.ht.neihan.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.ht.neihan.R;
import com.ht.neihan.client.ImageLoadTask;
import com.ht.neihan.fragment.ContentListFragment;
import com.ht.neihan.model.*;

import java.util.List;

/**
 * Created by annuo on 2015/5/26.
 */

/**
 * 内涵段子列表展现的adapter
 */
public class ContentListAdapter extends BaseAdapter {
    private Context context;
    private List<CommonEssay> essayList;
    private LayoutInflater inflater;
    private DigCallbacks digCallbacks;


    public ContentListAdapter(Context context, List<CommonEssay> essayList) {
        this.context = context;
        this.essayList = essayList;
        if (context != null) {
            inflater = LayoutInflater.from(context);
        }

    }

    @Override
    public int getCount() {
        int ret = 0;
        if (essayList != null) {
            ret = essayList.size();
        }
        return ret;
    }

    @Override
    public int getViewTypeCount() {
        //段子、图片、广告、登陆
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        //根据当前的数据，判断类型，并且返回
        CommonEssay essay = essayList.get(position);
        int ret = 0;
        if (essay instanceof MovieEssay) {
            ret = 1;
        } else if (essay instanceof ImageEssay) {
            ret = 2;
        } else
            ret = 0;
        return ret;
    }

    @Override
    public Object getItem(int i) {
        Object ret = null;
        if (essayList != null) {
            ret = essayList.get(i);
        }
        return ret;
    }

    @Override
    public long getItemId(int i) {
        long ret = 0;
        if (essayList != null) {
            CommonEssay essay = essayList.get(i);
            ret = essay.getGroupId();
        }
        return ret;
    }

    /**
     * @param position
     * @param convertView
     * @param viewGroup
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        TextViewHolder textViewHolder = null;
        ImageViewHolder imageViewHolder = null;
        MovieViewHolder movieViewHolder = null;
        CommonEssay essay = essayList.get(position);
        //因为adapter重写了getViewTypeCount以及getItemViewCount
        //并且getViewTypeCount返回值大于1，代表需要创建多布局内容
        if (essay instanceof ImageEssay) {
            //应该显示图片
            convertView = bindImage(position, convertView);
            Log.d("zhixingle", "image");
        }
        //essay instanceof MovieEssay修改了
        else if (essay instanceof MovieEssay) {
            //应该显示视频的内容
            convertView = bindMovie(position, convertView);
            Log.d("zhixingle", "movie");
        } else {
            //应该显示文本的内容
            convertView = bindText(position, convertView);
            Log.d("zhixingle", "text");
        }
        return convertView;
    }

    private View bindMovie(int position, View convertView) {
        MovieViewHolder movieViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_movie_essay, null);
            movieViewHolder = new MovieViewHolder();
            movieViewHolder.txtcommentCount = (TextView) convertView.findViewById(R.id.item_essay_comment_count);
            movieViewHolder.txtBuryCount = (TextView) convertView.findViewById(R.id.item_essay_bury_count);
            movieViewHolder.txtContent = (TextView) convertView.findViewById(R.id.item_essay_content);
            movieViewHolder.txtFavorityCount = (TextView) convertView.findViewById(R.id.item_essay_favority_count);
            movieViewHolder.txtName = (TextView) convertView.findViewById(R.id.item_essay_name);
            movieViewHolder.imgUserIcon = (ImageView) convertView.findViewById(R.id.item_essay_user_icon);
            convertView.setTag(movieViewHolder);
        } else {
            movieViewHolder = (MovieViewHolder) convertView.getTag();
        }
        User user = essayList.get(position).getUser();
        //在用户信息设置之前，清楚复用倒置的信息错乱
        movieViewHolder.imgUserIcon.setImageResource(R.drawable.ic_launcher);
        movieViewHolder.txtFavorityCount.setText(essayList.get(position).getFavoriteCount() + "");
        movieViewHolder.txtName.setText(essayList.get(position).getUser().getUserName());
        movieViewHolder.txtContent.setText(essayList.get(position).getContent());
        movieViewHolder.txtBuryCount.setText(essayList.get(position).getBuryCount() + "");
        movieViewHolder.txtcommentCount.setText(essayList.get(position).getCommentCount() + "");

        //设置用户的头像，需要开启异步任务
        String avataUrl = user.getAvatarUrl();
        //为了避免图片错乱的问题，需要给每一次加载图片时，imageview设置加载网址的tag
        //通过tag传递给task，进行检查
        movieViewHolder.imgUserIcon.setTag(avataUrl);
        if (avataUrl != null) {
            //todo 加载图片，并设置到imageview
            ImageLoadTask task = new ImageLoadTask(movieViewHolder.imgUserIcon, "userico");
            task.execute(avataUrl);
        }

        //点赞+1效果实现
        //通过接口实现吧,不太好，因为呢，这里一旦点赞，就需要联网提交给服务器的，但是现在还没有提交的接口
        //所以混乱错位就混乱错位吧
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ite_essay_dig);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_one);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                digCallbacks.onClick(imageView, textView);
            }
        });

        return convertView;
    }

    private View bindImage(int position, View convertView) {
        ImageViewHolder imageViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_image_essay, null);
            imageViewHolder = new ImageViewHolder();
            imageViewHolder.txtcommentCount = (TextView) convertView.findViewById(R.id.item_essay_comment_count);
            imageViewHolder.txtBuryCount = (TextView) convertView.findViewById(R.id.item_essay_bury_count);
            imageViewHolder.txtContent = (TextView) convertView.findViewById(R.id.item_essay_content);
            imageViewHolder.txtFavorityCount = (TextView) convertView.findViewById(R.id.item_essay_favority_count);
            imageViewHolder.txtName = (TextView) convertView.findViewById(R.id.item_essay_name);
            imageViewHolder.imgUserIcon = (ImageView) convertView.findViewById(R.id.item_essay_user_icon);
            imageViewHolder.imgContent = (ImageView) convertView.findViewById(R.id.item_essay_image);
            convertView.setTag(imageViewHolder);
        } else {
            imageViewHolder = (ImageViewHolder) convertView.getTag();
        }
        User user = essayList.get(position).getUser();
        //在用户信息设置之前，清楚复用倒置的信息错乱
        imageViewHolder.imgUserIcon.setImageResource(R.drawable.ic_launcher);
        imageViewHolder.txtFavorityCount.setText(essayList.get(position).getFavoriteCount() + "");
        imageViewHolder.txtName.setText(essayList.get(position).getUser().getUserName());
        imageViewHolder.txtContent.setText(essayList.get(position).getContent());
        imageViewHolder.txtBuryCount.setText(essayList.get(position).getBuryCount() + "");
        imageViewHolder.txtcommentCount.setText(essayList.get(position).getCommentCount() + "");
        //设置用户的头像，需要开启异步任务
        String avataUrl = user.getAvatarUrl();
        //为了避免图片错乱的问题，需要给每一次加载图片时，imageview设置加载网址的tag
        //通过tag传递给task，进行检查
        imageViewHolder.imgUserIcon.setTag(avataUrl);
        if (avataUrl != null) {
            //todo 加载图片，并设置到imageview
            ImageLoadTask task = new ImageLoadTask(imageViewHolder.imgUserIcon, "userico");
            task.execute(avataUrl);
        }

        imageViewHolder.imgContent.setImageResource(R.drawable.ic_launcher);
        //取图片段子的内容
        ImageEssay imageEssay = (ImageEssay) essayList.get(position);
        ResourceInfo middleRes = imageEssay.getMiddleImages();
        if (middleRes != null) {
            //得到大图的url链接地址
            List<String> urlList = middleRes.getUrlList();
            String imgUrl = urlList.get(0);
            if (imgUrl != null) {
                //todo 加载图片，并设置到imageview
                imageViewHolder.imgContent.setTag(imgUrl);
                ImageLoadTask task = new ImageLoadTask(imageViewHolder.imgContent);
                task.execute(imgUrl);
            }
        }

        //点赞+1效果实现
        //通过接口实现吧
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ite_essay_dig);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_one);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                digCallbacks.onClick(imageView, textView);
            }
        });

        return convertView;
    }


    /**
     * 用来加载和显示文本段子
     *
     * @param position
     * @param convertView
     * @return
     */
    private View bindText(int position, View convertView) {
        TextViewHolder textViewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_text_essay, null);
            textViewHolder = new TextViewHolder();
            textViewHolder.txtcommentCount = (TextView) convertView.findViewById(R.id.item_essay_comment_count);
            textViewHolder.txtBuryCount = (TextView) convertView.findViewById(R.id.item_essay_bury_count);
            textViewHolder.txtContent = (TextView) convertView.findViewById(R.id.item_essay_content);
            textViewHolder.txtFavorityCount = (TextView) convertView.findViewById(R.id.item_essay_favority_count);
            textViewHolder.txtName = (TextView) convertView.findViewById(R.id.item_essay_name);
            textViewHolder.imgUserIcon = (ImageView) convertView.findViewById(R.id.item_essay_user_icon);
            convertView.setTag(textViewHolder);
        } else {
            textViewHolder = (TextViewHolder) convertView.getTag();
        }

        User user = essayList.get(position).getUser();
        //在用户信息设置之前，清楚复用倒置的信息错乱
        textViewHolder.imgUserIcon.setImageResource(R.drawable.ic_launcher);
        textViewHolder.txtFavorityCount.setText(essayList.get(position).getFavoriteCount() + "");
        textViewHolder.txtName.setText(user.getUserName());
        textViewHolder.txtContent.setText(essayList.get(position).getContent());
        textViewHolder.txtBuryCount.setText(essayList.get(position).getBuryCount() + "");
        textViewHolder.txtcommentCount.setText(essayList.get(position).getCommentCount() + "");
        //设置用户的头像，需要开启异步任务
        String avataUrl = user.getAvatarUrl();
        //为了避免图片错乱的问题，需要给每一次加载图片时，imageview设置加载网址的tag
        //通过tag传递给task，进行检查
        textViewHolder.imgUserIcon.setTag(avataUrl);
        if (avataUrl != null) {
            //todo 加载图片，并设置到imageview
            ImageLoadTask task = new ImageLoadTask(textViewHolder.imgUserIcon, "userico");
            task.execute(avataUrl);
        }

        //点赞+1效果实现
        //通过接口实现吧
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ite_essay_dig);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_one);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                digCallbacks.onClick(imageView, textView);
            }
        });

        return convertView;
    }

    //点赞加1的接口
    public interface DigCallbacks {
        void onClick(ImageView imageView, TextView textView);
    }

    public void setDigCallbacks(DigCallbacks digCallbacks) {
        this.digCallbacks = digCallbacks;
    }


    private class TextViewHolder {
        public TextView txtName;
        public TextView txtContent;
        public TextView txtBuryCount;
        public TextView txtFavorityCount;
        public TextView txtcommentCount;
        //用户头像
        public ImageView imgUserIcon;
        //顶过的图片
        public ImageView digImageView;
        //踩过的图片
        public ImageView buryImageView;

    }

    /**
     * 图片的holder
     */
    private class ImageViewHolder extends TextViewHolder {
        public ImageView middleView;
        public ImageView imgContent;
    }

    private class MovieViewHolder extends TextViewHolder {

    }


}
