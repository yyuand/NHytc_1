package com.hytc.nhytc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.WebActivity;
import com.hytc.nhytc.domain.Subscribe;
import com.hytc.nhytc.domain.SubscribeDetail;
import com.hytc.nhytc.tool.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/3/4.
 */
public class SubscribeDetailAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<SubscribeDetail> items;
    private BitmapUtils bitmapUtils;

    public SubscribeDetailAdapter(Activity activity, List<SubscribeDetail> items) {
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
            convertView = inflater.inflate(R.layout.subscribe_detail_item,null);
            viewHolder = new ViewHolder();
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.iv_sub_detail);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_sub_detail_name);
            viewHolder.introduce = (TextView) convertView.findViewById(R.id.tv_sub_detail_intro);
            viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.rl_sub_detail);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        bitmapUtils.display(viewHolder.picture, items.get(position).getPicture_url());
        viewHolder.name.setText(items.get(position).getName());
        viewHolder.introduce.setText(items.get(position).getIntroduce());

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(activity, WebActivity.class);
                intent.putExtra("url",items.get(position).getWeb_url());
                activity.startActivity(intent);

            }
        });



        return convertView;
    }


    private class ViewHolder{
        ImageView picture;
        TextView name;
        TextView introduce;
        RelativeLayout relativeLayout;
    }

    /**
     * 添加数据
     *
     * @param items
     */
    public void additems(List<SubscribeDetail> items) {
        this.items.addAll(items);
        notifyDataSetChanged();

    }

    /**
     * 刷新数据
     *
     * @param items
     */
    public void refreshitems(List<SubscribeDetail> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
}
