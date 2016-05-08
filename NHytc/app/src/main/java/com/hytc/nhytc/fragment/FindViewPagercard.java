package com.hytc.nhytc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.manager.LostBackManager;

/**
 * Created by Administrator on 2016/4/11.
 */
public class FindViewPagercard extends Fragment {
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
            view = inflater.inflate(R.layout.lost_viewpager_card_find, container, false);
            initwigit(view);
            abpull.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
                @Override
                public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                    manager.onHeadFresh(6,22,"");
                }
            });
            abpull.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
                @Override
                public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                    manager.onFootFresh(6,22,"");
                }
            });
        }
        return view;
    }

    private void initwigit(View view) {
        listView = (ListView) view.findViewById(R.id.lv_lost_viewpager_card_find);
        abpull = (AbPullToRefreshView) view.findViewById(R.id.abpull_lost_card_find);
        progressBar = (ProgressBar) view.findViewById(R.id.pgb_lost_card_find);
        manager = new LostBackManager(getActivity(), progressBar, listView, abpull);
        manager.firstgetlost(6,22,"");
    }

}
