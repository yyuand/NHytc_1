package com.hytc.nhytc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.manager.LostBackManager;

/**
 * Created by Administrator on 2015/8/18.
 */
public class LostViewPagerBook extends Fragment {
    private ListView listView;
    private AbPullToRefreshView abpull;
    private ProgressBar progressBar;
    private LostBackManager manager;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.lost_viewpager_book, container, false);
            initwigit(view);
            manager.firstgetlost(45,11,"");
            abpull.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
                @Override
                public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                    manager.onHeadFresh(45,11,"");
                }
            });
            abpull.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
                @Override
                public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                    manager.onFootFresh(45,11,"");
                }
            });
        }
        return view;
    }
    private void initwigit(View view) {
        listView = (ListView) view.findViewById(R.id.lv_lost_viewpager_book);
        abpull = (AbPullToRefreshView) view.findViewById(R.id.abpull_lost_book);
        progressBar = (ProgressBar) view.findViewById(R.id.pgb_lost_book);
        manager = new LostBackManager(getActivity(),progressBar,listView,abpull);
    }
}
