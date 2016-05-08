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


/**
 * Created by Administrator on 2015/8/13.
 */
public class HomeFragmentAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    //private Myapplication myapplication;

    private int iconid[] = {

            //R.mipmap.iconfontcommunity,
            R.mipmap.iconfontcalendar,
            R.mipmap.iconfontservice,
            R.mipmap.iconfontcart,
            R.mipmap.iconfontapps,
            /*R.drawable.iconfontvipcard,
            R.drawable.iconfontsearchlist,
            R.drawable.iconfontshop,*/
            R.mipmap.iconfontphone,
            //R.mipmap.iconfontfriendfill
    };

    public HomeFragmentAdapter(Activity activity) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
    }


    @Override
    public int getCount() {
        return 3;
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
            convertView = inflater.inflate(R.layout.home_list_adapter_item,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView1 = (ImageView) convertView.findViewById(R.id.iv_homeleft_list);
            viewHolder.imageView2 = (ImageView) convertView.findViewById(R.id.iv_homeright_list);
            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.tv_homeleft_list);
            viewHolder.textView2 = (TextView) convertView.findViewById(R.id.tv_homeright_list);
            viewHolder.relativelayoutleft = (RelativeLayout) convertView.findViewById(R.id.rl_home_item_left);
            viewHolder.relativelayoutright = (RelativeLayout) convertView.findViewById(R.id.rl_home_item_right);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView1.setImageResource(iconid[position * 2]);
        viewHolder.imageView2.setImageResource(iconid[position * 2 + 1]);
        viewHolder.textView1.setText(activity.getResources().getTextArray(R.array.icon_home)[position * 2]);
        viewHolder.textView2.setText(activity.getResources().getTextArray(R.array.icon_home)[position * 2 + 1]);

        viewHolder.relativelayoutleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        /*Intent intent = new Intent();
                        intent.setClass(activity, ShuoshuoActivity.class);
                        activity.startActivity(intent);*/
                        break;
                    case 1:
                        /*Intent intent1 = new Intent();
                        intent1.setClass(activity, LostActivity.class);
                        activity.startActivity(intent1);*/
                        break;
                    /*case 2:
                        Intent intent_syllabus = new Intent();
                        intent_syllabus.setClass(activity, SyllabusActivity.class);
                        activity.startActivity(intent_syllabus);
                        break;
                    case 3:
                        Toast.makeText(activity, "6", Toast.LENGTH_SHORT).show();
                        break;*/
                    case 2:
                        /*Intent intent4 = new Intent();
                        intent4.setClass(activity, PhoneActivity.class);
                        activity.startActivity(intent4);*/
                        break;
                }
            }
        });

        viewHolder.relativelayoutright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        /*Intent intent00 = new Intent();
                        intent00.setClass(activity, ShowLoveActivity.class);
                        activity.startActivity(intent00);*/
                        break;
                    case 1:
                        /*Intent intent11 = new Intent();
                        intent11.setClass(activity, MarketActivity.class);
                        activity.startActivity(intent11);*/
                        break;
                    /*case 2:
                        Toast.makeText(activity, "5", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(activity, "7", Toast.LENGTH_SHORT).show();
                        break;*/
                    case 2:
                        /*Intent intent = new Intent();
                        intent.setClass(activity, FirstHeadWebActivity.class);
                        intent.putExtra("page",8);
                        activity.startActivity(intent);*/
                        break;
                }
            }
        });

        return convertView;
    }

    private final class ViewHolder{
        private ImageView imageView1;
        private ImageView imageView2;
        private TextView textView1;
        private TextView textView2;
        private RelativeLayout relativelayoutleft;
        private RelativeLayout relativelayoutright;
    }

}
