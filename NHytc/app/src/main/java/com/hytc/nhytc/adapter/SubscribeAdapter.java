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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.SubscribeDetailActivity;
import com.hytc.nhytc.domain.ShuoShuo;
import com.hytc.nhytc.domain.Subscribe;
import com.hytc.nhytc.domain.SubscribeDetail;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.view.CircleImageView;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/3/4.
 */
public class SubscribeAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Subscribe> items;
    private BitmapUtils bitmapUtils;

    public SubscribeAdapter(Activity activity, List<Subscribe> items) {
        this.activity = activity;
        this.items = items;
        this.inflater = LayoutInflater.from(activity);
        this.bitmapUtils = BitmapHelper.getBitmapUtils(activity);
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
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.subscribe_item,null);
            viewHolder = new ViewHolder();
            viewHolder.header = (ImageView) convertView.findViewById(R.id.iv_pic_subscribe);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name_subscribe);
            viewHolder.introduce = (TextView) convertView.findViewById(R.id.tv_introduce_subscribe);
            viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.rl_subscribe_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        bitmapUtils.display(viewHolder.header,items.get(position).getPic_path());
        viewHolder.name.setText(items.get(position).getSub_name());
        viewHolder.introduce.setText(items.get(position).getSub_introduce());

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("subscribe", items.get(position));
                Intent intent = new Intent();
                intent.setClass(activity, SubscribeDetailActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });

        return convertView;
    }






    private class ViewHolder{
        ImageView header;
        TextView name;
        TextView introduce;
        RelativeLayout relativeLayout;
    }


    /**
     * 添加数据
     *
     * @param items
     */
    public void additems(List<Subscribe> items) {
        this.items.addAll(items);
        notifyDataSetChanged();

    }

    /**
     * 刷新数据
     *
     * @param items
     */
    public void refreshitems(List<Subscribe> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
}
