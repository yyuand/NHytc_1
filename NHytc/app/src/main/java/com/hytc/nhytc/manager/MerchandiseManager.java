package com.hytc.nhytc.manager;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.MarketListAdapter;
import com.hytc.nhytc.domain.Merchandise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/2/10.
 */
public class MerchandiseManager {
    private Activity activity;
    private MarketListAdapter adapter;
    private ProgressBar probar;
    private ArrayList<Merchandise> items;
    private Merchandise item;
    private ListView listView;
    private AbPullToRefreshView abpull;

    private String starttime;
    private String endtime;

    public MerchandiseManager(Activity activity,ProgressBar probar,ListView listView,AbPullToRefreshView abpull) {
        this.activity = activity;
        this.probar = probar;
        this.listView = listView;
        this.abpull = abpull;
        this.items = new ArrayList<>();
    }

    /**
     * 或许商品信息
     * @param topic 通过topic值为 101 则是查询所有，否则则是查询不同类型商品
     *              其它具体值代表什么类目参考MerchandisePublishActivity里的creatDialog方法
     */
    public void firstgetmerchandise(Integer topic){
        probar.setVisibility(View.VISIBLE);
        BmobQuery<Merchandise> query = new BmobQuery<>();
        query.setLimit(10);
        query.order("-createdAt");
        query.include("author");
        if(topic != 101) {
            query.addWhereEqualTo("type", topic);
        }
        boolean isCache = query.hasCachedResult(activity,Merchandise.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(2));//此表示缓存2天*/
        query.findObjects(activity, new FindListener<Merchandise>() {
            @Override
            public void onSuccess(List<Merchandise> list) {
                probar.setVisibility(View.GONE);
                if(list.size() == 0){
                    activity.findViewById(R.id.nothing).setVisibility(View.VISIBLE);
                }else {
                    starttime = list.get(0).getCreatedAt();
                    endtime = list.get(list.size() - 1).getCreatedAt();
                    adapter = new MarketListAdapter(activity, list);
                    listView.setAdapter(adapter);
                    listView.setDividerHeight(0);
                }
            }

            @Override
            public void onError(int i, String s) {
                probar.setVisibility(View.GONE);
                if(i != 9009){
                    Toast.makeText(activity, "连接失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                    activity.findViewById(R.id.nothing).setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public void onHeadFresh(Integer topic){
        BmobQuery<Merchandise> query = new BmobQuery<>();
        query.setLimit(10);
        query.order("-createdAt");
        query.include("author");
        if(topic != 101) {
            query.addWhereEqualTo("type",topic);
        }
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(activity, new FindListener<Merchandise>() {
            @Override
            public void onSuccess(List<Merchandise> list) {
                starttime = list.get(0).getCreatedAt();
                endtime = list.get(list.size() - 1).getCreatedAt();
                adapter.refreshitems(list);
                abpull.onHeaderRefreshFinish();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(activity, "连接失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                abpull.onHeaderRefreshFinish();
            }
        });
    }

    public void onFootFresh(Integer topic){
        BmobQuery<Merchandise> query = new BmobQuery<>();
        query.setLimit(10);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date  = null;
        try {
            date = sdf.parse(endtime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        query.addWhereLessThan("createdAt", new BmobDate(date));
        query.order("-createdAt");
        query.include("author");
        if(topic != 101) {
            query.addWhereEqualTo("type", topic);
        }
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(activity, new FindListener<Merchandise>() {
            @Override
            public void onSuccess(List<Merchandise> list) {
                if(list.size() == 0){
                    Toast.makeText(activity, "亲，已经显示全部内容啦~", Toast.LENGTH_SHORT).show();
                }else {
                    endtime = list.get(list.size() - 1).getCreatedAt();
                    adapter.additems(list);
                }
                abpull.onFooterLoadFinish();
            }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(activity, "连接失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                abpull.onFooterLoadFinish();
            }
        });
    }







}
