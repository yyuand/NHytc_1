package com.hytc.nhytc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.domain.ForResulltMsg;
import com.hytc.nhytc.manager.ShowLoveMyManager;

/**
 * Created by Administrator on 2016/2/8.
 */
public class ShowLoveMyActivity extends Activity {
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;


    private ListView listView;
    private AbPullToRefreshView abpullfresh;
    private ProgressBar wait;
    private ShowLoveMyManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_showlove);
        inittitle();
    }



    private void inittitle() {
        /**
         * 标题栏控件
         */
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);


        listView = (ListView) this.findViewById(R.id.lv_my_show_love);
        wait = (ProgressBar) this.findViewById(R.id.wait_pro_my_showlove);
        abpullfresh = (AbPullToRefreshView) this.findViewById(R.id.abpull_my_showlove);
        manager = new ShowLoveMyManager(this,wait,listView,abpullfresh);
        manager.firstGetShowLove();
        /**
         * 为上拉刷新绑定监听
         */
        abpullfresh.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                manager.onHeadLoad();
            }
        });
        /**
         * 为上拉刷新绑定监听
         */
        abpullfresh.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                manager.onFootLoad();
            }
        });



        titlename.setText("我的表白");
        ivinfo.setVisibility(View.GONE);
        tvinfo.setVisibility(View.GONE);
        ivmore.setVisibility(View.GONE);

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            manager.upData(new ForResulltMsg(data.getBooleanExtra("appstatus", false), data.getIntExtra("position", 0), data.getStringExtra("comcounts"), data.getStringExtra("appcounts")));
        }
    }
}
