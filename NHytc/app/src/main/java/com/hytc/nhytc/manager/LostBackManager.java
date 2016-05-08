package com.hytc.nhytc.manager;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.LostActivityItemAdapter;
import com.hytc.nhytc.domain.LostBack;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by Administrator on 2016/2/7.
 */
public class LostBackManager {
    private Activity activity;
    private ProgressBar probar;
    private ListView listView;
    private AbPullToRefreshView abpull;
    private LostActivityItemAdapter adapter;

    private String starttime;
    private String endtime;

    public LostBackManager(Activity activity,ProgressBar probar,ListView listView,AbPullToRefreshView abpull) {
        this.activity = activity;
        this.probar = probar;
        this.listView = listView;
        this.abpull = abpull;
    }


    public void firstgetlost(Integer topic, final Integer function,String content){
        probar.setVisibility(View.VISIBLE);
        BmobQuery<LostBack> query = new BmobQuery<>();
        query.setLimit(10);
        query.order("-createdAt");
        query.include("author");
        if(function != 30) {
            query.addWhereEqualTo("lostType", topic);
            query.addWhereEqualTo("lostfunction", function);
        }else if(!"".equals(content)){
            BmobQuery<LostBack> query1 = new BmobQuery<>();
            query1.addWhereMatches("content",content);
            BmobQuery<LostBack> query2 = new BmobQuery<>();
            query2.addWhereMatches("lostName",content);
            List<BmobQuery<LostBack>> queries = new ArrayList<BmobQuery<LostBack>>();
            queries.add(query1);
            queries.add(query2);
            query.or(queries);
        }
        /*boolean isCache = query.hasCachedResult(activity,LostBack.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(3));//此表示缓存3天*/
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(2));//此表示缓存2天
        query.findObjects(activity, new FindListener<LostBack>() {
            @Override
            public void onSuccess(List<LostBack> list) {
                probar.setVisibility(View.GONE);
                if(list.size() == 0){
                    if(function == 30){
                        Toast.makeText(activity, "没有查询到您搜索的相关内容", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(activity, "目前还没有相关内容哦~", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    starttime = list.get(0).getCreatedAt();
                    endtime = list.get(list.size() - 1).getCreatedAt();
                    adapter = new LostActivityItemAdapter(activity, list);
                    listView.setAdapter(adapter);
                    listView.setDividerHeight(0);
                }
            }

            @Override
            public void onError(int i, String s) {
                probar.setVisibility(View.GONE);
                if(i != 9009){
                    Toast.makeText(activity, "连接失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void onHeadFresh(Integer topic,Integer function,String content){
        BmobQuery<LostBack> query = new BmobQuery<>();
        query.setLimit(10);
        query.order("-createdAt");
        query.include("author");
        if(function != 30) {
            query.addWhereEqualTo("lostType", topic);
            query.addWhereEqualTo("lostfunction", function);
        }else if(!"".equals(content)){
            BmobQuery<LostBack> query1 = new BmobQuery<>();
            query1.addWhereMatches("content",content);
            BmobQuery<LostBack> query2 = new BmobQuery<>();
            query2.addWhereMatches("lostName",content);
            List<BmobQuery<LostBack>> queries = new ArrayList<BmobQuery<LostBack>>();
            queries.add(query1);
            queries.add(query2);
            query.or(queries);
        }
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(activity, new FindListener<LostBack>() {
            @Override
            public void onSuccess(List<LostBack> list) {
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

    public void onFootFresh(Integer topic,Integer function,String content) {
        BmobQuery<LostBack> query = new BmobQuery<>();
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
        if(function != 30) {
            query.addWhereEqualTo("lostType", topic);
            query.addWhereEqualTo("lostfunction", function);
        }else if(!"".equals(content)){
            BmobQuery<LostBack> query1 = new BmobQuery<>();
            query1.addWhereMatches("content",content);
            BmobQuery<LostBack> query2 = new BmobQuery<>();
            query2.addWhereMatches("lostName",content);
            List<BmobQuery<LostBack>> queries = new ArrayList<BmobQuery<LostBack>>();
            queries.add(query1);
            queries.add(query2);
            query.or(queries);
        }
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(activity, new FindListener<LostBack>() {
            @Override
            public void onSuccess(List<LostBack> list) {
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
