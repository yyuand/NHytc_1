package com.hytc.nhytc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.MerchandiseDetailActivity;
import com.hytc.nhytc.activity.PersonDetailDataActivity;
import com.hytc.nhytc.domain.Merchandise;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.tool.ShowTimeTools;
import com.hytc.nhytc.view.CircleImageView;
import com.hytc.nhytc.view.MyGirdView;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/19.
 */
public class MarketListAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<Merchandise> items;
    private BitmapUtils bitmapUtils;

    public MarketListAdapter(Activity activity, List<Merchandise> items) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.items = items;
        this.bitmapUtils = BitmapHelper.getBitmapUtils(activity.getApplicationContext());
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
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.activity_market_item,null);
            viewHolder.head = (CircleImageView) convertView.findViewById(R.id.iv_activity_market);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name_mer);
            viewHolder.faculty = (TextView) convertView.findViewById(R.id.tv_faculty_mer);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time_mer);
            viewHolder.money = (TextView) convertView.findViewById(R.id.tv_activity_market);
            viewHolder.describe = (TextView) convertView.findViewById(R.id.tv_mer_describe);
            viewHolder.myGirdView = (MyGirdView) convertView.findViewById(R.id.gl_activity_market_item);

            viewHolder.relativeLayout1 = (RelativeLayout) convertView.findViewById(R.id.rl_activity_market);
            viewHolder.relativeLayout2 = (RelativeLayout) convertView.findViewById(R.id.rl_mer_describe);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        bitmapUtils.display(viewHolder.head, items.get(position).getAuthor().getHeadSculpture());
        viewHolder.head.setOnClickListener(new View.OnClickListener() {
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


        viewHolder.name.setText(items.get(position).getAuthor().getUsername());
        viewHolder.faculty.setText(items.get(position).getAuthor().getFaculty());
        viewHolder.time.setText(ShowTimeTools.getShowTime(items.get(position).getCreatedAt()));
        viewHolder.money.setText("￥" + items.get(position).getPrice());
        viewHolder.describe.setText(items.get(position).getMerchandiseName());
        ArrayList<String> pics = new ArrayList<>();
        for (Object s : items.get(position).getPictures()) {
            pics.add(String.valueOf(s));
        }
        PicsGirdAdapter adapter = new PicsGirdAdapter(activity,pics,9);
        viewHolder.myGirdView.setAdapter(adapter);

        viewHolder.relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putSerializable("merchandise_info", items.get(position));
                Intent intent2 = new Intent();
                intent2.setClass(activity, MerchandiseDetailActivity.class);
                intent2.putExtras(data);
                activity.startActivity(intent2);
            }
        });
        viewHolder.relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putSerializable("merchandise_info",items.get(position));
                Intent intent2 = new Intent();
                intent2.setClass(activity,MerchandiseDetailActivity.class);
                intent2.putExtras(data);
                activity.startActivity(intent2);
            }
        });
        return convertView;
    }


    private class ViewHolder{
        CircleImageView head;
        TextView name;
        TextView faculty;
        TextView time;
        TextView money;
        TextView describe;
        MyGirdView myGirdView;

        RelativeLayout relativeLayout1;
        RelativeLayout relativeLayout2;
    }

    /**
     * 添加数据
     *
     * @param items
     */
    public void additems(List<Merchandise> items) {
        this.items.addAll(items);
        notifyDataSetChanged();

    }

    /**
     * 刷新数据
     *
     * @param items
     */
    public void refreshitems(List<Merchandise> items) {
        this.items.clear();
        this.items.addAll(items) ;
        notifyDataSetChanged();
    }

}
