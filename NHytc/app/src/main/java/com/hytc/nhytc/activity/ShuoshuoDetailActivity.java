package com.hytc.nhytc.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.hytc.nhytc.adapter.PicsGirdAdapter;
import com.hytc.nhytc.domain.ProgresMsgEB;
import com.hytc.nhytc.domain.ShuoShuo;
import com.hytc.nhytc.domain.ShuoShuoComment;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.manager.RYfriendManager;
import com.hytc.nhytc.manager.ShuoShuoDetailManager;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.view.CircleImageView;
import com.hytc.nhytc.view.MyGirdView;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import de.greenrobot.event.EventBus;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2015/8/15.
 */
public class ShuoshuoDetailActivity extends Activity implements View.OnClickListener {
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;

    /**
     * 说说详情控件初始化
     */
    private CircleImageView head;
    private TextView name;
    private TextView time;
    private TextView topic;
    private TextView content;

    private ImageView approve;
    private TextView approvecount;
    private ImageView comment;
    private TextView commentcount;

    private ProgressBar progressBar;

    private RelativeLayout relativeLayout1;
    private RelativeLayout relativeLayout2;
    private RelativeLayout relativeLayout3;
    private MyGirdView myGirdView;
    private AbPullToRefreshView abpull;


    private ShuoShuoDetailManager manager;
    private BitmapUtils bitmapUtils;

    private ListView listView;
    private LayoutInflater inflater;

    private ShuoShuo shuoshuo;
    private List<ShuoShuoComment> items = null;

    private boolean isshowdialog = false;


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
        setContentView(R.layout.shuoshuodetail);
        EventBus.getDefault().register(this);
        findTitleByID();
        manager.initTitle(titlename, ivinfo, tvinfo, ivmore);
        initdetail();

        /**
         * 退出当前详情页面后，把数据返回到上一个activity
         */
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(approvestatus == shuoshuo.getIsApprove()){
                    data.putExtra("appstatus",false);
                }else {
                    data.putExtra("appstatus",true);
                }

                data.putExtra("comcounts",commentcount.getText().toString());
                data.putExtra("appcounts",approvecount.getText().toString());

                data.putExtra("position",position);

                setResult(0,data);

