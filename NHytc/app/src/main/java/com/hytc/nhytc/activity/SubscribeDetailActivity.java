package com.hytc.nhytc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.domain.Subscribe;
import com.hytc.nhytc.manager.SubscribeDetailManager;

/**
 * Created by Administrator on 2016/3/4.
 */
public class SubscribeDetailActivity extends Activity {
    /**
     * title
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;

    private Intent intent;
    private Subscribe subscribe;

    private ListView listView;
    private AbPullToRefreshView abpull;
    private ProgressBar progressBar;
    private SubscribeDetailManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_detail);
        initTitle();
        initwidget();
    }

    private void initwidget() {
        listView = (ListView) this.findViewById(R.id.lv_detail_subscribe);
        abpull = (AbPullToRefreshView) this.findViewById(R.id.abpull_detail_subscribe);
        progressBar = (ProgressBar) this.findViewById(R.id.wait_pro_detail_subscribe);
        manager = new SubscribeDetailManager(this,subscribe,progressBar,listView,abpull);
        manager.firstGetsubscribedetail();

        abpull.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                manager.onHeadLoad();
            }
        });

        abpull.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                manager.onFootLoad();
            }
        });
    }

    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        intent = getIntent();
        subscribe = (Subscribe) intent.getSerializableExtra("subscribe");

        titlename.setText(subscribe.getSub_name());

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivinfo.setVisibility(View.GONE);
        ivmore.setVisibility(View.GONE);
    }

}
