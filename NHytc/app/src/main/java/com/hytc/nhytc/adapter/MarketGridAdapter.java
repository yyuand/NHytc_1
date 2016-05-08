package com.hytc.nhytc.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hytc.nhytc.R;


/**
 * Created by Administrator on 2015/8/19.
 */
public class MarketGridAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private int[] icons;

    public MarketGridAdapter(Activity activity) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        icons = new int[]{R.drawable.m1,R.drawable.m2,R.drawable.m3,R.drawable.m4,R.drawable.m5,R.drawable.m6,R.drawable.m7,R.drawable.m8};
    }

    @Override
    public int getCount() {
        return 8;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.activity_market_gride_item,null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_market_grid_item);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_market_grid_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(icons[position]);
        viewHolder.textView.setText(activity.getResources().getStringArray(R.array.market_item)[position]);
        return convertView;
    }
    private class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
