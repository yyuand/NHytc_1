package com.hytc.nhytc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.PartJobListAdapter;
import com.hytc.nhytc.domain.PartJob;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by yyuand on 2016/5/27.
 */
public class ActPartJob extends Activity {


    @Bind(R.id.lv_partjob)
    ListView lvPartjob;
    @Bind(R.id.abpull_partjob)
    AbPullToRefreshView abpullPartjob;
    @Bind(R.id.wait_pro_partjob)
    ProgressBar waitProPartjob;
    @Bind(R.id.iv_new_back_titlebar)
    ImageView ivNewBackTitlebar;
    @Bind(R.id.tv_new_title_bar)
    TextView tvNewTitleBar;

    private PartJobListAdapter adapter;
    private Boolean isFirst;//判断是否第一次获取数据
    private String endTime;
    private String jobName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_partjob);
        ButterKnife.bind(this);
        initdata();
        getData();
    }

    private void initdata() {
        tvNewTitleBar.setText("掌淮兼职");
        isFirst = true;
        /**
         * 获取兼职名称，实现顶部刷新（下拉刷新）
         */
        abpullPartjob.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                getData();
                abpullPartjob.onHeaderRefreshFinish();
            }
        });

        /**
         * 获取兼职名称，实现底部刷新（上拉刷新）
         */
        abpullPartjob.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                onFootLoads();
                abpullPartjob.onFooterLoadFinish();
            }
        });
    }

    @OnClick(R.id.iv_new_back_titlebar)
    public void onClick() {
        finish();
    }

    /**获取数据
     *
     */
    private void getData(){
        if(isFirst){//是否第一次获取数据
            waitProPartjob.setVisibility(View.VISIBLE);//显示缓冲的小圆圈
        }
        BmobQuery<PartJob> query = new BmobQuery<>();
        query.setLimit(15);//一次刷新显示15条数据
        query.order("-createdAt");//降序
        query.include("boss");//显示包括发布者的信息
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);//先获取服务器上的数据，再找本地的
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//保存一天的缓存

        query.findObjects(this, new FindListener<PartJob>() {
            @Override
            public void onSuccess(List<PartJob> list) {
                endTime = list.get(list.size()-1).getCreatedAt();//最后一条数据的创建时间
                if(isFirst){
                    jobName = list.get(0).getJob_Name();
                    waitProPartjob.setVisibility(View.INVISIBLE);
                    isFirst = false;
                    adapter = new PartJobListAdapter(ActPartJob.this,list);
                    lvPartjob.setAdapter(adapter);
                }else {
                    if(jobName.equals(list.get(0).getJob_Name())){
                        Toast.makeText(ActPartJob.this,"已是最新内容咯~",Toast.LENGTH_SHORT).show();
                    }else{
                        jobName = list.get(0).getJob_Name();
                        adapter.refreshData(list);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                if(isFirst){
                    waitProPartjob.setVisibility(View.INVISIBLE);
                    ActPartJob.this.findViewById(R.id.nothing).setVisibility(View.VISIBLE);
                }
                Toast.makeText(ActPartJob.this,"请检查网络连接！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onFootLoads(){
        BmobQuery<PartJob> query = new BmobQuery<>();
        query.setLimit(15);
        query.order("-createdAt");
        query.include("boss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date  = null;
        try {
            date = sdf.parse(endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        query.addWhereLessThan("createdAt", new BmobDate(date));
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));
        query.findObjects(this, new FindListener<PartJob>() {
            @Override
            public void onSuccess(List<PartJob> list) {
                if(list.size()==0){
                    Toast.makeText(ActPartJob.this,"已经到底啦~",Toast.LENGTH_SHORT).show();
                }else{
                    endTime = list.get(list.size()-1).getCreatedAt();
                    adapter.addData(list);
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(ActPartJob.this,"请检查网络连接！",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
