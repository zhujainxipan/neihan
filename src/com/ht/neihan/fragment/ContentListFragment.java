package com.ht.neihan.fragment;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ht.neihan.Constants;
import com.ht.neihan.R;
import com.ht.neihan.adapter.ContentListAdapter;
import com.ht.neihan.client.ClientAPI;
import com.ht.neihan.model.CommonEssay;
import com.ht.neihan.model.EssayListData;
import com.ht.neihan.model.ImageEssay;
import com.ht.neihan.model.ResponseData;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by annuo on 2015/5/26.
 */
public class ContentListFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2<ListView> {

    /**
     * 显示的列表内容是什么
     */
    private String contentType;
    private ContentListAdapter adapter;

    //用于adapter展现的list，包含的就是内涵段子
    private List<CommonEssay> essayList;
    private long maxTime;
    private long minTime;
    private PullToRefreshListView pullToRefreshListView;
    private Animation animation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载点赞+1的动画
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.nn);
        essayList = new ArrayList<CommonEssay>();
        adapter = new ContentListAdapter(getActivity(), essayList);

        //实现contentlistadapter中的点赞+1的效果
        adapter.setDigCallbacks(new ContentListAdapter.DigCallbacks() {
            @Override
            public void onClick(ImageView imageView, TextView textView) {
                textView.setVisibility(View.VISIBLE);
                textView.startAnimation(animation);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        textView.setVisibility(View.GONE);
                    }
                }, 1000);
                imageView.setImageResource(R.drawable.ic_digg_pressed);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contentlist, container, false);
        //获取要加载的数据的类型，是段子还是视频还是图片
        Bundle args = getArguments();
        if (args != null) {
            contentType = args.getString(Constants.ARG_CONTENT_LIST_TYPE);
        }

        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.content_list_view);
        //更新adapter的设计，来支持数据的加载
        pullToRefreshListView.setAdapter(adapter);
        //下拉刷新和上拉加载
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(this);


        return view;
    }


    /**
     * fragment显示
     */
    @Override
    public void onResume() {
        super.onResume();
        EssayListTask task = new EssayListTask();
        if (minTime > 0) {
            task.execute(contentType, "30", "1", Long.toString(minTime));
        } else {
            task.execute(contentType, "30");
        }

    }

    /**
     * 下拉刷新
     *
     * @param refreshView
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        //todo 根据上一次加载的最新的数据，
        EssayListTask task = new EssayListTask();
        task.execute(contentType, "20", "1", Long.toString(minTime));
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        EssayListTask task = new EssayListTask();
        task.execute(contentType, "20", "2", Long.toString(maxTime));
    }


    private class EssayListTask extends AsyncTask<String, Void, JSONObject> {
        //存储当前当前是下拉还是加载更多
        private int upOrDown;

        @Override
        protected JSONObject doInBackground(String... params) {
            //给参数赋予一些默认值
            String contentType = "";
            String count = "30";
            String loadType = "0";
            String time = "0";
            if (params != null) {
                if (params.length > 0) {
                    contentType = params[0];
                    Log.d("cont", contentType);
                    if (params.length > 1) {
                        count = params[1];
                        if (params.length > 2) {
                            loadType = params[2];
                            if (params.length > 3) {
                                time = params[3];
                            }
                        }
                    }
                }
            }

            int cc = Integer.parseInt(count);
            int lt = Integer.parseInt(loadType);

            //记录当前的请求是哪种方式，是刷新呢还是加载更多
            upOrDown = lt;

            JSONObject jsonObject = ClientAPI.getEssayList(contentType, cc, lt, Integer.parseInt(time));
            //TODO 实际的版本需要将object解析成实际的列表数据
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            //关闭刷新的操作
            pullToRefreshListView.onRefreshComplete();
            if (jsonObject != null) {
                try {
                    String message = jsonObject.getString("message");
                    ResponseData responseData = new ResponseData();
                    //解析网络返回来的数据
                    responseData.parseJSON(jsonObject);
                    //获取数据、更新ui
                    EssayListData data = responseData.getData();
                    if (data != null) {
                        //maxtime是为了加载旧的数据，因此时间上应该取最小值
                        long l = data.getMaxTime();
                        if (maxTime == 0) {
                            maxTime = l;
                        } else {
                            if (l < maxTime) {
                                maxTime = l;
                            }
                        }
                        //mintime是为了加载最新的数据，因此时间上应该取最大的值
                        l = data.getMinTime();
                        if (minTime == 0) {
                            minTime = l;
                        } else {
                            if (l > minTime) {
                                minTime = l;
                            }
                        }
                        //获取所有的段子信息
                        List<CommonEssay> commonEssays = data.getData();
                        //显示段子列表,加载更多时加载在最下面，刷新时加载到最上面
                        if (commonEssays != null) {
                            if (!commonEssays.isEmpty()) {
                                if (upOrDown == 2) {
                                    essayList.addAll(commonEssays);
                                } else {
                                    essayList.addAll(0, commonEssays);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}