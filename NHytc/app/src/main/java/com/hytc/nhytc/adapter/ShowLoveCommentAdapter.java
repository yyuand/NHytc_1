package com.hytc.nhytc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.PersonDetailDataActivity;
import com.hytc.nhytc.domain.ShowLoveComment;
import com.hytc.nhytc.domain.ShuoShuoComment;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.tool.ShowTimeTools;
import com.hytc.nhytc.view.CircleImageView;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/2/1.
 */
public class ShowLoveCommentAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ShowLoveComment> items;
    private BitmapUtils bitmapUtils;

    public ShowLoveCommentAdapter(Activity activity,List<ShowLoveComment> items) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.items = items;
        bitmapUtils = BitmapHelper.getBitmapUtils(activity.getApplicationContext());
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
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.commentshuoitem,null);
            holder.head = (CircleImageView) convertView.findViewById(R.id.comment_shuo_head);
            holder.name = (TextView) convertView.findViewById(R.id.tv_commentname_shuo);
            holder.time = (TextView) convertView.findViewById(R.id.tv_commenttime);
            holder.content = (TextView) convertView.findViewById(R.id.tv_comtent_comment);
            holder.contented = (TextView) convertView.findViewById(R.id.tv_comtented_comment);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        bitmapUtils.display(holder.head, items.get(position).getAuthor().getHeadSculpture());
        holder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("person_data", items.get(position).getAuthor());
                Intent intent = new Intent();
                intent.setClass(activity, PersonDetailDataActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });

        holder.name.setText(items.get(position).getAuthor().getUsername());
        holder.time.setText(ShowTimeTools.getShowTime(items.get(position).getCreatedAt()));
        holder.content.setText(items.get(position).getContent());

        if(!items.get(position).getCommentType()){
            StringBuilder s = new StringBuilder();
            s.append("回复 ")
                    .append(items.get(position).getCommentedConment().getAuthor().getUsername())
                    .append(": ")
                    .append(items.get(position).getCommentedConment().getContent());
            holder.contented.setText(s);
        }else {
            holder.contented.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder{
        CircleImageView head;
        TextView name;
        TextView time;
        TextView content;
        TextView contented;
    }

    /**
     * 添加数据
     *
     * @param items
     */
    public void additems(ShowLoveComment items) {
        this.items.add(items);
        notifyDataSetChanged();
    }

    public void reFresh(List<ShowLoveComment> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 获取一条评论
     * @param position
     * @return
     */
    public ShowLoveComment getcommentitem(int position){
        return items.get(position);
    }
}
