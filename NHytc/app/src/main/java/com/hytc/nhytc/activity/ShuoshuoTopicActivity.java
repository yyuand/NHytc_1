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
import com.hytc.nhytc.manager.ShuoshuoTopicActivityManager;


/**
 * Created by Administrator on 2015/8/15.
 */
public class ShuoshuoTopicActivity extends Activity{
    /**
     * title
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;

    private ShuoshuoTopicActivityManager manager;
    private ListView listView;

    private AbPullToRefreshView abpull;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shuoshuotopic);
        initWidget();
        inittitle();
        manager = new ShuoshuoTopicActivityManager(this,progressBar,listView,abpull);
        manager.firstGetShuo(gettopic());
    }

    private int gettopic() {
        Intent intent = getIntent();
        int topic = intent.getIntExtra("shuoshuo", 0);
        return topic;
    }

    private void inittitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        switch (gettopic()){
            case 0:
                titlename.setText("无话题");
                break;
            case 1:
                titlename.setText("表情帝");
                break;
            case 2:
                titlename.setText("宿舍奇葩");
                break;
            case 3:
                titlename.setText("结伴回宿舍");
                break;
            case 4:
                titlename.setText("敢不敢再嗅点");
                break;
            case 5:
                titlename.setText("逗逼搞笑");
                break;
            case 6:
                titlename.setText("我要吐槽");
                break;
        }

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


    private void initWidget() {

        abpull = (AbPullToRefreshView) this.findViewById(R.id.ab_pull_shuo_topic);
        progressBar = (ProgressBar) this.findViewById(R.id.wait_pro_shuo_topic);
        listView = (ListView) this.findViewById(R.id.lv_shuoshuotopic);

        abpull.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                manager.onHeadLoad(gettopic());
            }
        });
        abpull.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                manager.onFootLoad(gettopic());
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
