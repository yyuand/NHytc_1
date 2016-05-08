package com.hytc.nhytc.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.PersonDetailDataActivity;
import com.hytc.nhytc.activity.ShowLoveDetailActivity;
import com.hytc.nhytc.activity.ShuoshuoDetailActivity;
import com.hytc.nhytc.domain.MyInfo;
import com.hytc.nhytc.domain.ShowLove;
import com.hytc.nhytc.domain.ShuoShuo;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.tool.ShowTimeTools;
import com.hytc.nhytc.view.CircleImageView;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/3/1.
 */
public class MyInfoItemAdapter extends BaseAdapter {
    private Context context;
    private List<MyInfo> items;
    private LayoutInflater inflater;
    private BitmapUtils bitmapUtils;
    private ProgressDialog progressDialog;

    public MyInfoItemAdapter(Context context,List<MyInfo> items) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
        this.bitmapUtils = BitmapHelper.getBitmapUtils(context);
        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setMessage("信息获取跳转中，请稍等...");
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
            convertView = inflater.inflate(R.layout.myinfoitem,null);
            viewHolder.head = (CircleImageView) convertView.findViewById(R.id.iv_myinfo_head);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_myinfo_username);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time_myinfo);
            viewHolder.replyThye = (TextView) convertView.findViewById(R.id.tv_title_myinfo_item);
            viewHolder.content = (TextView) convertView.findViewById(R.id.tv_content_myinfo_item);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_myinfo_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        bitmapUtils.display(viewHolder.head, items.get(position).getAuthor().getHeadSculpture());
        viewHolder.name.setText(items.get(position).getAuthor().getUsername());
        viewHolder.time.setText(ShowTimeTools.getShowTime(items.get(position).getCreatedAt()));
        if(items.get(position).getType1() == items.get(position).getType2()){
            viewHolder.replyThye.setText("回复了您");
            viewHolder.content.setText(items.get(position).getContent());
        }else if(items.get(position).getType1() && !items.get(position).getType2()){
            viewHolder.replyThye.setText("赞");
            viewHolder.content.setText(items.get(position).getAuthor().getUsername()+" 给您点了赞");
        }else if(!items.get(position).getType1() && items.get(position).getType2()){
            viewHolder.replyThye.setText("暗恋");
            viewHolder.content.setText(items.get(position).getAuthor().getUsername()+" 暗恋了您");
        }

        /**
         * 点击头像查看个人详情
         */
        viewHolder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("person_data",items.get(position).getAuthor());
                Intent intent = new Intent();
                intent.setClass(context, PersonDetailDataActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        /**
         * 点击整体，查看具体说说或者表白或个人详情
         *
         *
         *
         */
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(items.get(position).getType1() != items.get(position).getType2()){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("person_data",items.get(position).getAuthor());
                    Intent intent = new Intent();
                    intent.setClass(context, PersonDetailDataActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else if(items.get(position).getType1() && items.get(position).getType2()){
                    findshuoshuo(items.get(position).getId());
                }else if(!items.get(position).getType1() && !items.get(position).getType2()){
                    findshowLove(items.get(position).getId());
                }
            }
        });



        return convertView;
    }



    private class ViewHolder{
        private CircleImageView head;
        private TextView name;
        private TextView time;
        private TextView replyThye;
        private TextView content;
        private LinearLayout linearLayout;
    }


    /**
     * 添加数据
     *
     * @param items
     */
    public void additems(List<MyInfo> items) {
        this.items.addAll(items);
        notifyDataSetChanged();

    }

    /**
     * 刷新数据
     *
     * @param items
     */
    public void refreshitems(List<MyInfo> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 根据某条说说的id查找说说详情
     * @param shuoshuoid 说说的id
     */
    public void findshuoshuo(String shuoshuoid){
        progressDialog.show();
        BmobQuery<ShuoShuo> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId",shuoshuoid);
        query.include("author");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(3));//此表示缓存3天
        query.findObjects(context, new FindListener<ShuoShuo>() {
            @Override
            public void onSuccess(List<ShuoShuo> list) {
                progressDialog.dismiss();
                if (list.size() == 0) {
                    Toast.makeText(context, "查看详情失败", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle data = new Bundle();
                    data.putSerializable("shuoinfo", list.get(0));
                    Intent intent = new Intent();
                    intent.setClass(context, ShuoshuoDetailActivity.class);
                    intent.putExtras(data);
                    intent.putExtra("isshowdialog", false);
                    intent.putExtra("position", 0);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onError(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(context, "查看详情失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 根据showloveid获取表白详情
     * @param showloveid 表白的id
     */
    public void findshowLove(String showloveid){
        progressDialog.show();
        BmobQuery<ShowLove> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId",showloveid);
        query.include("author");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(3));//此表示缓存3天
        query.findObjects(context, new FindListener<ShowLove>() {
            @Override
            public void onSuccess(List<ShowLove> list) {
                progressDialog.dismiss();
                if (list.size() == 0) {
                    Toast.makeText(context, "查看详情失败", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle data = new Bundle();
                    data.putSerializable("info", list.get(0));
                    Intent intent = new Intent();
                    intent.setClass(context, ShowLoveDetailActivity.class);
                    intent.putExtras(data);
                    intent.putExtra("isshowdialog", false);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onError(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(context, "查看详情失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
