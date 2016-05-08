package com.hytc.nhytc.manager;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.SubscribeAdapter;
import com.hytc.nhytc.domain.ShuoShuo;
import com.hytc.nhytc.domain.Subscribe;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/3/4.
 */
public class SubscribeManager {
    private Activity activity;
    private SubscribeAdapter adapter;

    private ProgressBar probar;
    private ListView listView;
    private AbPullToRefreshView abpull;

    public SubscribeManager(Activity activity, ProgressBar probar, ListView listView, AbPullToRefreshView abpull) {
        this.activity = activity;
        this.probar = probar;
        this.listView = listView;
        this.abpull = abpull;
    }


    public void firstGetsubscribe(){
        probar.setVisibility(View.VISIBLE);
        BmobQuery<Subscribe> query = new BmobQuery<>();
        query.setLimit(30);
        query.addWhereEqualTo("is_show",true);
        query.order("-sort");
        boolean isCache = query.hasCachedResult(activity,Subscribe.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(8));//此表示缓存8天
        query.findObjects(activity, new FindListener<Subscribe>() {
            @Override
            public void onSuccess(List<Subscribe> list) {
                probar.setVisibility(View.GONE);
                if(list.size() == 0){
                    activity.findViewById(R.id.nothing_for_internet).setVisibility(View.VISIBLE);
                }else {
                    adapter = new SubscribeAdapter(activity,list);
                    probar.setVisibility(View.GONE);
                    listView.setAdapter(adapter);
                    listView.setDividerHeight(0);
                }
            }

            @Override
            public void onError(int i, String s) {
                probar.setVisibility(View.GONE);
                activity.findViewById(R.id.nothing_for_internet).setVisibility(View.VISIBLE);
                if(i != 9009){
                    Toast.makeText(activity, "访问失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void reFresh(){

        BmobQuery<Subscribe> query = new BmobQuery<>();
        query.setLimit(30);
        query.addWhereEqualTo("is_show",true);
        query.order("-sort");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(8));//此表示缓存8天
        query.findObjects(activity, new FindListener<Subscribe>() {
            @Override
            public void onSuccess(List<Subscribe> list) {
                adapter.refreshitems(list);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(activity, "访问失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
