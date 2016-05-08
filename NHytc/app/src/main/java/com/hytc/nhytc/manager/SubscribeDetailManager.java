package com.hytc.nhytc.manager;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.SubscribeDetailAdapter;
import com.hytc.nhytc.domain.Subscribe;
import com.hytc.nhytc.domain.SubscribeDetail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/3/4.
 */
public class SubscribeDetailManager {
    private Activity activity;
    private SubscribeDetailAdapter adapter;
    private Subscribe subscribe;
    private ProgressBar probar;
    private ListView listView;
    private AbPullToRefreshView abpull;

    private Integer endsort = 1;
    private Float endmoney = (float) 0;

    public SubscribeDetailManager(Activity activity, Subscribe subscribe, ProgressBar probar, ListView listView, AbPullToRefreshView abpull) {
        this.activity = activity;
        this.subscribe = subscribe;
        this.probar = probar;
        this.listView = listView;
        this.abpull = abpull;
    }

    public void firstGetsubscribedetail(){
        probar.setVisibility(View.VISIBLE);
        BmobQuery<SubscribeDetail> query = new BmobQuery<>();
        query.setLimit(15);
        query.addWhereEqualTo("is_show", true);
        query.addWhereEqualTo("type",subscribe);
        if(subscribe.getSub_number().equals(45)){
            query.order("-money");
        }else {
            query.order("-sort");
        }

        boolean isCache = query.hasCachedResult(activity,SubscribeDetail.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(3));//此表示缓存3天
        query.findObjects(activity, new FindListener<SubscribeDetail>() {
            @Override
            public void onSuccess(List<SubscribeDetail> list) {
                probar.setVisibility(View.GONE);
                if (list.size() == 0) {
                    activity.findViewById(R.id.nothing).setVisibility(View.VISIBLE);
                } else {
                    if(subscribe.getSub_number().equals(45)){
                        endmoney = list.get(list.size()-1).getMoney();
                    }else {
                        endsort = list.get(list.size()-1).getSort();
                    }
                    adapter = new SubscribeDetailAdapter(activity, list);
                    probar.setVisibility(View.GONE);
                    listView.setAdapter(adapter);
                    listView.setDividerHeight(0);
                }
            }

            @Override
            public void onError(int i, String s) {
                probar.setVisibility(View.GONE);
                activity.findViewById(R.id.nothing).setVisibility(View.VISIBLE);
                if (i != 9009) {
                    Toast.makeText(activity, "访问失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void onHeadLoad(){
        BmobQuery<SubscribeDetail> query = new BmobQuery<>();
        query.setLimit(15);
        query.addWhereEqualTo("is_show", true);
        query.addWhereEqualTo("type", subscribe);
        if(subscribe.getSub_number().equals(45)){
            query.order("-money");
        }else {
            query.order("-sort");
        }
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(3));//此表示缓存3天
        query.findObjects(activity, new FindListener<SubscribeDetail>() {
            @Override
            public void onSuccess(List<SubscribeDetail> list) {
                if(subscribe.getSub_number().equals(45)){
                    endmoney = list.get(list.size()-1).getMoney();
                }else {
                    endsort = list.get(list.size()-1).getSort();
                }
                adapter.refreshitems(list);
                abpull.onHeaderRefreshFinish();

            }

            @Override
            public void onError(int i, String s) {
                probar.setVisibility(View.GONE);
                Toast.makeText(activity, "访问失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                abpull.onHeaderRefreshFinish();
            }
        });

    }


    public void onFootLoad() {

        BmobQuery<SubscribeDetail> query = new BmobQuery<>();
        query.setLimit(15);
        query.addWhereEqualTo("is_show", true);
        query.addWhereEqualTo("type", subscribe);

        if(subscribe.getSub_number().equals(45)){
            query.addWhereLessThan("money", endmoney);
            query.order("-money");
        }else {
            /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date  = null;
            try {
                date = sdf.parse(endtime);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            query.addWhereLessThan("sort", endsort);
            query.order("-sort");
        }
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(3));//此表示缓存3天
        query.findObjects(activity, new FindListener<SubscribeDetail>() {
            @Override
            public void onSuccess(List<SubscribeDetail> list) {
                if(list.size() == 0){
                    Toast.makeText(activity, "亲，已经显示全部内容啦~", Toast.LENGTH_SHORT).show();
                }else {
                    if(subscribe.getSub_number().equals(45)){
                        endmoney = list.get(list.size()-1).getMoney();
                    }else {
                        endsort = list.get(list.size()-1).getSort();
                    }
                    adapter.additems(list);
                }
                abpull.onFooterLoadFinish();

            }

            @Override
            public void onError(int i, String s) {
                probar.setVisibility(View.GONE);
                Toast.makeText(activity, "访问失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                abpull.onFooterLoadFinish();
            }
        });

    }





}
