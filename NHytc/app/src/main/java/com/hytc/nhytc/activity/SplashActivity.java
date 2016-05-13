package com.hytc.nhytc.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hytc.nhytc.R;
import com.hytc.nhytc.tool.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2016/3/9.
 */
public class SplashActivity extends Activity {
    private LinearLayout linearLayout;
    private ImageView iv_splash;
    private BitmapUtils bitmapUtils;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.splash);
        Bmob.initialize(this, "c86d7a0d5cea82f223c8cd789ff283a2");

        sharedPreferences = getSharedPreferences("first", Activity.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        /*final String isshow = sharedPreferences.getString("is_show", "k");
        String isrequest = sharedPreferences.getString("is_request", "k");*/
        String name = sharedPreferences.getString("name", "k");
        if ("k".equals(name) || "success2".equals(name)) {
            editor.putString("name", "success3");
            Intent intent = new Intent();
            intent.setClass(this, WelcomeActivity.class);
            startActivity(intent);
            editor.apply();
            finish();
        } else {
            editor.apply();
            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
            linearLayout = (LinearLayout) findViewById(R.id.ll_sp);
            //iv_splash = (ImageView) findViewById(R.id.iv_splash);
            bitmapUtils = BitmapHelper.getBitmapUtils(this);
            /*if ("yes".equals(isrequest)) {

                BmobQuery<Splash> query_picture = new BmobQuery<>();
                query_picture.addWhereEqualTo("function", "picture");
                boolean isCache = query_picture.hasCachedResult(this, Splash.class);
                if (isCache) {
                    query_picture.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
                } else {
                    query_picture.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
                }
                query_picture.setMaxCacheAge(TimeUnit.DAYS.toMillis(5));//此表示缓存5天
                query_picture.findObjects(this, new FindListener<Splash>() {
                    @Override
                    public void onSuccess(List<Splash> list) {
                        if (list.size() != 0) {
                            Splash splash = list.get(0);
                            if ("yes".equals(isshow)) {
                                bitmapUtils.display(iv_splash,splash.getContent1());
                            }else {
                                iv_splash.setImageResource(R.mipmap.splash);
                            }
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }else {
                iv_splash.setImageResource(R.mipmap.splash);
            }*/

                //iv_splash.setImageResource(R.mipmap.splash);
                aa.setDuration(1800);
                /*BmobQuery<Splash> query = new BmobQuery<>();
                query.addWhereEqualTo("function", "request");
                query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                query.setMaxCacheAge(TimeUnit.DAYS.toMillis(4));//此表示缓存4天
                query.findObjects(this, new FindListener<Splash>() {
                    @Override
                    public void onSuccess(List<Splash> list) {
                        if (list.size() != 0) {
                            Splash splash = list.get(0);
                            if (splash.getIsshow()) {
                                editor.putString("is_show", "yes");
                            } else {
                                editor.putString("is_show", "no");
                            }
                            if (splash.getIsrequest()) {
                                editor.putString("is_request", "yes");
                            } else {
                                editor.putString("is_request", "no");
                            }
                            editor.apply();
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });*/
                linearLayout.startAnimation(aa);
                new Thread() {

                    public void run() {
                        try {
                            sleep(2000);
                            Intent intent1 = new Intent();
                            intent1.setClass(SplashActivity.this, LoginActivity.class);
                            SplashActivity.this.startActivity(intent1);
                            SplashActivity.this.finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }

}