                finish();
            }
        });

    }

    private void findTitleByID() {
        Intent intent = getIntent();
        shuoshuo = (ShuoShuo)intent.getSerializableExtra("shuoinfo");
        isshowdialog = intent.getBooleanExtra("isshowdialog", false);

        position = intent.getIntExtra("position", -1);
        data = new Intent();
        approvestatus = shuoshuo.getIsApprove();

        progressBar = (ProgressBar) this.findViewById(R.id.wait_pro_shuo);

        user = BmobUser.getCurrentUser(this,User.class);

        abpull = (AbPullToRefreshView) this.findViewById(R.id.ab_pull_detail_comment);
        listView = (ListView) this.findViewById(R.id.ls_shuo_detail);
        listView.setSelected(true);
        /**
         * 说说详情控件初始化
         */
        inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.shuodetaillisthead, null);
        head = (CircleImageView) view.findViewById(R.id.iv_shuo_head_detail);
        name = (TextView) view.findViewById(R.id.tv_username_shuo_detail);
        time = (TextView) view.findViewById(R.id.tv_time_shuo_shuo_detail);
        topic = (TextView) view.findViewById(R.id.tvtopic__shuo_detail);
        content = (TextView) view.findViewById(R.id.tv_content_shuo_detail);
        approve = (ImageView) view.findViewById(R.id.iv_shuoshuo1);
        approvecount = (TextView) view.findViewById(R.id.tv_approve_count_detail);
        comment = (ImageView) view.findViewById(R.id.iv_shuoshuo2);
        commentcount = (TextView) view.findViewById(R.id.comment_count);
        relativeLayout1 = (RelativeLayout) view.findViewById(R.id.rl_approve_shuo_detail);
        relativeLayout2 = (RelativeLayout) view.findViewById(R.id.rl_comment_shuo_detail);
        relativeLayout3 = (RelativeLayout) view.findViewById(R.id.tl_to_chat);
        relativeLayout1.setOnClickListener(this);
        relativeLayout2.setOnClickListener(this);
        relativeLayout3.setOnClickListener(this);
        myGirdView = (MyGirdView) view.findViewById(R.id.gl_picture_shuoshuo_detail);
        bitmapUtils = BitmapHelper.getBitmapUtils(this.getApplicationContext());
        manager = new ShuoShuoDetailManager(this,shuoshuo,listView,view,commentcount);

        manager.getallcommment(progressBar);
        /**
         * 标题栏控件
         */
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);


        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuoshuo != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("person_data", shuoshuo.getAuthor());
                    Intent intent = new Intent();
                    intent.setClass(ShuoshuoDetailActivity.this, PersonDetailDataActivity.class);
                    intent.putExtras(bundle);
                    ShuoshuoDetailActivity.this.startActivity(intent);
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
                /**
                 * 当position为0的时候，那就是点击了listview的头了，那时也会触发这个点击事件，当然就要出错了
                 */
                if(position != 0) {
                    manager.getcommentdialog(false, manager.getShuoComment(position - 1));
                }
                //int newposition = position - 1;
                //manager.getcommentdialog(0, items.get(newposition).getTokenid(), items.get(newposition).getFloor());
            }
        });
    }


    public void onEventMainThread(ProgresMsgEB msg) {
        if(msg.getMsg().equals("success")){
            if(isshowdialog){
                manager.getcommentdialog(true,null);
            }
        }
    }

    public void initdetail(){
        bitmapUtils.display(head, shuoshuo.getAuthor().getHeadSculpture());
        name.setText(shuoshuo.getAuthor().getUsername());
        time.setText(shuoshuo.getCreatedAt());
        switch (shuoshuo.getTopic()){
            case 0:topic.setText("");break;
            case 1:topic.setText("表情帝");break;
            case 2:topic.setText("考研考证");break;
            case 3:topic.setText("约约约");break;
            case 4:topic.setText("敢不敢再嗅点");break;
            case 5:topic.setText("逗比搞笑");break;
            case 6:topic.setText("我要吐槽");break;
        }
        content.setText(shuoshuo.getContent());

        if(shuoshuo.getIsApprove()){
            approve.setImageResource(R.mipmap.iconfont5);
        }else {
            approve.setImageResource(R.mipmap.iconfont1);
        }
        approvecount.setText(shuoshuo.getApproveCount() + "");
        commentcount.setText(shuoshuo.getCommentCount() + "");

        ArrayList<String> pics = new ArrayList<>();
        if(shuoshuo.getPictures() != null) {
            for (Object s : shuoshuo.getPictures()) {
                pics.add(String.valueOf(s));
            }
            PicsGirdAdapter adapter = new PicsGirdAdapter(this,pics,9);
            myGirdView.setAdapter(adapter);
        }else {
            PicsGirdAdapter adapter = new PicsGirdAdapter(this,pics,0);
            myGirdView.setAdapter(adapter);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_approve_shuo_detail:
                relativeLayout1.setClickable(false);
                if(shuoshuo.getIsApprove()){
                    manager.approveshuo(false,approvecount,approve,relativeLayout1);
                }else {
                    manager.approveshuo(true,approvecount,approve,relativeLayout1);
                }
                break;
            case R.id.rl_comment_shuo_detail:
                manager.getcommentdialog(true,null);
                break;
            case R.id.tl_to_chat:
                relativeLayout3.setClickable(false);
                boolean ismyself = user.getObjectId().equals(shuoshuo.getAuthor().getObjectId());
                boolean isexist = RYfriendManager.isexistfriend(this, shuoshuo.getAuthor().getObjectId());
                if(!ismyself && !isexist){
                    RYfriendManager.putdata(this, shuoshuo.getAuthor().getObjectId(), shuoshuo.getAuthor().getUsername(), shuoshuo.getAuthor().getHeadSculpture());
                }
                if(isexist){
                    RYfriendManager.update(this,shuoshuo.getAuthor().getObjectId(), shuoshuo.getAuthor().getUsername(), shuoshuo.getAuthor().getHeadSculpture());
                }
                if (ismyself) {
                    relativeLayout3.setClickable(true);
                    Toast.makeText(this, "亲，这里不能跟自己聊天哦~", Toast.LENGTH_SHORT).show();
                }
                else if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(this, shuoshuo.getAuthor().getObjectId(), shuoshuo.getAuthor().getUsername());
                    relativeLayout3.setClickable(true);
                }
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if(approvestatus == shuoshuo.getIsApprove()){
                data.putExtra("appstatus",false);
            }else {
                data.putExtra("appstatus",true);
            }

            data.putExtra("comcounts",commentcount.getText().toString());
            data.putExtra("appcounts",approvecount.getText().toString());

            data.putExtra("position",position);

            setResult(0,data);

            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
