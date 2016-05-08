package com.hytc.nhytc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.MainActivity;
import com.hytc.nhytc.adapter.SubscribeAdapter;
import com.hytc.nhytc.manager.SubscribeManager;


/**
 * Created by Administrator on 2015/8/11.
 */
public class Sunscribefragment extends Fragment {
    /**
     * title
     */
    private ImageView menu;
    private TextView titlename;


    private View view;
    private ListView listView;
    private AbPullToRefreshView abpull;
    private ProgressBar progressBar;

    private SubscribeManager manager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.framelayout_sunscribe, null);
            initTitle(view);
            initwidget(view);
        }
        return view;
    }

    private void initwidget(View view) {
        listView = (ListView) view.findViewById(R.id.lv_frame_subscribe);
        abpull = (AbPullToRefreshView) view.findViewById(R.id.abpull_subscribe);
        progressBar = (ProgressBar) view.findViewById(R.id.wait_pro_subscribe);
        manager = new SubscribeManager(getActivity(),progressBar,listView,abpull);
        manager.firstGetsubscribe();

        abpull.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                manager.reFresh();
                abpull.onHeaderRefreshFinish();
            }
        });

        abpull.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                manager.reFresh();
                abpull.onFooterLoadFinish();
            }
        });
    }

    private void initTitle(View view) {
        menu = (ImageView) view.findViewById(R.id.iv_user_head_my);
        titlename = (TextView) view.findViewById(R.id.tv_title_bar);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.dl.open();
            }
        });
        titlename.setText("订阅");
    }
}
