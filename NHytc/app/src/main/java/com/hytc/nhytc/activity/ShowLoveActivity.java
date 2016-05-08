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
import com.hytc.nhytc.manager.ShowLoveManager;

/**
 * Created by Administrator on 2015/8/16.
 */
public class ShowLoveActivity extends Activity {
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;


    private ListView listView;
    private ProgressBar wait;
    private AbPullToRefreshView abptr;
    private ShowLoveManager manager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showlove);
        initTitle();
        initlist();
    }


    private void initlist(){
        listView = (ListView) this.findViewById(R.id.lv_showlove_acrivity);
        abptr = (AbPullToRefreshView) this.findViewById(R.id.abptr_show_love);
        wait = (ProgressBar) this.findViewById(R.id.wait_pro);
        manager = new ShowLoveManager(this,wait,listView,abptr);
        manager.firstGetShowLove();
        /**
         * 为上拉刷新绑定监听
         */
        abptr.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                manager.onHeadLoad();
            }
        });
        /**
         * 为上拉刷新绑定监听
         */
        abptr.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
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

        titlename.setText("表白墙");
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivinfo.setVisibility(View.GONE);
        ivmore.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            manager.upData(new ForResulltMsg(data.getBooleanExtra("appstatus", false), data.getIntExtra("position", 0), data.getStringExtra("comcounts"), data.getStringExtra("appcounts")));
        }
    }
}
