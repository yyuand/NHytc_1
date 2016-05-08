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
import com.hytc.nhytc.manager.LostBackMyManager;

/**
 * Created by Administrator on 2016/2/8.
 */
public class LostMyActivity extends Activity {
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
    private LostBackMyManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lost);
        inittitle();
        initwigit();
    }

    private void initwigit() {
        listView = (ListView) this.findViewById(R.id.lv_my_lost);
        abpull = (AbPullToRefreshView) this.findViewById(R.id.abpull_lost_my);
        progressBar = (ProgressBar) this.findViewById(R.id.wait_pro_my_lost);
        manager = new LostBackMyManager(this,progressBar,listView,abpull);
        manager.firstgetlost();
        abpull.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                manager.onHeadFresh();
            }
        });
        abpull.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                manager.onFootFresh();
            }
        });
    }


    private void inittitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);
        listView = (ListView) this.findViewById(R.id.lv_my_lost);

        titlename.setText("我发布的失物");
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
