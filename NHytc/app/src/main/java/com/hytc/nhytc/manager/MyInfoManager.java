package com.hytc.nhytc.manager;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.MyInfoItemAdapter;
import com.hytc.nhytc.adapter.MyMerchandiseAcivityAdapter;
import com.hytc.nhytc.domain.Merchandise;
import com.hytc.nhytc.domain.MyInfo;
import com.hytc.nhytc.domain.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/3/1.
 */
public class MyInfoManager {
    private Activity activity;
    private MyInfoItemAdapter adapter;
    private ProgressBar probar;
    private ArrayList<MyInfo> items;
    private ListView listView;
    private AbPullToRefreshView abpull;

    private String endtime;

    public MyInfoManager(Activity activity,ProgressBar probar,ListView listView,AbPullToRefreshView abpull) {
        this.activity = activity;
        this.probar = probar;
        this.listView = listView;
        this.abpull = abpull;
    }

    public void firstGetMyinfo(){
        probar.setVisibility(View.VISIBLE);
        BmobQuery<MyInfo> query = new BmobQuery<>();
        query.setLimit(10);
        query.addWhereEqualTo("replyid", BmobUser.getCurrentUser(activity, User.class).getObjectId());
        query.order("-createdAt");
        query.include("author");
        /*boolean isCache = query.hasCachedResult(activity,MyInfo.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(3));//此表示缓存3天*/
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(activity, new FindListener<MyInfo>() {
            @Override
            public void onSuccess(List<MyInfo> list) {
                probar.setVisibility(View.GONE);
                if(list.size() == 0){
                    activity.findViewById(R.id.nothing).setVisibility(View.VISIBLE);
                }else {
                    endtime = list.get(list.size() - 1).getCreatedAt();
                    adapter = new MyInfoItemAdapter(activity, list);
                    listView.setAdapter(adapter);
                    listView.setDividerHeight(0);
                }
            }

            @Override
            public void onError(int i, String s) {
                probar.setVisibility(View.GONE);
                activity.findViewById(R.id.nothing).setVisibility(View.VISIBLE);
                if(i != 9009){
                    Toast.makeText(activity, "连接失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void onHeadFreshMy(){
        BmobQuery<MyInfo> query = new BmobQuery<>();
        query.setLimit(10);
        query.addWhereEqualTo("replyid", BmobUser.getCurrentUser(activity, User.class).getObjectId());
        query.order("-createdAt");
        query.include("author");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(activity, new FindListener<MyInfo>() {
            @Override
            public void onSuccess(List<MyInfo> list) {
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

    public void onFootFreshMy(){
        BmobQuery<MyInfo> query = new BmobQuery<>();
        query.setLimit(10);
        query.addWhereEqualTo("replyid", BmobUser.getCurrentUser(activity, User.class).getObjectId());
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
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(activity, new FindListener<MyInfo>() {
            @Override
            public void onSuccess(List<MyInfo> list) {
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
