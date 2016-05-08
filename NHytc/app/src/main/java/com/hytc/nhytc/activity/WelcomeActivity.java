package com.hytc.nhytc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hytc.nhytc.R;
import com.hytc.nhytc.domain.Splash;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/9.
 */
public class WelcomeActivity extends Activity {
    private ViewPager viewPager;
    private LinearLayout goto_hytc;
    ArrayList<View> viewContainter = new ArrayList<View>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_welcome);
        viewPager = (ViewPager) this.findViewById(R.id.view_pager_welcome);
        View view1 = LayoutInflater.from(this).inflate(R.layout.activity_welcome1, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.activity_welcome2, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.activity_welcome3, null);
        View view4 = LayoutInflater.from(this).inflate(R.layout.activity_welcome4, null);
        View view5 = LayoutInflater.from(this).inflate(R.layout.activity_welcome5, null);
        goto_hytc = (LinearLayout) view5.findViewById(R.id.goto_hytc);
        //viewpager开始添加view
        viewContainter.add(view1);
        viewContainter.add(view2);
        viewContainter.add(view3);
        viewContainter.add(view4);
        viewContainter.add(view5);
        viewPager.setAdapter(new PagerAdapter() {

            //viewpager中的组件数量
            @Override
            public int getCount() {
                return viewContainter.size();
            }

            //滑动切换的时候销毁当前的组件
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView(viewContainter.get(position));
            }

            //每次滑动的时候生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(viewContainter.get(position));
                return viewContainter.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return super.getPageTitle(position);
            }
        });

        goto_hytc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, SplashActivity.class);
                WelcomeActivity.this.startActivity(intent);
                WelcomeActivity.this.finish();
            }
        });
    }
}
