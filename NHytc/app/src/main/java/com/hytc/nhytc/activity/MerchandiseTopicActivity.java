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
import com.hytc.nhytc.manager.MerchandiseManager;

/**
 * Created by Administrator on 2016/2/10.
 */
public class MerchandiseTopicActivity extends Activity{
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

    int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_topic_detail);
        initTitle();
        initwidget();
    }

    private void initwidget() {
        listView = (ListView) this.findViewById(R.id.lv_market_topic);
        abpull = (AbPullToRefreshView) this.findViewById(R.id.abpull_topic_mer);
        progressBar = (ProgressBar) this.findViewById(R.id.wait_pro_topic_mer);
        final MerchandiseManager manager = new MerchandiseManager(this,progressBar,listView,abpull);
        manager.firstgetmerchandise(type);
        abpull.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                manager.onHeadFresh(type);
            }
        });
        abpull.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                manager.onFootFresh(type);
            }
        });
    }


    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        Intent getintent = getIntent();
        type = getintent.getIntExtra("market", 0);
        if(type == 0){
            titlename.setText("全部");
            type = 101;
        }else {
            type = type - 1;
            titlename.setText(getResources().getStringArray(R.array.market_topic)[type]);
            switch (type){
                case 0:
                    type = 5;
                    break;
                case 1:
                    type = 14;
                    break;
                case 2:
                    type = 23;
                    break;
                case 3:
                    type = 32;
                    break;
                case 4:
                    type = 41;
                    break;
                case 5:
                    type = 56;
                    break;
                case 6:
                    type = 67;
                    break;
            }
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

   /* @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent2 = new Intent();
        intent2.setClass(this,MarketDetailActivity.class);
        startActivity(intent2);
    }*/
}
