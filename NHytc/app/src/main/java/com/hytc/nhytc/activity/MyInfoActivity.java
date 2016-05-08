package com.hytc.nhytc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.MyInfoItemAdapter;
import com.hytc.nhytc.manager.MyInfoManager;

/**
 * Created by Administrator on 2016/3/1.
 */
public class MyInfoActivity extends Activity {
    /**
     * title
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;

    private ListView listView;
    private AbPullToRefreshView abpull;
    private ProgressBar progressBar;
    private MyInfoItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        inittitle();
        initwidget();
    }



    private void initwidget() {
        listView = (ListView) this.findViewById(R.id.lv_activity_myinfo);
        abpull = (AbPullToRefreshView) this.findViewById(R.id.ab_pull_myinfo);
        progressBar = (ProgressBar) this.findViewById(R.id.wait_pro_myinfo);
        final MyInfoManager manager = new MyInfoManager(this,progressBar,listView,abpull);
        manager.firstGetMyinfo();
        abpull.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                manager.onHeadFreshMy();
            }
        });
        abpull.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                manager.onFootFreshMy();
            }
        });
    }


    private void inittitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        titlename.setText("我的消息");
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
}
