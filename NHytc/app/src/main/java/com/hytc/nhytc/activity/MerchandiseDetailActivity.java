package com.hytc.nhytc.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.view.sample.AbInnerViewPager;
import com.hytc.nhytc.R;
import com.hytc.nhytc.domain.Merchandise;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.manager.RYfriendManager;
import com.hytc.nhytc.tool.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2016/2/10.
 */
public class MerchandiseDetailActivity  extends Activity {
    /**
     * title
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;

    private Merchandise merchandise;
    private TextView name;
    private TextView price;
    private TextView oldprice;
    private TextView describe;
    private Button button;

    private BitmapUtils bitmapUtils = null;
    private AbInnerViewPager mViewPager;
    private LinearLayout linearLayout;
    private ArrayList<ImageView> tips = new ArrayList<>();
    private ArrayList<String> imgs = new ArrayList<>();

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_detail);
        initTitle();
        findwidget();

    }

    private void findwidget() {
        for(String  s : merchandise.getPictures()){
            imgs.add(s);
        }
        initViewPager(imgs);
        name = (TextView) this.findViewById(R.id.tv_mer_name);
        price = (TextView) this.findViewById(R.id.tv_mer_price);
        oldprice = (TextView) this.findViewById(R.id.tv_old_price);
        describe = (TextView) this.findViewById(R.id.tv_describe_detail);
        button = (Button) this.findViewById(R.id.bt_contact_seller);

        user = BmobUser.getCurrentUser(this,User.class);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setClickable(false);
                boolean ismyself = user.getObjectId().equals(merchandise.getAuthor().getObjectId());
                boolean isexist = RYfriendManager.isexistfriend(MerchandiseDetailActivity.this, merchandise.getAuthor().getObjectId());
                if(!ismyself && !isexist){
                    RYfriendManager.putdata(MerchandiseDetailActivity.this, merchandise.getAuthor().getObjectId(), merchandise.getAuthor().getUsername(), merchandise.getAuthor().getHeadSculpture());
                }
                if(isexist){
                    RYfriendManager.update(MerchandiseDetailActivity.this,merchandise.getAuthor().getObjectId(), merchandise.getAuthor().getUsername(), merchandise.getAuthor().getHeadSculpture());
                }
                if (ismyself) {
                    Toast.makeText(MerchandiseDetailActivity.this, "亲，这里不能跟自己聊天哦~", Toast.LENGTH_SHORT).show();
                    button.setClickable(true);
                }
                else if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(MerchandiseDetailActivity.this, merchandise.getAuthor().getObjectId(), merchandise.getAuthor().getUsername());
                    button.setClickable(true);
                }
            }
        });
        name.setText(merchandise.getAuthor().getUsername());
        price.setText("￥:" + merchandise.getPrice());
        oldprice.setText("原价￥:" + merchandise.getOldPrice());
        describe.setText(merchandise.getContent());
    }

    private void initTitle() {
        Intent intent = getIntent();
        merchandise = (Merchandise) intent.getSerializableExtra("merchandise_info");
        bitmapUtils = BitmapHelper.getBitmapUtils(this);
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        titlename.setText("商品详情");
        ivinfo.setVisibility(View.GONE);
        tvinfo.setVisibility(View.GONE);
        ivmore.setVisibility(View.GONE);

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 根据获取的图片列表在viewpager中进行展示
     * @param imgs
     */
    private void initViewPager(ArrayList<String> imgs) {
        mViewPager = (AbInnerViewPager) findViewById(R.id.container);
        PagerAdapter mAdapter = new SimplePagerAdapter(bitmapUtils);
        mViewPager.setAdapter(mAdapter);
        linearLayout = (LinearLayout) findViewById(R.id.ll_image);
        for (int i = 0; i < imgs.size(); i++) {
            ImageView mImageView = new ImageView(this);
            tips.add(mImageView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT));
            layoutParams.rightMargin = 3;
            layoutParams.leftMargin = 3;
            mImageView.setBackgroundResource(R.drawable.page_indicator_unfocused_show);
            linearLayout.addView(mImageView, layoutParams);
        }
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
        public View instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            bitmapUtils.display(imageView, imgs.get(position));
            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
