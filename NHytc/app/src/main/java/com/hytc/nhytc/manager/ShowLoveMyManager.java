package com.hytc.nhytc.manager;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.ShowLoveMyActivityAdapter;
import com.hytc.nhytc.dbDAO.ApproveShuoDBDao;
import com.hytc.nhytc.domain.ForResulltMsg;
import com.hytc.nhytc.domain.ShowLove;
import com.hytc.nhytc.domain.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/2/8.
 */
public class ShowLoveMyManager {
    private Activity activity;
    private ShowLoveMyActivityAdapter adapter;
    private ProgressBar probar;
    private List<ShowLove> items;
    private ShowLove item;
    private ListView listView;
    private AbPullToRefreshView abpull;

    private String starttime;
    private String endtime;

    private ApproveShuoDBDao dbDao;

    public ShowLoveMyManager(Activity activity,ProgressBar probar,ListView listView,AbPullToRefreshView abpull) {
        this.activity = activity;
        this.probar = probar;
        this.listView = listView;
        this.abpull = abpull;
        this.dbDao = new ApproveShuoDBDao(activity);
    }

    public void firstGetShowLove(){
        probar.setVisibility(View.VISIBLE);
        BmobQuery<ShowLove> query = new BmobQuery<>();
        query.setLimit(10);
        query.addWhereEqualTo("author", BmobUser.getCurrentUser(activity, User.class));
        query.order("-createdAt");
        query.include("author");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(2));//此表示缓存2天
        query.findObjects(activity, new FindListener<ShowLove>() {
            @Override
            public void onSuccess(List<ShowLove> list) {

                starttime = list.get(0).getCreatedAt();
                endtime = list.get(list.size()-1).getCreatedAt();
                adapter = new ShowLoveMyActivityAdapter(activity,list);
                probar.setVisibility(View.GONE);
                listView.setAdapter(adapter);
                listView.setDividerHeight(0);
            }

            @Override
            public void onError(int i, String s) {
                probar.setVisibility(View.GONE);
                activity.findViewById(R.id.nothing).setVisibility(View.VISIBLE);
                if(i != 9009){
                    Toast.makeText(activity, "访问失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    public void onHeadLoad(){

        BmobQuery<ShowLove> query = new BmobQuery<>();
        query.setLimit(10);
        query.addWhereEqualTo("author", BmobUser.getCurrentUser(activity, User.class));
        query.order("-createdAt");
        query.include("author");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(activity, new FindListener<ShowLove>() {
            @Override
            public void onSuccess(List<ShowLove> list) {
                starttime = list.get(0).getCreatedAt();
                endtime = list.get(list.size() - 1).getCreatedAt();
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

        BmobQuery<ShowLove> query = new BmobQuery<>();
        query.setLimit(10);
        query.addWhereEqualTo("author", BmobUser.getCurrentUser(activity, User.class));
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
        query.findObjects(activity, new FindListener<ShowLove>() {
            @Override
            public void onSuccess(List<ShowLove> list) {
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
                probar.setVisibility(View.GONE);
                Toast.makeText(activity, "访问失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                abpull.onFooterLoadFinish();
            }
        });

    }

    public void upData(ForResulltMsg msg){
        adapter.upData(msg);
    }

}
