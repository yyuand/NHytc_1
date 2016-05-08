package com.hytc.nhytc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.MarketGridAdapter;
import com.hytc.nhytc.manager.MerchandiseManager;

/**
 * Created by Administrator on 2016/2/10.
 */
public class MerchandiseActivity extends Activity implements AdapterView.OnItemClickListener{
    /**
     * title
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;


    private GridView gridView;
    private ListView listView;
    private MarketGridAdapter adaptergrid;
    private View view;
    private LayoutInflater inflater;
    private ProgressBar progressBar;
    private AbPullToRefreshView abpull;
    private MerchandiseManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        initTitle();
        findwdiget();
    }

    private void findwdiget() {
        inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.activity_market_head, null);
        gridView = (GridView) view.findViewById(R.id.gv_market);
        listView = (ListView) this.findViewById(R.id.lv_activity_market);
        progressBar = (ProgressBar) this.findViewById(R.id.wait_pro_merchandise);
        abpull = (AbPullToRefreshView) this.findViewById(R.id.ab_pull_merchandise);
        adaptergrid = new MarketGridAdapter(this);
        gridView.setAdapter(adaptergrid);
        listView.addHeaderView(view);
        manager = new MerchandiseManager(this,progressBar,listView,abpull);
        manager.firstgetmerchandise(101);
        gridView.setOnItemClickListener(this);
        listView.setOnItemClickListener(this);
        abpull.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                manager.onHeadFresh(101);
            }
        });
        abpull.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                manager.onFootFresh(101);
            }
        });
    }


    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        titlename.setText("跳蚤市场");

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.gv_market:
                Intent intent1 = new Intent();
                intent1.setClass(this, MerchandiseTopicActivity.class);
                intent1.putExtra("market",position);
                startActivity(intent1);
                break;
        }
    }
}
