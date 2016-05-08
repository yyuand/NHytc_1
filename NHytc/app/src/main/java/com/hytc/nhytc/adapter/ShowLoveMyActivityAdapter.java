package com.hytc.nhytc.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.hytc.nhytc.domain.ShowLoveComment;
import com.hytc.nhytc.domain.ShuoShuo;
import com.hytc.nhytc.domain.ShuoShuoComment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/2/8.
 */
public class ShowLoveMyActivityAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ShowLove> items;
    private ApproveShowLoveDBDao dbDao;

    public ShowLoveMyActivityAdapter(Activity activity, List<ShowLove> items) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.items = items;
        this.dbDao = new ApproveShowLoveDBDao(activity);
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
            convertView = inflater.inflate(R.layout.activity_my_show_love_item, null);
            holder = new HolderView();

            holder.showLovePerson = (TextView) convertView.findViewById(R.id.tv_my_show_love_person);
            holder.lovedperson = (TextView) convertView.findViewById(R.id.tv_my_loved_person);

            holder.relativeLayout1 = (RelativeLayout) convertView.findViewById(R.id.rl_my_approve_showlove);

            holder.rl_content1 = (RelativeLayout) convertView.findViewById(R.id.rl_my_showlove1);
            holder.rl_content2 = (RelativeLayout) convertView.findViewById(R.id.rl_my_showlove2);
            holder.tocomment = (RelativeLayout) convertView.findViewById(R.id.rl_my_comment_showlove);

            holder.content = (TextView) convertView.findViewById(R.id.my_contant_all_love);
            holder.approve = (ImageView) convertView.findViewById(R.id.iv_my_show_love);
            holder.approvecount = (TextView) convertView.findViewById(R.id.tv_my_show_love);
            holder.commentcount = (TextView) convertView.findViewById(R.id.tv_my_approve_count_show_love);
            holder.tosay = (RelativeLayout) convertView.findViewById(R.id.rl_my_show_love_to_say);
            holder.chat = (ImageView) convertView.findViewById(R.id.iv_my_comment_love);
            holder.showlove = (RelativeLayout) convertView.findViewById(R.id.rl_my_approve_showlove);
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
        final HolderView finalHolder = holder;
        holder.relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.relativeLayout1.setClickable(false);
                if(items.get(position).getIsApprove()){
                    approveshuo(false,items.get(position).getObjectId(),position,finalimageView,finaltvfavoritecount,finalHolder.relativeLayout1);
                }else {
                    approveshuo(true,items.get(position).getObjectId(),position,finalimageView,finaltvfavoritecount,finalHolder.relativeLayout1);
                }
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
                activity.startActivityForResult(intent, 1);
                //activity.startActivity(intent);
            }
        });

        holder.tocomment.setOnClickListener(new View.OnClickListener() {
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
            }
        });

        /**
         * 这里写删除代码（因为太懒，没有改变量名，所以还是用的tosay）
         */
        holder.tosay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(items.get(position).getObjectId(),position);
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
                if (isapprove) {
                    dbDao.adddata(loveid);
                    items.get(position).setIsApprove(true);
                    items.get(position).setApproveCount(items.get(position).getApproveCount() + 1);
                    approvecount.setText(items.get(position).getApproveCount() + "");
                    imageView.setImageResource(R.mipmap.iconfont5);
                } else {
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
                Toast.makeText(activity, "请检查网络连接！", Toast.LENGTH_SHORT).show();
                relativeLayout.setClickable(true);
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

        private RelativeLayout rl_content1;
        private RelativeLayout rl_content2;

        private RelativeLayout tocomment;

        private ImageView chat;
        private RelativeLayout tosay;
        private RelativeLayout showlove;
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

    public void deleteitem(int dposition){
        this.items.remove(dposition);
        notifyDataSetChanged();
    }

    public void deleteData(final String ObJectid,final int nowposition){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("表白删除中...");
        progressDialog.show();
        ShowLove showLove = new ShowLove();
        showLove.setObjectId(ObJectid);
        showLove.delete(activity, new DeleteListener() {
            @Override
            public void onSuccess() {
                deleteitem(nowposition);
                progressDialog.dismiss();
                Toast.makeText(activity, "表白删除成功！", Toast.LENGTH_SHORT).show();
                findCommentid(ObJectid);
            }

            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(activity, "表白删除失败\n请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void findCommentid(String ObJectid){
        BmobQuery<ShowLoveComment> query = new BmobQuery<>();
        query.addWhereEqualTo("showLoveId", ObJectid);
        query.findObjects(activity, new FindListener<ShowLoveComment>() {
            @Override
            public void onSuccess(List<ShowLoveComment> list) {
                deleteCommentData(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    public void deleteCommentData(List<ShowLoveComment> list){
        List<BmobObject> comments = new ArrayList<>();
        comments.addAll(list);
        new BmobObject().deleteBatch(activity, comments, new DeleteListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    public void showDeleteDialog(final String ObJectid,final int nowposition){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("删除");
        builder.setMessage("亲，您确定要删除这条表白么？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteData(ObJectid,nowposition);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create();
        builder.show();
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
