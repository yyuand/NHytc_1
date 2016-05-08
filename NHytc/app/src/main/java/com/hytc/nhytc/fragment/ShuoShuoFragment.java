package com.hytc.nhytc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.MainActivity;
import com.hytc.nhytc.activity.ShuoshuoMoreTopicActivity;
import com.hytc.nhytc.domain.ForResulltMsg;
import com.hytc.nhytc.manager.ShuoShuommmManager;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/1/26.
 */
public class ShuoShuoFragment extends Fragment {



    private View view;

    /**
     * title 控件声明
     */
    private TextView titlename;
    private TextView moretopic;
    /**
     * listview控件
     */
    private ListView listView;

    private AbPullToRefreshView abpull;
    private ProgressBar probar;

    private ShuoShuommmManager manager;

    private ImageView userheader;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.framelayout_shuo, null);
            EventBus.getDefault().register(this);
            findTitleByID(view);
            initTitle();
            manager.firstGetShuo();
        }
        return view;
    }


    public void initTitle() {

        titlename.setText("淮说");
        moretopic.setText("更多话题");

        moretopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choosetopicintent = new Intent();
                choosetopicintent.setClass(getActivity(), ShuoshuoMoreTopicActivity.class);
                getActivity().startActivity(choosetopicintent);
            }
        });

        userheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.dl.open();
            }
        });
    }

    public void onEventMainThread(ForResulltMsg msg) {
        Log.e("qwe","123");
        manager.updata(msg);
    }


    private void findTitleByID(View view) {
        /**
         * 标题栏控件
         */
        titlename = (TextView) view.findViewById(R.id.tv_title_bar);
        moretopic = (TextView) view.findViewById(R.id.tv_more_topic);
        userheader = (ImageView) view.findViewById(R.id.iv_user_head_my);

        /**
         * listview控件
         */
        listView = (ListView) view.findViewById(R.id.lv_shuoshuo);
        abpull = (AbPullToRefreshView) view.findViewById(R.id.abptr_shuoshuo);
        probar = (ProgressBar) view.findViewById(R.id.wait_pro_shuo);

        manager = new ShuoShuommmManager(getActivity(), probar, listView, abpull);

        /**
         * 上拉刷新
         */
        abpull.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                manager.onHeadLoad();
            }
        });

        /**
         * 下拉刷新
         */
        abpull.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                manager.onFootLoad();
            }
        });
    }


}
