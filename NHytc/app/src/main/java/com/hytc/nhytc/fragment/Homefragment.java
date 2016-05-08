package com.hytc.nhytc.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.ab.view.sample.AbInnerViewPager;
import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.HomeFragmentAdapter;
import com.hytc.nhytc.tool.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/11.
 */
public class Homefragment extends Fragment {
    private View view;
    private View view1;
    private View view2;
    private HomeFragmentAdapter adapter;

    private ListView listView;

    private BitmapUtils bitmapUtils = null;
    private AbInnerViewPager mViewPager;
    private LinearLayout linearLayout;
    private ArrayList<ImageView> tips = new ArrayList<>();
    private ArrayList<String> imgs = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.framelayout_home,null);
        adapter = new HomeFragmentAdapter(getActivity());
        view1 = inflater.inflate(R.layout.home_head_item,null);
        view2 = inflater.inflate(R.layout.home_end_item,null);
        listView = (ListView) view.findViewById(R.id.lv_frame_home);
        imgs.clear();
        imgs.add("http://img.woyaogexing.com/2015/08/26/2c2978e55f37cd60!200x200.jpg");
        imgs.add("http://img.woyaogexing.com/2015/08/26/2c2978e55f37cd60!200x200.jpg");
        imgs.add("http://img.woyaogexing.com/2015/08/26/2c2978e55f37cd60!200x200.jpg");
        imgs.add("http://img.woyaogexing.com/2015/08/26/2c2978e55f37cd60!200x200.jpg");
        bitmapUtils = BitmapHelper.getBitmapUtils(getActivity());
        initViewPager(imgs);
        Log.e("Home==", "6");
        listView.addHeaderView(view1);
        Log.e("Home==", "7");
        listView.addFooterView(view2);
        Log.e("Home==", "8");
        listView.setAdapter(adapter);
        Log.e("Home==", "9");
        listView.setDividerHeight(0);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    /**
     * @param imgs
     */
    private void initViewPager(ArrayList<String> imgs) {
        Log.e("Home==", "1");
        mViewPager = (AbInnerViewPager) view1.findViewById(R.id.container1);
        PagerAdapter mAdapter = new SimplePagerAdapter(bitmapUtils);
        Log.e("Home==", "2");
        mViewPager.setAdapter(mAdapter);
        Log.e("Home==", "3");
        linearLayout = (LinearLayout) view1.findViewById(R.id.ll_image1);
        for (int i = 0; i < imgs.size(); i++) {
            ImageView mImageView = new ImageView(getActivity());
            tips.add(mImageView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT));
            layoutParams.rightMargin = 3;
            layoutParams.leftMargin = 3;
            mImageView.setBackgroundResource(R.drawable.page_indicator_unfocused_show);
            linearLayout.addView(mImageView, layoutParams);
        }
        Log.e("Home==", "4");
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setImageBackground(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Log.e("Home==", "5");
    }


    private class SimplePagerAdapter extends PagerAdapter {
        private BitmapUtils bitmapUtils;

        public SimplePagerAdapter(BitmapUtils bitmapUtils){
            this.bitmapUtils = bitmapUtils;
        }

        @Override
        public int getCount() {
            return imgs.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent intent = new Intent();
                    intent.setClass(getActivity(), FirstHeadWebActivity.class);
                    intent.putExtra("page",position);
                    startActivity(intent);
                    Toast.makeText(getActivity(),position+"===>",Toast.LENGTH_SHORT).show();*/
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }


    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.size(); i++) {
            if (i == selectItems) {
                tips.get(i).setBackgroundResource(R.drawable.page_indicator_focused_show);
            } else {
                tips.get(i).setBackgroundResource(R.drawable.page_indicator_unfocused_show);
            }
        }
    }


}
