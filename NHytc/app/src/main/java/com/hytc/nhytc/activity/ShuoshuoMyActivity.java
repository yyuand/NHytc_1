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
import com.hytc.nhytc.manager.ShuoShuoMyManager;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/1/30.
 */
public class ShuoshuoMyActivity extends Activity {
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;

    private ListView listView;
    private AbPullToRefreshView abpull;
    private ProgressBar progressBar;
    private ShuoShuoMyManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shuoshuo);
        inittitle();
        initwidget();
    }

    private void initwidget() {
        listView = (ListView) this.findViewById(R.id.lv_my_shuoshuo);
        abpull = (AbPullToRefreshView) this.findViewById(R.id.ab_pull_my_shuo);
        progressBar = (ProgressBar) this.findViewById(R.id.wait_pro_my_shuo);
        manager = new ShuoShuoMyManager(this,progressBar,listView,abpull);

        manager.firstGetShuo();

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


    private void inittitle() {
        /**
         * 标题栏控件
         */
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        titlename.setText("我的说说");
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
            Log.e("qwe", data.getBooleanExtra("appstatus", false) + "");
            Log.e("qwe",data.getIntExtra("position", 0) + "");
            Log.e("qwe",data.getStringExtra("comcounts"));
            Log.e("qwe",data.getStringExtra("appcounts"));
            manager.upData(new ForResulltMsg(data.getBooleanExtra("appstatus", false), data.getIntExtra("position", 0), data.getStringExtra("comcounts"), data.getStringExtra("appcounts")));
        }
    }
}
