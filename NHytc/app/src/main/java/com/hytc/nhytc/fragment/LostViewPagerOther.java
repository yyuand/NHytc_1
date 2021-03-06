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
 * Created by Administrator on 2015/8/18.
 */
public class LostViewPagerOther extends Fragment {
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
            view = inflater.inflate(R.layout.lost_viewpager_other, container, false);
            initwigit(view);
            manager.firstgetlost(34,11,"");
            abpull.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
                @Override
                public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                    manager.onHeadFresh(34,11,"");
                }
            });
            abpull.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
                @Override
                public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                    manager.onFootFresh(34,11,"");
                }
            });
        }
        return view;
    }
    private void initwigit(View view) {
        listView = (ListView) view.findViewById(R.id.lv_lost_viewpager_other);
        abpull = (AbPullToRefreshView) view.findViewById(R.id.abpull_lost_other);
        progressBar = (ProgressBar) view.findViewById(R.id.pgb_lost_other);
        manager = new LostBackManager(getActivity(),progressBar,listView,abpull);
    }
}
