package com.hytc.nhytc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.manager.LostBackManager;

/**
 * Created by Administrator on 2016/4/12.
 */
public class SearchLostActivity extends Activity{
    private ListView listView;
    private AbPullToRefreshView abpull;
    private ProgressBar progressBar;
    private LostBackManager manager;

    private ImageView search;
    private EditText search_content;
    private TextView search_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lost);
        inittitle();
        initwidget();
    }

    private void initwidget() {
        manager.firstgetlost(12, 30,search_content.getText().toString());
        abpull.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                manager.onHeadFresh(12,30,search_content.getText().toString());
            }
        });
        abpull.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                manager.onFootFresh(12,30,search_content.getText().toString());
            }
        });
    }

    private void inittitle() {
        search = (ImageView) findViewById(R.id.iv_to_search);
        search_content = (EditText) findViewById(R.id.et_search_content);
        search_text = (TextView) findViewById(R.id.tv_lost_quxiao);
        listView = (ListView) findViewById(R.id.lv_lost_search);
        abpull = (AbPullToRefreshView) findViewById(R.id.abpull_lost_search);
        progressBar = (ProgressBar) findViewById(R.id.pgb_lost_search);
        manager = new LostBackManager(this,progressBar,listView,abpull);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.firstgetlost(12, 30,search_content.getText().toString());
            }
        });
        search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchLostActivity.this.finish();
            }
        });
    }
}
