package com.hytc.nhytc.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.LostActivity;
import com.hytc.nhytc.activity.MainActivity;
import com.hytc.nhytc.activity.MainCourseActivity;
import com.hytc.nhytc.activity.MerchandiseActivity;
import com.hytc.nhytc.activity.PhoneActivity;
import com.hytc.nhytc.activity.ShowLoveActivity;
import com.hytc.nhytc.activity.ShuoshuoMoreTopicActivity;
import com.hytc.nhytc.activity.TextActivity;
import com.hytc.nhytc.activity.WebActivity;
import com.hytc.nhytc.domain.LostBack;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.view.CircleImageView;
import com.lidroid.xutils.BitmapUtils;

import cn.bmob.v3.BmobUser;


/**
 * Created by Administrator on 2015/8/11.
 */
public class Activityfragment extends Fragment {

    /**
     * title
     */
    private TextView titlename;
    private ImageView userheader;

    private RelativeLayout relativeLayout1;
    private RelativeLayout relativeLayout2;
    private RelativeLayout relativeLayout3;
    private RelativeLayout relativeLayout4;
    private RelativeLayout relativeLayout5;
    private RelativeLayout relativeLayout6;




    private View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fregment_more, null);
            initTitle(view);
            initWidget(view);
        }
        return view;
    }

    private void initWidget(View view) {
        relativeLayout1 = (RelativeLayout) view.findViewById(R.id.rl_fregment_more1);
        relativeLayout2 = (RelativeLayout) view.findViewById(R.id.rl_fregment_more2);
        relativeLayout3 = (RelativeLayout) view.findViewById(R.id.rl_fregment_more3);
        relativeLayout4 = (RelativeLayout) view.findViewById(R.id.rl_fregment_more4);
        relativeLayout5 = (RelativeLayout) view.findViewById(R.id.rl_fregment_more5);
        relativeLayout6 = (RelativeLayout) view.findViewById(R.id.rl_fregment_more6);

        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choosetopicintent = new Intent();
                choosetopicintent.setClass(getActivity(), LostActivity.class);
                getActivity().startActivity(choosetopicintent);
            }
        });
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choosetopicintent = new Intent();
                choosetopicintent.setClass(getActivity(), MerchandiseActivity.class);
                getActivity().startActivity(choosetopicintent);
            }
        });
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choosetopicintent = new Intent();
                choosetopicintent.setClass(getActivity(), ShowLoveActivity.class);
                getActivity().startActivity(choosetopicintent);
            }
        });
        relativeLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), WebActivity.class);
                intent.putExtra("url", "http://www.iweizhijia.com/mobile/cet/index/token/CwULCEUGW2pSBA4HVQ4HU1EDBVVOB28ECFcCVQ--/_/1457441767");
                getActivity().startActivity(intent);
            }
        });
        relativeLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choosetopicintent = new Intent();
                choosetopicintent.setClass(getActivity(), PhoneActivity.class);
                getActivity().startActivity(choosetopicintent);
            }
        });
        relativeLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MainCourseActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    public void initTitle(View view) {

        titlename = (TextView) view.findViewById(R.id.tv_title_bar);
        titlename.setText("更多");
        userheader = (ImageView) view.findViewById(R.id.iv_menu);
        userheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.dl.open();
            }
        });
    }

}
