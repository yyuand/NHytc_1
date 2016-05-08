package com.hytc.nhytc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.ShowLoveDetailActivity;
import com.hytc.nhytc.dbDAO.ApproveShowLoveDBDao;
import com.hytc.nhytc.domain.ForResulltMsg;
import com.hytc.nhytc.domain.ShowLove;
import com.hytc.nhytc.domain.ShuoShuo;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.manager.RYfriendManager;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2015/8/16.
 */
public class ShowLoveActivityAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ShowLove> items;
    private ApproveShowLoveDBDao dbDao;
    private User user;

    public ShowLoveActivityAdapter(Activity activity, List<ShowLove> items) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.items = items;
        this.dbDao = new ApproveShowLoveDBDao(activity);
        this.user = BmobUser.getCurrentUser(activity, User.class);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HolderView holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.showlovea_item, null);
            holder = new HolderView();

            holder.showLovePerson = (TextView) convertView.findViewById(R.id.tv_show_love_person);
            holder.lovedperson = (TextView) convertView.findViewById(R.id.tv_loved_person);
            holder.comment = (LinearLayout) convertView.findViewById(R.id.ll_comment_love);

            holder.relativeLayout1 = (RelativeLayout) convertView.findViewById(R.id.rl_approve_showlove);
            holder.relativeLayout2 = (RelativeLayout) convertView.findViewById(R.id.rl_comment_showlove);

            holder.rl_content1 = (RelativeLayout) convertView.findViewById(R.id.rl_showlove1);
            holder.rl_content2 = (RelativeLayout) convertView.findViewById(R.id.rl_showlove2);

            holder.content = (TextView) convertView.findViewById(R.id.contant_all_love);
            holder.approve = (ImageView) convertView.findViewById(R.id.iv_show_love);
            holder.approvecount = (TextView) convertView.findViewById(R.id.tv_show_love);
            holder.commentcount = (TextView) convertView.findViewById(R.id.tv_approve_count_show_love);
            holder.tosay = (RelativeLayout) convertView.findViewById(R.id.rl_show_love_to_say);
            holder.chat = (ImageView) convertView.findViewById(R.id.iv_chart_love);
            holder.showlove = (RelativeLayout) convertView.findViewById(R.id.rl_approve_showlove);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        holder.showLovePerson.setText(items.get(position).getAuthor().getUsername());

        holder.lovedperson.setText(items.get(position).getShowLoveName());

        holder.content.setText(items.get(position).getContent());

        holder.approvecount.setText(items.get(position).getApproveCount()+"");

        holder.commentcount.setText(items.get(position).getCommentCount() + "");


        /**
         * 此处关系于点赞的逻辑
         *
         * 本app点赞的逻辑是把本人所有赞的说说都存到本地数据库中
         * 没获取一条说说，就根据该条说说的id看看本人有没有赞过该
         * 条说说，赞过就setIsApprove(true);
         */
        if(dbDao.isExist(items.get(position).getObjectId())){
            items.get(position).setIsApprove(true);
        }

        /**
         * 处理赞的问题
         */
        if(items.get(position).getIsApprove()){
            holder.approve.setImageResource(R.mipmap.iconfont5);
        }else {
            holder.approve.setImageResource(R.mipmap.iconfont1);
        }


        /**
         * 点赞或取消点赞后的处理
         */
        final ImageView finalimageView = holder.approve;
        final TextView finaltvfavoritecount = holder.approvecount;
        final HolderView finalHolder1 = holder;
        holder.relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder1.relativeLayout1.setClickable(false);
                if(items.get(position).getIsApprove()){
                    approveshuo(false,items.get(position).getObjectId(),position,finalimageView,finaltvfavoritecount,finalHolder1.relativeLayout1);
                }else {
                    approveshuo(true,items.get(position).getObjectId(),position,finalimageView,finaltvfavoritecount,finalHolder1.relativeLayout1);
                }
            }
        });

        holder.relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putSerializable("info", items.get(position));
                Intent intent = new Intent();
                intent.setClass(activity, ShowLoveDetailActivity.class);
                intent.putExtras(data);
                intent.putExtra("isshowdialog", true);
                intent.putExtra("position", position);
                activity.startActivityForResult(intent, 1);
                //activity.startActivity(intent);
            }
        });


        holder.rl_content1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putSerializable("info", items.get(position));
                Intent intent = new Intent();
                intent.setClass(activity, ShowLoveDetailActivity.class);
                intent.putExtras(data);
                intent.putExtra("isshowdialog", false);
                intent.putExtra("position", position);
                activity.startActivityForResult(intent, 1);
                //activity.startActivity(intent);
            }
        });

        holder.rl_content2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putSerializable("info", items.get(position));
                Intent intent = new Intent();
                intent.setClass(activity, ShowLoveDetailActivity.class);
                intent.putExtras(data);
                intent.putExtra("isshowdialog", false);
                intent.putExtra("position", position);
                activity.startActivityForResult(intent,1);
                //activity.startActivity(intent);
            }
        });

        final HolderView finalHolder = holder;
        holder.tosay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.tosay.setClickable(false);
                boolean ismyself = user.getObjectId().equals(items.get(position).getAuthor().getObjectId());
                boolean isexist = RYfriendManager.isexistfriend(activity, items.get(position).getAuthor().getObjectId());
                if(!ismyself && !isexist){
                    RYfriendManager.putdata(activity, items.get(position).getAuthor().getObjectId(), items.get(position).getAuthor().getUsername(), items.get(position).getAuthor().getHeadSculpture());
                }
                if(isexist){
                    RYfriendManager.update(activity,items.get(position).getAuthor().getObjectId(), items.get(position).getAuthor().getUsername(), items.get(position).getAuthor().getHeadSculpture());
                }
                if (ismyself) {
                    Toast.makeText(activity, "亲，这里不能跟自己聊天哦~", Toast.LENGTH_SHORT).show();
                    finalHolder.tosay.setClickable(true);
                }
                else if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(activity, items.get(position).getAuthor().getObjectId(), items.get(position).getAuthor().getUsername());
                    finalHolder.tosay.setClickable(true);
                }
            }
        });




        return convertView;


    }

    /**
     * 处理点赞的方法
     *
     * @param isapprove 本人是否评论过该条表白
     * @param loveid 点赞或取消点赞的那条表白的id
     * @param position 点赞或取消点赞的那条表白的位置
     * @param imageView 点赞或取消点赞时需要改变的那个图片（大拇指）
     * @param approvecount 该条表白的点赞数目
     */
    private void approveshuo(final Boolean isapprove,final String loveid, final int position,final ImageView imageView,final TextView approvecount, final RelativeLayout relativeLayout) {
        ShowLove showLove = new ShowLove();
        showLove.setObjectId(loveid);
        if(isapprove) {
            showLove.increment("approveCount");
        }else {
            showLove.increment("approveCount",-1);
        }
        showLove.update(activity, new UpdateListener() {
            @Override
            public void onSuccess() {
                if (isapprove){
                    dbDao.adddata(loveid);
                    items.get(position).setIsApprove(true);
                    items.get(position).setApproveCount(items.get(position).getApproveCount() + 1);
                    approvecount.setText(items.get(position).getApproveCount() + "");
                    imageView.setImageResource(R.mipmap.iconfont5);
                }else {
                    dbDao.deletedata(loveid);
                    items.get(position).setIsApprove(false);
                    items.get(position).setApproveCount(items.get(position).getApproveCount() - 1);
                    approvecount.setText(items.get(position).getApproveCount() + "");
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



    private class HolderView{
        private TextView showLovePerson;
        private TextView lovedperson;

        private TextView approvecount;
        private TextView commentcount;
        private TextView content;
        private ImageView approve;

        private RelativeLayout relativeLayout1;
        private RelativeLayout relativeLayout2;

        private RelativeLayout rl_content1;
        private RelativeLayout rl_content2;


        private ImageView chat;
        private RelativeLayout tosay;
        private RelativeLayout showlove;
        private LinearLayout comment;
    }


    /**
     * 添加数据
     *
     * @param items
     */
    public void additems(List<ShowLove> items) {
        this.items.addAll(items);
        notifyDataSetChanged();

    }

    /**
     * 刷新数据
     *
     * @param items
     */
    public void refreshitems(List<ShowLove> items) {
        this.items.clear();
        this.items.addAll(items) ;
        notifyDataSetChanged();
    }


    public void upData(ForResulltMsg msg){
        this.items.get(msg.getPosition()).setCommentCount(Integer.valueOf(msg.getCommentcount()));
        this.items.get(msg.getPosition()).setApproveCount(Integer.valueOf(msg.getApprovecount()));
        if(msg.getStatus()){
            this.items.get(msg.getPosition()).setIsApprove(!this.items.get(msg.getPosition()).getIsApprove());
        }
        notifyDataSetChanged();
    }


}
