package com.hytc.nhytc.manager;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.ShuocommentAdapter;
import com.hytc.nhytc.dbDAO.ApproveShuoDBDao;
import com.hytc.nhytc.domain.MyInfo;
import com.hytc.nhytc.domain.ProgresMsgEB;
import com.hytc.nhytc.domain.ShowLove;
import com.hytc.nhytc.domain.ShuoShuo;
import com.hytc.nhytc.domain.ShuoShuoComment;
import com.hytc.nhytc.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/1/29.
 */
public class ShuoShuoDetailManager {
    private Activity activity;
    private ShuoShuo shuoshuo;
    private ListView listView;
    private View view;
    private ShuocommentAdapter adapter;
    private TextView commentcount;
    private ApproveShuoDBDao dbDao;

    private String endTime;

    public ShuoShuoDetailManager(Activity activity,ShuoShuo shuoshuo,ListView listView,View view, final TextView commentcont) {
        this.activity = activity;
        this.shuoshuo = shuoshuo;
        this.listView = listView;
        this.commentcount = commentcont;
        this.view = view;
        this.dbDao = new ApproveShuoDBDao(activity);
    }




    public void getcommentdialog(final Boolean commentType, final ShuoShuoComment shuoShuoComment){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater1 = LayoutInflater.from(activity);
        final View view = inflater1.inflate(R.layout.commentedit, null);
        final EditText commenttext = (EditText) view.findViewById(R.id.et_comment_love);
        if(!commentType){
            StringBuilder hints = new StringBuilder();
            hints.append("回复：")
                  .append(shuoShuoComment.getCommentUser().getUsername());
            commenttext.setHint(hints);
        }
        builder.setView(view);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = commenttext.getText().toString();
                if("".equals(content)){
                    Toast.makeText(activity, "评论内容不能为空！", Toast.LENGTH_SHORT).show();
                }else {
                    if (commentType) {
                        addcomment(content, shuoshuo.getObjectId(), true, null);
                    } else {
                        addcomment(content, shuoshuo.getObjectId(), false, shuoShuoComment);
                    }
                }
            }
        });
        builder.create();
        builder.show();
    }

    public void initTitle(TextView titlename,ImageView ivinfo,TextView tvinfo,ImageView ivmore) {



        titlename.setText("详情");
        ivinfo.setVisibility(View.GONE);
        tvinfo.setVisibility(View.GONE);
        ivmore.setVisibility(View.GONE);


    }


    /**
     *
     * @param content 评论的内容
     * @param shuoshuoid 评论所在说说的id
     * @param commentType 评论的类型
     *                    true：是评论说说的
     *                    false：是回复别人的评论的
     * @param shuoShuoComment 如果 commentType 为true，其值为空
     *                        如果 commentType 为false，其值为被回复的评论
     */
    public void addcomment(final String content, final String shuoshuoid, final Boolean commentType, final ShuoShuoComment shuoShuoComment){
        final ShuoShuoComment parseComment = new ShuoShuoComment();
        parseComment.setContent(content);
        parseComment.setShuoshuo(shuoshuoid);
        parseComment.setCommentUser(BmobUser.getCurrentUser(activity, User.class));
        parseComment.setCommentType(commentType);
        if(!commentType){
            parseComment.setCommentedConment(shuoShuoComment);
        }
        parseComment.save(activity, new SaveListener() {
            @Override
            public void onSuccess() {
                adapter.additems(parseComment);
                addCommentCount();
                if (!commentType) {
                    replycommentToBmob(BmobUser.getCurrentUser(activity, User.class), content, shuoshuoid, shuoShuoComment.getCommentUser().getObjectId());
                }
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(activity, "请检查网络连接！", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 获取所有的评论
     * @param progressBar
     */
    public void getallcommment(final ProgressBar progressBar) {
        BmobQuery<ShuoShuoComment> query = new BmobQuery<>();
        query.addWhereEqualTo("shuoshuoid", shuoshuo.getObjectId());
        query.setLimit(1200);
        query.order("createdAt");
        query.include("commentUser,commentedConment,commentedConment.commentUser");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(activity, new FindListener<ShuoShuoComment>() {
            @Override
            public void onSuccess(List<ShuoShuoComment> list) {
                if(list.size()!=0) {
                    endTime = list.get(list.size() - 1).getCreatedAt();
                }
                adapter = new ShuocommentAdapter(activity, list);
                listView.addHeaderView(view);

                progressBar.setVisibility(View.GONE);

                listView.setAdapter(adapter);
                listView.setDividerHeight(0);

                EventBus.getDefault().post(new ProgresMsgEB("success"));
            }

            @Override
            public void onError(int i, String s) {
                if (i == 9015 || i == 9009) {
                    List<ShuoShuoComment> list = new ArrayList<>();
                    adapter = new ShuocommentAdapter(activity, list);
                    listView.addHeaderView(view);

                    progressBar.setVisibility(View.GONE);

                    listView.setAdapter(adapter);
                    listView.setDividerHeight(0);

                    EventBus.getDefault().post(new ProgresMsgEB("success"));
                } else {
                    progressBar.setVisibility(View.GONE);
                    activity.findViewById(R.id.nothing_for_internet).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 下拉刷新
     */
    public void onHeader(final AbPullToRefreshView abpull){
        BmobQuery<ShuoShuoComment> query = new BmobQuery<>();
        query.addWhereEqualTo("shuoshuoid", shuoshuo.getObjectId());
        query.setLimit(1200);
        query.order("createdAt");
        query.include("commentUser,commentedConment,commentedConment.commentUser");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存1天
        query.findObjects(activity, new FindListener<ShuoShuoComment>() {
            @Override
            public void onSuccess(List<ShuoShuoComment> list) {
                if(list.size()!=0) {
                    endTime = list.get(list.size() - 1).getCreatedAt();
                }
                adapter.onFresh(list);
                abpull.onHeaderRefreshFinish();
            }

            @Override
            public void onError(int i, String s) {
                abpull.onHeaderRefreshFinish();
            }
        });
    }

    public void onFoot(final AbPullToRefreshView abpull){
        BmobQuery<ShuoShuoComment> query = new BmobQuery<>();
        query.addWhereEqualTo("shuoshuoid", shuoshuo.getObjectId());
        query.setLimit(1200);
        query.order("createdAt");
        query.include("commentUser,commentedConment,commentedConment.commentUser");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存1天
        query.findObjects(activity, new FindListener<ShuoShuoComment>() {
            @Override
            public void onSuccess(List<ShuoShuoComment> list) {
                if(list.size()!=0) {
                    endTime = list.get(list.size() - 1).getCreatedAt();
                }
                adapter.onFresh(list);
                listView.setSelection(list.size() - 1);
                abpull.onFooterLoadFinish();
            }

            @Override
            public void onError(int i, String s) {
                abpull.onFooterLoadFinish();
            }
        });
    }


    /**
     * 当发起回复成功后，就到说说表中把CommentCount自增1
     */
    private void addCommentCount() {
        final ShuoShuo shuoShuo = new ShuoShuo();
        shuoShuo.setObjectId(shuoshuo.getObjectId());
        shuoShuo.increment("CommentCount");
        shuoShuo.update(activity, new UpdateListener() {
            @Override
            public void onSuccess() {
                commentcount.setText(String.valueOf(Integer.valueOf(commentcount.getText().toString()) + 1));
            }

            @Override
            public void onFailure(int i, String s) {
            }
        });
    }


    /**
     * 处理点赞的方法
     * @param isapprove 本人是否评论过该条说说
     * @param approvecount 该条说说的点赞数目
     */
    public void approveshuo(final Boolean isapprove, final TextView approvecount, final ImageView imageView, final RelativeLayout relativeLayout) {
        ShuoShuo shuoShuo = new ShuoShuo();
        shuoShuo.setObjectId(shuoshuo.getObjectId());
        if(isapprove) {
            shuoShuo.increment("approveCount");
        }else {
            shuoShuo.increment("approveCount",-1);
        }
        shuoShuo.update(activity, new UpdateListener() {
            @Override
            public void onSuccess() {
                if (isapprove){
                    dbDao.adddata(shuoshuo.getObjectId());
                    shuoshuo.setIsApprove(true);
                    shuoshuo.setApproveCount(shuoshuo.getApproveCount() + 1);
                    approvecount.setText(shuoshuo.getApproveCount() + "");
                    imageView.setImageResource(R.mipmap.iconfont5);
                }else {
                    dbDao.deletedata(shuoshuo.getObjectId());
                    shuoshuo.setIsApprove(false);
                    shuoshuo.setApproveCount(shuoshuo.getApproveCount() - 1);
                    approvecount.setText(shuoshuo.getApproveCount() + "");
                    imageView.setImageResource(R.mipmap.iconfont1);
                }
                relativeLayout.setClickable(true);
            }

            @Override
            public void onFailure(int i, String s) {
                relativeLayout.setClickable(true);
                Toast.makeText(activity, "请检查网络连接！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ShuoShuoComment getShuoComment(int position){
        return adapter.getcommentitem(position);
    }

    /**
     * 当有人回复某人的评论时呢
     *
     * @param user 发起回复的人
     * @param content 回复的内容
     * @param id 说说的id，表示这个回复在哪一条说说中
     * @param replyid 被回复的那个人的id，如果你要查看你的消息
     *                就是看看有谁给你回复了，就是通过这个id去服务器
     *                查找的
     */
    public void replycommentToBmob(User user,String content,String id,String replyid){
        MyInfo myInfo = new MyInfo();
        myInfo.setAuthor(user);
        myInfo.setContent(content);
        myInfo.setType1(true);
        myInfo.setType2(true);
        myInfo.setId(id);
        myInfo.setReplyid(replyid);
        myInfo.save(activity, new SaveListener() {
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

}
