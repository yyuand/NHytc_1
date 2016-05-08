package com.hytc.nhytc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.domain.ProgresMsgEB;
import com.hytc.nhytc.domain.ShowLove;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.manager.RYfriendManager;
import com.hytc.nhytc.manager.ShowLoveDetailManager;
import com.hytc.nhytc.tool.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.bmob.v3.BmobUser;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2016/2/1.
 */
public class ShowLoveDetailActivity extends Activity {
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;

    /**
     * 表白详情控件
     */
    private TextView author;
    private TextView loved_name;
    private TextView content;
    private ImageView iv_approve;
    private TextView tv_approve_count;
    private TextView tv_comment_count;
    private ImageView to_say;
    private RelativeLayout relativeLayout1;
    private RelativeLayout relativeLayout2;
    private RelativeLayout relativeLayout3;

    private LayoutInflater inflater;
    private View headerView;

    private AbPullToRefreshView abpull;
    private BitmapUtils bitmapUtils;
    private ListView listView;
    boolean isshowdialog = false;
    private ShowLove showLove;
    private List<ShowLove> items = null;
    private ProgressBar progressBar;
    private ShowLoveDetailManager manager;

    private User user;

    /**
     * 传过来的位置
     */
    private Integer position;
    /**
     * 用来设置返回的数据
     */
    private Intent data;
    /**
     * 最初的点赞值
     */
    private Boolean approvestatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showlovedetail);
        EventBus.getDefault().register(this);
        initTitle();

        initWidget();
        initheaderdata();
    }

    private void initTitle() {

        /**
         * title
         */
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        titlename.setText("详情");
        ivinfo.setVisibility(View.GONE);
        tvinfo.setVisibility(View.GONE);
        ivmore.setVisibility(View.GONE);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(approvestatus == showLove.getIsApprove()){
                    data.putExtra("appstatus",false);
                }else {
                    data.putExtra("appstatus",true);
                }

                data.putExtra("comcounts",tv_comment_count.getText().toString());
                data.putExtra("appcounts",tv_approve_count.getText().toString());

                data.putExtra("position",position);

                setResult(0,data);

                finish();
            }
        });
    }

    /**
     * 把数据放入list的头中
     */
    private void initheaderdata() {
        author.setText(showLove.getAuthor().getUsername());

        loved_name.setText(showLove.getShowLoveName());

        content.setText(showLove.getContent());

        if(showLove.getIsApprove()){
            iv_approve.setImageResource(R.mipmap.iconfont5);
        }else {
            iv_approve.setImageResource(R.mipmap.iconfont1);
        }

        tv_approve_count.setText(showLove.getApproveCount() + "");
        tv_comment_count.setText(showLove.getCommentCount() + "");




    }

    public void onEventMainThread(ProgresMsgEB msg) {
        if(msg.getMsg().equals("success")){
            if(isshowdialog){
                manager.getcommentdialog(true,null);
            }
        }
    }


    private void initWidget() {
        inflater = LayoutInflater.from(this);

        Intent intent = getIntent();
        showLove = (ShowLove)intent.getSerializableExtra("info");
        isshowdialog = intent.getBooleanExtra("isshowdialog", false);

        progressBar = (ProgressBar) this.findViewById(R.id.wait_pro_love);

        position = intent.getIntExtra("position", -1);
        data = new Intent();
        approvestatus = showLove.getIsApprove();

        user = BmobUser.getCurrentUser(this,User.class);
        /**
         * 详情头控件
         */
        headerView = inflater.inflate(R.layout.showlovedetaiheader, null);
        author = (TextView) headerView.findViewById(R.id.tv_show_love_person_detail);
        loved_name = (TextView) headerView.findViewById(R.id.tv_loved_person_detail);
        content = (TextView) headerView.findViewById(R.id.contant_all_love_detail);
        iv_approve = (ImageView) headerView.findViewById(R.id.iv_show_love_detail);
        tv_approve_count = (TextView) headerView.findViewById(R.id.tv_show_love_detail);
        tv_comment_count = (TextView) headerView.findViewById(R.id.tv_approve_count_show_love_detail);
        to_say = (ImageView) headerView.findViewById(R.id.iv_help_detail);
        relativeLayout1 = (RelativeLayout) headerView.findViewById(R.id.rl_approve_showlove_detail);
        relativeLayout2 = (RelativeLayout) headerView.findViewById(R.id.rl_comment_showlove_detail);
        relativeLayout3 = (RelativeLayout) headerView.findViewById(R.id.rl_tosay_showlove_detail);



        /**
         * 详情控件
         */
        abpull = (AbPullToRefreshView) this.findViewById(R.id.ab_pull_detail_comment);
        bitmapUtils = BitmapHelper.getBitmapUtils(this.getApplicationContext());
        listView = (ListView) this.findViewById(R.id.ls_love_detail);
        listView.setSelected(true);

        manager = new ShowLoveDetailManager(this,showLove,listView,headerView,tv_comment_count);
        manager.getallcommment(progressBar);



        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout1.setClickable(false);
                if (showLove.getIsApprove()) {
                    manager.approvelove(false, tv_approve_count, iv_approve,relativeLayout1);
                } else {
                    manager.approvelove(true, tv_approve_count, iv_approve,relativeLayout1);
                }
            }
        });

        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.getcommentdialog(true,null);
            }
        });

        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout3.setClickable(false);
                boolean ismyself = user.getObjectId().equals(showLove.getAuthor().getObjectId());
                boolean isexist = RYfriendManager.isexistfriend(ShowLoveDetailActivity.this, showLove.getAuthor().getObjectId());
                if (!ismyself && !isexist) {
                    RYfriendManager.putdata(ShowLoveDetailActivity.this, showLove.getAuthor().getObjectId(), showLove.getAuthor().getUsername(), showLove.getAuthor().getHeadSculpture());
                }
                if (isexist) {
                    RYfriendManager.update(ShowLoveDetailActivity.this, showLove.getAuthor().getObjectId(), showLove.getAuthor().getUsername(), showLove.getAuthor().getHeadSculpture());
                }
                if (ismyself) {
                    Toast.makeText(ShowLoveDetailActivity.this, "亲，这里不能跟自己聊天哦~", Toast.LENGTH_SHORT).show();
                    relativeLayout3.setClickable(true);
                } else if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(ShowLoveDetailActivity.this, showLove.getAuthor().getObjectId(), showLove.getAuthor().getUsername());
                    relativeLayout3.setClickable(true);
                }
            }
        });

        abpull.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
                manager.onFoot(abpull);
            }
        });
        abpull.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
                manager.onHeader(abpull);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    manager.getcommentdialog(false, manager.getloveComment(position - 1));
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if(approvestatus == showLove.getIsApprove()){
                data.putExtra("appstatus",false);
            }else {
                data.putExtra("appstatus",true);
            }

            data.putExtra("comcounts",tv_comment_count.getText().toString());
            data.putExtra("appcounts",tv_approve_count.getText().toString());

            data.putExtra("position",position);

            setResult(0,data);

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
