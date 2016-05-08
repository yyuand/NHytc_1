package com.hytc.nhytc.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hytc.nhytc.R;
import com.hytc.nhytc.dbDAO.ApproveDao;
import com.hytc.nhytc.dbDAO.LoveDao;
import com.hytc.nhytc.domain.MyInfo;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.manager.RYfriendManager;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.view.CircleImageView;
import com.hytc.nhytc.view.activity_transition.ActivityTransitionLauncher;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2016/2/28.
 */
public class PersonDetailDataActivity extends Activity {
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;

    private User user;
    private LayoutInflater inflater;
    private BitmapUtils bitmapUtils;
    Intent intent;

    private CircleImageView header;
    private TextView name;
    private ImageView sex;
    private TextView saysomething;
    private RelativeLayout love;
    private RelativeLayout approve;
    private TextView approve_count;
    private RelativeLayout tochat;
    private TextView faculty;
    private RelativeLayout photos;
    private ImageView iv_love;
    private ImageView iv_approve;

    /**
     * 记录最初是否可以点赞，就是刚进入这个界面
     * 点赞图片是红的还是灰的
     */
    private Boolean isapprove;

    private LoveDao loveDao;
    private ApproveDao approveDao;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnal_detail_data);
        initTitle();
        initwidget();
        isok();

    }


    private void initwidget() {
        intent = getIntent();
        user = (User) intent.getSerializableExtra("person_data");
        inflater = LayoutInflater.from(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("信息详情获取中...");
        bitmapUtils = BitmapHelper.getBitmapUtils(this);

        header = (CircleImageView) findViewById(R.id.iv_person_detail_head);
        name = (TextView) findViewById(R.id.tv_username_person_detail);
        sex = (ImageView) findViewById(R.id.iv_sex_person_detail);
        saysomething = (TextView) findViewById(R.id.tv_say_something_person_detail);
        love = (RelativeLayout) findViewById(R.id.rl_love_person_detail);
        approve = (RelativeLayout) findViewById(R.id.rl_approve_person_detail);
        approve_count = (TextView) findViewById(R.id.tv_approve_count_person_detail);
        tochat = (RelativeLayout) findViewById(R.id.rl_tochat_person_detail);
        faculty = (TextView) findViewById(R.id.tv_faculty_person_detail);
        photos = (RelativeLayout) findViewById(R.id.rl_photos_person_detail);
        iv_love = (ImageView) findViewById(R.id.iv_love);
        iv_approve = (ImageView) findViewById(R.id.iv_prase);
        loveDao = new LoveDao(this);
        approveDao = new ApproveDao(this);


        /**
         * 设置头像的点击事件
         */
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ArrayList<String> picurls = new ArrayList<>();
                picurls.add(user.getHeadSculpture());
                intent.putExtra("imgurls", picurls);
                intent.putExtra("pagenum", 1);
                intent.setClass(PersonDetailDataActivity.this, ImageViewActivity.class);
                ActivityTransitionLauncher.with(PersonDetailDataActivity.this).from(v).launch(intent);
            }
        });

        /**
         * 设置点赞RelativeLayout的点击事件
         */
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null) {
                    approve.setClickable(false);
                    if(approveDao.isExist(user.getObjectId()) && approveDao.isOneDay(user.getObjectId(),getDay())){
                        approveDataToBmpb(false,approve);
                    }else {
                        approveDataToBmpb(true,approve);
                    }
                }else {
                    Toast.makeText(PersonDetailDataActivity.this, "操作失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 设置暗恋RelativeLayout的点击事件
         */
        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                love.setClickable(false);
                if(user != null) {
                    if(loveDao.isExist(user.getObjectId())){
                        loveDataToBmob(false,love);
                    }else {
                        loveDataToBmob(true,love);
                    }
                }else {
                    Toast.makeText(PersonDetailDataActivity.this, "操作失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /**
         * 设置聊天RelativeLayout的点击事件
         */
        tochat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tochat.setClickable(false);
                if(user != null) {
                    boolean ismyself = BmobUser.getCurrentUser(PersonDetailDataActivity.this,User.class).getObjectId().equals(user.getObjectId());
                    boolean isexist = RYfriendManager.isexistfriend(PersonDetailDataActivity.this, user.getObjectId());
                    if (!ismyself && !isexist) {
                        RYfriendManager.putdata(PersonDetailDataActivity.this, user.getObjectId(), user.getUsername(), user.getHeadSculpture());
                    }
                    if (isexist) {
                        RYfriendManager.update(PersonDetailDataActivity.this, user.getObjectId(), user.getUsername(), user.getHeadSculpture());
                    }
                    if (ismyself) {
                        Toast.makeText(PersonDetailDataActivity.this, "亲，自己就不要勾搭了吧~", Toast.LENGTH_SHORT).show();
                        tochat.setClickable(true);
                    }
                    else if (RongIM.getInstance() != null) {
                        RongIM.getInstance().startPrivateChat(PersonDetailDataActivity.this, user.getObjectId(), user.getUsername());
                        tochat.setClickable(true);
                    }
                }else {
                    Toast.makeText(PersonDetailDataActivity.this, "发起聊天失败！", Toast.LENGTH_SHORT).show();
                    tochat.setClickable(true);
                }
            }
        });

        /**
         * 设置相册RelativeLayout的点击事件
         */
        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PersonDetailDataActivity.this, UpLoadMyPhotos.class);
                intent.putExtra("photos", false);
                if(user != null) {
                    intent.putExtra("userid", user.getObjectId());
                }else {
                    intent.putExtra("userid","null");
                }
                startActivity(intent);
            }
        });

    }

    /**
     * 初始化标题栏
     */
    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);


        titlename.setText("信息详情");
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
     * 用来判断接收到的user是否为空
     */
    public void isok(){
        if(user == null){
            String userid = intent.getStringExtra("suerid");
            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("objectId",userid);
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
            query.setMaxCacheAge(TimeUnit.DAYS.toMillis(3));//此表示缓存3天
            progressDialog.show();
            query.findObjects(this, new FindListener<User>() {
                @Override
                public void onSuccess(List<User> list) {
                    progressDialog.dismiss();
                    dataToWidget(list.get(0));
                }

                @Override
                public void onError(int i, String s) {
                    progressDialog.dismiss();
                    Toast.makeText(PersonDetailDataActivity.this,"信息获取失败\n请检查网络连接",Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            dataToWidget(user);
        }
    }


    /**
     * 当确认user不为空的时候，把user的数据放入控件中
     * @param user
     */
    public void dataToWidget(User user) {
        bitmapUtils.display(header, user.getHeadSculpture());
        name.setText(user.getUsername());
        if(user.getGender()){
            sex.setImageResource(R.drawable.man);
        }else {
            sex.setImageResource(R.drawable.woman);
        }

        if(loveDao.isExist(user.getObjectId())){
            iv_love.setImageResource(R.mipmap.loveyes);
        }else {
            iv_love.setImageResource(R.mipmap.loveno);
        }

        /**
         * 如果数据库中存在该信息（就是我为这个人点过赞了），但是不是同一天，那么此时我就要把这个人的消息从本地数据库中取消清楚掉
         */
        if(approveDao.isExist(user.getObjectId()) && !approveDao.isOneDay(user.getObjectId(),getDay())){
            approveDao.deletedata(user.getObjectId());
        }

        if(approveDao.isExist(user.getObjectId()) && approveDao.isOneDay(user.getObjectId(),getDay())){
            isapprove = true;
            iv_approve.setImageResource(R.mipmap.person_approve);
        }else {
            isapprove = false;
            iv_approve.setImageResource(R.mipmap.person_approveno);
        }


        saysomething.setText(user.getAutograhp());
        faculty.setText(user.getFaculty());
        approve_count.setText("赞：" + user.getPraise());
    }

    /**
     * 把暗恋信息存入bmob服务器
     * @param isok
     */
    public void loveDataToBmob(final Boolean isok, final RelativeLayout relativeLayout){
        //test对应你刚刚创建的云端逻辑名称
        String cloudCodeName;
        if(isok) {
            cloudCodeName = "addloved";
        }else {
            cloudCodeName = "subloved";
        }
        JSONObject params = new JSONObject();
        //name是上传到云端的参数名称，值是bmob，云端逻辑可以通过调用request.body.name获取这个值
        try {
            params.put("userid", user.getObjectId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //创建云端逻辑对象
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        //异步调用云端逻辑
        cloudCode.callEndpoint(PersonDetailDataActivity.this, cloudCodeName, params, new CloudCodeListener() {

            //执行成功时调用，返回result对象
            @Override
            public void onSuccess(Object result) {
                if (isok) {
                    loveDao.adddata(user.getObjectId());
                    iv_love.setImageResource(R.mipmap.loveyes);
                    lovedToBmob(user.getObjectId());
                    //String data = BmobUser.getCurrentUser(PersonDetailDataActivity.this,User.class).getUsername() + "暗恋了您，快去看看吧~";
                    //push_data(user.getObjectId(),data);
                } else {
                    loveDao.deletedata(user.getObjectId());
                    iv_love.setImageResource(R.mipmap.loveno);
                }
                relativeLayout.setClickable(true);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(PersonDetailDataActivity.this, "操作失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                relativeLayout.setClickable(true);
            }
        });


    }

    /**
     * 把点赞信息存入bmob服务器
     * @param isok 此处添加的消息是增加赞还是取消赞
     */
    public void approveDataToBmpb(final Boolean isok, final RelativeLayout relativeLayout){
        //test对应你刚刚创建的云端逻辑名称
        String cloudCodeName;
        if(isok) {
            cloudCodeName = "addpraise";
        }else {
            cloudCodeName = "subpraise";
        }
        JSONObject params = new JSONObject();
        //name是上传到云端的参数名称，值是bmob，云端逻辑可以通过调用request.body.name获取这个值
        try {
            params.put("userid", user.getObjectId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //创建云端逻辑对象
        AsyncCustomEndpoints cloudCode = new AsyncCustomEndpoints();
        //异步调用云端逻辑
        cloudCode.callEndpoint(PersonDetailDataActivity.this, cloudCodeName, params, new CloudCodeListener() {

            //执行成功时调用，返回result对象
            @Override
            public void onSuccess(Object result) {
                if (isok) {
                    if (approveDao.isExist(user.getObjectId())) {
                        approveDao.updataDay(user.getObjectId(), getDay());
                    } else {
                        approveDao.adddata(user.getObjectId(), getDay());
                    }

                    if (isapprove) {
                        approve_count.setText("赞：" + user.getPraise());
                    } else {
                        approve_count.setText("赞：" + (user.getPraise() + 1));
                    }
                    iv_approve.setImageResource(R.mipmap.person_approve);
                    praiseToBmob(user.getObjectId());


                    /**
                     * 点赞消息推送
                     */
                    //String data = BmobUser.getCurrentUser(PersonDetailDataActivity.this,User.class).getUsername() + "给您点了赞，快去看看吧~";
                    //push_data(user.getObjectId(), data);
                } else {
                    approveDao.deletedata(user.getObjectId());
                    if (isapprove) {
                        approve_count.setText("赞：" + (user.getPraise() - 1));
                    } else {
                        approve_count.setText("赞：" + user.getPraise());
                    }
                    iv_approve.setImageResource(R.mipmap.person_approveno);
                }

                relativeLayout.setClickable(true);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(PersonDetailDataActivity.this, "操作失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                relativeLayout.setClickable(true);
            }
        });

    }

    public String getDay(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = simpleDateFormat.format(date);
        return s.substring(8,10);
    }

    /**
     * 当有人给你点赞的时候
     *
     * @param replyid 被点赞的那个人的id，如果你要查看你的消息
     *                就是看看有谁给你回复了，就是通过这个id去服务器
     *                查找的
     */
    public void praiseToBmob(String replyid){
        MyInfo myInfo = new MyInfo();
        myInfo.setAuthor(BmobUser.getCurrentUser(this, User.class));
        myInfo.setContent(null);
        myInfo.setType1(true);
        myInfo.setType2(false);
        myInfo.setId(null);
        myInfo.setReplyid(replyid);
        myInfo.save(this, new SaveListener() {
            /**
             * 无乱数据上传成功还是失败，都不做处理
             */
            @Override
            public void onSuccess() {
                /**
                 * 数据上传成功
                 */
            }

            @Override
            public void onFailure(int i, String s) {
                /**
                 * 数据上传失败
                 */
            }
        });
    }


    /**
     * 当有人对你表示暗恋的时候
     *
     * @param replyid 被暗恋的那个人的id
     */
    public void lovedToBmob(String replyid){
        MyInfo myInfo = new MyInfo();
        myInfo.setAuthor(BmobUser.getCurrentUser(this, User.class));
        myInfo.setContent(null);
        myInfo.setType1(false);
        myInfo.setType2(true);
        myInfo.setId(null);
        myInfo.setReplyid(replyid);
        myInfo.save(this, new SaveListener() {
            /**
             * 无乱数据上传成功还是失败，都不做处理
             */
            @Override
            public void onSuccess() {
                /**
                 * 数据上传成功
                 */
            }

            @Override
            public void onFailure(int i, String s) {
                /**
                 * 数据上传失败
                 */
            }
        });
    }


    public void push_data(String otherid,String content){
        RequestParams params = new RequestParams();
        params.addBodyParameter("oneID",BmobUser.getCurrentUser(PersonDetailDataActivity.this,User.class).getObjectId());
        params.addBodyParameter("otherID",otherid);
        params.addBodyParameter("content",content);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, "http://newhytc.aliapp.com/push_data", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                /**
                 * 消息推送成功
                 */
            }

            @Override
            public void onFailure(HttpException e, String s) {
                /**
                 * 消息推送失败
                 */
            }
        });
    }






}
