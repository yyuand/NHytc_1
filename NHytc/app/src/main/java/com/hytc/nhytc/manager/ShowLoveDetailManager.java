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
import com.hytc.nhytc.adapter.ShowLoveCommentAdapter;
import com.hytc.nhytc.dbDAO.ApproveShowLoveDBDao;
import com.hytc.nhytc.domain.MyInfo;
import com.hytc.nhytc.domain.ProgresMsgEB;
import com.hytc.nhytc.domain.ShowLove;
import com.hytc.nhytc.domain.ShowLoveComment;
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
 * Created by Administrator on 2016/2/1.
 */
public class ShowLoveDetailManager  {
    private Activity activity;
    private ShowLove showLove;
    private ListView listView;
    private View view;
    private ShowLoveCommentAdapter adapter;
    private TextView commentcount;
    private ApproveShowLoveDBDao dbDao;

    private String endTime;

    public ShowLoveDetailManager(Activity activity,ShowLove showLove,ListView listView,View view, final TextView commentcont) {
        this.activity = activity;
        this.showLove = showLove;
        this.listView = listView;
        this.commentcount = commentcont;
        this.view = view;
        this.dbDao = new ApproveShowLoveDBDao(activity);
    }

    public void getcommentdialog(final Boolean commentType, final ShowLoveComment showLoveComment){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater1 = LayoutInflater.from(activity);
        final View view = inflater1.inflate(R.layout.commentedit, null);
        final EditText commenttext = (EditText) view.findViewById(R.id.et_comment_love);
        if(!commentType){
            StringBuilder hints = new StringBuilder();
            hints.append("回复：")
                    .append(showLoveComment.getAuthor().getUsername());
            commenttext.setHint(hints);
        }
        builder.setView(view);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //EditText commenttext = (EditText) view.findViewById(R.id.et_comment_love);
                String content = commenttext.getText().toString();
                if ("".equals(content)) {
                    Toast.makeText(activity, "评论内容不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    if (commentType) {
                        addcomment(content, showLove.getObjectId(), true, null);
                    } else {
                        addcomment(content, showLove.getObjectId(), false, showLoveComment);
                    }
                }
            }
        });
        builder.create();
        builder.show();
    }


    /**
     *
     * @param content 评论的内容
     * @param showloveid 评论所在说说的id
     * @param commentType 评论的类型
     *                    true：是评论说说的
     *                    false：是回复别人的评论的
     * @param showLoveComment 如果 commentType 为true，其值为空
     *                        如果 commentType 为false，其值为被回复的评论
     */
    public void addcomment(final String content, final String showloveid, final Boolean commentType, final ShowLoveComment showLoveComment){
        final ShowLoveComment parseComment = new ShowLoveComment();
        parseComment.setContent(content);
        parseComment.setShowLoveId(showloveid);
        parseComment.setAuthor(BmobUser.getCurrentUser(activity, User.class));
        parseComment.setCommentType(commentType);
        if(!commentType){
            parseComment.setCommentedConment(showLoveComment);
        }
        parseComment.save(activity, new SaveListener() {
            @Override
            public void onSuccess() {
                adapter.additems(parseComment);
                addCommentCount();
                if(!commentType){
                    replycommentToBmob(BmobUser.getCurrentUser(activity, User.class),content,showloveid,showLoveComment.getAuthor().getObjectId());
                }
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(activity, "请检查网络连接！", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void getallcommment(final ProgressBar progressBar) {
        BmobQuery<ShowLoveComment> query = new BmobQuery<>();
        query.addWhereEqualTo("showLoveId", showLove.getObjectId());
        query.setLimit(500);
        query.order("createdAt");
        query.include("author,commentedConment,commentedConment.author");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(activity, new FindListener<ShowLoveComment>() {
            @Override
            public void onSuccess(List<ShowLoveComment> list) {
                endTime = list.get(list.size() - 1).getCreatedAt();
                adapter = new ShowLoveCommentAdapter(activity, list);
                listView.addHeaderView(view);
                progressBar.setVisibility(View.GONE);
                listView.setAdapter(adapter);
                listView.setDividerHeight(0);

                EventBus.getDefault().post(new ProgresMsgEB("success"));
            }

            @Override
            public void onError(int i, String s) {
                Log.e("koko", i + s);
                if (i == 9015 || i == 9009) {
                    List<ShowLoveComment> list = new ArrayList<>();
                    adapter = new ShowLoveCommentAdapter(activity, list);
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
        BmobQuery<ShowLoveComment> query = new BmobQuery<>();
        query.addWhereEqualTo("showLoveId", showLove.getObjectId());
        query.setLimit(500);
        query.order("createdAt");
        query.include("author,commentedConment,commentedConment.author");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存1天
        query.findObjects(activity, new FindListener<ShowLoveComment>() {
            @Override
            public void onSuccess(List<ShowLoveComment> list) {
                endTime = list.get(list.size() - 1).getCreatedAt();
                adapter.reFresh(list);
                abpull.onHeaderRefreshFinish();
            }

            @Override
            public void onError(int i, String s) {
                abpull.onHeaderRefreshFinish();
            }
        });
    }

    public void onFoot(final AbPullToRefreshView abpull){
        BmobQuery<ShowLoveComment> query = new BmobQuery<>();
        query.addWhereEqualTo("showLoveId", showLove.getObjectId());
        query.setLimit(500);
        query.order("createdAt");
        query.include("author,commentedConment,commentedConment.author");boolean isCache = query.hasCachedResult(activity,ShowLoveComment.class);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存1天

        query.findObjects(activity, new FindListener<ShowLoveComment>() {
            @Override
            public void onSuccess(List<ShowLoveComment> list) {
                endTime = list.get(list.size() - 1).getCreatedAt();
                adapter.reFresh(list);
                listView.setSelection(list.size() - 1);
                abpull.onFooterLoadFinish();
            }

            @Override
            public void onError(int i, String s) {
                abpull.onFooterLoadFinish();
            }
        });
    }

    private void addCommentCount() {
        ShowLove showlove = new ShowLove();
        showlove.setObjectId(showLove.getObjectId());
        showlove.increment("commentCount");
        showlove.update(activity, new UpdateListener() {
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
     * @param isapprove 本人是否可以评论该条说说
     * @param approvecount 该条说说的点赞数目
     */
    public void approvelove(final Boolean isapprove, final TextView approvecount, final ImageView imageView, final RelativeLayout relativeLayout) {
        ShowLove showlove = new ShowLove();
        showlove.setObjectId(showLove.getObjectId());
        if(isapprove) {
            showlove.increment("approveCount");
        }else {
            showlove.increment("approveCount",-1);
        }
        showlove.update(activity, new UpdateListener() {
            @Override
            public void onSuccess() {
                if (isapprove){
                    dbDao.adddata(showLove.getObjectId());
                    showLove.setIsApprove(true);
                    showLove.setApproveCount(showLove.getApproveCount() + 1);
                    approvecount.setText(showLove.getApproveCount() + "");
                    imageView.setImageResource(R.mipmap.iconfont5);
                }else {
                    dbDao.deletedata(showLove.getObjectId());
                    showLove.setIsApprove(false);
                    showLove.setApproveCount(showLove.getApproveCount() - 1);
                    approvecount.setText(showLove.getApproveCount() + "");
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

    public ShowLoveComment getloveComment(int position){
        return adapter.getcommentitem(position);
    }

    /**
     * 当有人回复某人的评论时呢
     *
     * @param user 发起回复的人
     * @param content 回复的内容
     * @param id 表白的id，表示这个回复在哪一条表白中
     * @param replyid 被回复的那个人的id，如果你要查看你的消息
     *                就是看看有谁给你回复了，就是通过这个id去服务器
     *                查找的
     */
    public void replycommentToBmob(User user,String content,String id,String replyid){
        MyInfo myInfo = new MyInfo();
        myInfo.setAuthor(user);
        myInfo.setContent(content);
        myInfo.setType1(false);
        myInfo.setType2(false);
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
