package com.hytc.nhytc.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.LostViewPageAdapter;
import com.hytc.nhytc.fragment.FindViewPagerBook;
import com.hytc.nhytc.fragment.FindViewPagerElectron;
import com.hytc.nhytc.fragment.FindViewPagerOther;
import com.hytc.nhytc.fragment.FindViewPagercard;
import com.hytc.nhytc.fragment.FindViewPagercloths;
import com.hytc.nhytc.fragment.LostViewPagerBook;
import com.hytc.nhytc.fragment.LostViewPagerElectron;
import com.hytc.nhytc.fragment.LostViewPagerOther;
import com.hytc.nhytc.fragment.LostViewPagercard;
import com.hytc.nhytc.fragment.LostViewPagercloths;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/7.
 */
public class LostActivity extends FragmentActivity {


    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView tvshiwu;
    private TextView tvxunwu;
    private ImageView ivmore;


    private ViewPager vp;
    private ViewPager vp_find;
    private LostViewPageAdapter adapter;
    private LostViewPageAdapter function_adapter;




    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private LinearLayout linearLayout3;
    private LinearLayout linearLayout4;
    private LinearLayout linearLayout5;

    private RelativeLayout shiwu;
    private RelativeLayout xunwu;

    List<Fragment> fragments;
    List<Fragment> function_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost);
        initWideget();
        initTitle();
        fragments = new ArrayList<>();
        fragments.add(new LostViewPagercard());
        fragments.add(new LostViewPagerBook());
        fragments.add(new LostViewPagerElectron());
        fragments.add(new LostViewPagercloths());
        fragments.add(new LostViewPagerOther());

        function_fragment = new ArrayList<>();
        function_fragment.add(new FindViewPagercard());
        function_fragment.add(new FindViewPagerBook());
        function_fragment.add(new FindViewPagerElectron());
        function_fragment.add(new FindViewPagercloths());
        function_fragment.add(new FindViewPagerOther());

        adapter = new LostViewPageAdapter(getSupportFragmentManager(),fragments);
        function_adapter = new LostViewPageAdapter(getSupportFragmentManager(),function_fragment);
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(new Mypagechanged());
        vp.setCurrentItem(0);

        vp_find.setAdapter(function_adapter);
        vp_find.setOnPageChangeListener(new Mypagechanged());
        vp_find.setCurrentItem(0);
    }


    public void initWideget(){


        vp = (ViewPager) this.findViewById(R.id.view_pager_activity_lost);
        vp_find = (ViewPager) findViewById(R.id.view_pager_activity_lost_function);

        /**
         * 5个标签页
         */
        textView1 = (TextView) this.findViewById(R.id.tv_lost11);
        textView2 = (TextView) this.findViewById(R.id.tv_lost22);
        textView3 = (TextView) this.findViewById(R.id.tv_lost33);
        textView4 = (TextView) this.findViewById(R.id.tv_lost44);
        textView5 = (TextView) this.findViewById(R.id.tv_lost55);
        /**
         * 5个标签页设置监听
         */
        textView1.setOnClickListener(new MyOnclick(0));
        textView2.setOnClickListener(new MyOnclick(1));
        textView3.setOnClickListener(new MyOnclick(2));
        textView4.setOnClickListener(new MyOnclick(3));
        textView5.setOnClickListener(new MyOnclick(4));

        linearLayout1 = (LinearLayout) this.findViewById(R.id.ll_lost1);
        linearLayout2 = (LinearLayout) this.findViewById(R.id.ll_lost2);
        linearLayout3 = (LinearLayout) this.findViewById(R.id.ll_lost3);
        linearLayout4 = (LinearLayout) this.findViewById(R.id.ll_lost4);
        linearLayout5 = (LinearLayout) this.findViewById(R.id.ll_lost5);


    }




    public class MyOnclick implements View.OnClickListener{
        private int i;

        public MyOnclick(int i){
            this.i = i;
        }

        @Override
        public void onClick(View v) {
            setbaccurrent(i);
            vp.setCurrentItem(i);
        }
    }

    public class Mypagechanged implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setbaccurrent(position);
            vp.setCurrentItem(position);
            vp_find.setCurrentItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setbacground(){
        linearLayout1.setBackground(null);
        linearLayout2.setBackground(null);
        linearLayout3.setBackground(null);
        linearLayout4.setBackground(null);
        linearLayout5.setBackground(null);

        /*linearLayout1.setBackgroundColor(getResources().getColor(R.color.ivory_background));
        linearLayout2.setBackgroundColor(getResources().getColor(R.color.ivory_background));
        linearLayout3.setBackgroundColor(getResources().getColor(R.color.ivory_background));
        linearLayout4.setBackgroundColor(getResources().getColor(R.color.ivory_background));
        linearLayout5.setBackgroundColor(getResources().getColor(R.color.ivory_background));*/
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setbaccurrent(int i){
        switch (i){
            case 0:
                setbacground();
                Drawable drawable1 = getResources().getDrawable(R.drawable.kindshape);
                linearLayout1.setBackground(drawable1);
                break;
            case 1:
                setbacground();
                Drawable drawable2 = getResources().getDrawable(R.drawable.kindshape);
                linearLayout2.setBackground(drawable2);
                break;
            case 2:
                setbacground();
                Drawable drawable3 = getResources().getDrawable(R.drawable.kindshape);
                linearLayout3.setBackground(drawable3);
                break;
            case 3:
                setbacground();
                Drawable drawable4 = getResources().getDrawable(R.drawable.kindshape);
                linearLayout4.setBackground(drawable4);
                break;
            case 4:
                setbacground();
                Drawable drawable5 = getResources().getDrawable(R.drawable.kindshape);
                linearLayout5.setBackground(drawable5);
                break;
        }
    }



    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        shiwu = (RelativeLayout) this.findViewById(R.id.rl_lost_shiwu);
        xunwu = (RelativeLayout) this.findViewById(R.id.rl_lost_xunwu);
        ivmore = (ImageView) this.findViewById(R.id.iv_search_titlebar);
        tvshiwu = (TextView) findViewById(R.id.tv_lost_shiwu);
        tvxunwu = (TextView) findViewById(R.id.tv_lost_xunwu);

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shiwu.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                initTitleRl();
                Drawable drawable = getResources().getDrawable(R.drawable.kindshape);
                shiwu.setBackground(drawable);
                vp.setVisibility(View.VISIBLE);
                vp_find.setVisibility(View.INVISIBLE);
                tvshiwu.setTextSize(13);
            }
        });
        xunwu.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                initTitleRl();
                Drawable drawable = getResources().getDrawable(R.drawable.kindshape);
                xunwu.setBackground(drawable);
                vp.setVisibility(View.INVISIBLE);
                vp_find.setVisibility(View.VISIBLE);
                tvxunwu.setTextSize(13);
            }
        });
        ivmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LostActivity.this,SearchLostActivity.class);
                LostActivity.this.startActivity(intent);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void initTitleRl(){
        shiwu.setBackground(null);
        xunwu.setBackground(null);
        tvshiwu.setTextSize(10);
        tvxunwu.setTextSize(10);
    }

}
