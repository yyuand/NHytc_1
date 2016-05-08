package com.hytc.nhytc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.PersonDetailDataActivity;
import com.hytc.nhytc.domain.LostBack;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.manager.RYfriendManager;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.tool.ShowTimeTools;
import com.hytc.nhytc.view.CircleImageView;
import com.hytc.nhytc.view.MyGirdView;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2015/8/18.
 */
public class LostActivityItemAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<LostBack> items;
    private BitmapUtils bitmapUtils;
    private User user;

    public LostActivityItemAdapter(Activity activity, List<LostBack> items) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.items = items;
        this.bitmapUtils = BitmapHelper.getBitmapUtils(activity.getApplicationContext());
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
        HolderView holderView = null;
        if(convertView == null){
            holderView = new HolderView();
            convertView = inflater.inflate(R.layout.activity_lost_list_item,null);
            holderView.head = (CircleImageView) convertView.findViewById(R.id.iv_lost_head);
            holderView.name = (TextView) convertView.findViewById(R.id.tv_username_lost);
            holderView.time = (TextView) convertView.findViewById(R.id.tv_time_lost);
            holderView.lostname = (TextView) convertView.findViewById(R.id.tv_lost_name);
            holderView.lostlocation = (TextView) convertView.findViewById(R.id.tv_lost_location);
            holderView.losttime = (TextView) convertView.findViewById(R.id.tv_lost_time);
            holderView.lostcontent = (TextView) convertView.findViewById(R.id.tv_content_lost_list_item);
            holderView.myGirdView = (MyGirdView) convertView.findViewById(R.id.gl_picture_lost_list_item);
            holderView.linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_context_lost_item);
            convertView.setTag(holderView);
        }else {
            holderView = (HolderView) convertView.getTag();
        }
        bitmapUtils.display(holderView.head, items.get(position).getAuthor().getHeadSculpture());

        holderView.head.setOnClickListener(new View.OnClickListener() {
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
        

        holderView.name.setText(items.get(position).getAuthor().getUsername());
        holderView.time.setText(ShowTimeTools.getShowTime(items.get(position).getCreatedAt()));
        holderView.lostname.setText(items.get(position).getLostName());
        holderView.lostlocation.setText(items.get(position).getLostLocation());
        holderView.losttime.setText(items.get(position).getLostTime());
        holderView.lostcontent.setText(items.get(position).getContent());
        ArrayList<String> pics = new ArrayList<>();
        for (Object s : items.get(position).getPictures()) {
            pics.add(String.valueOf(s));
        }
        PicsGirdAdapter adapter = new PicsGirdAdapter(activity,pics,9);
        holderView.myGirdView.setAdapter(adapter);
        holderView.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean ismyself = user.getObjectId().equals(items.get(position).getAuthor().getObjectId());
                boolean isexist = RYfriendManager.isexistfriend(activity, items.get(position).getAuthor().getObjectId());
                if(!ismyself && !isexist){
                    RYfriendManager.putdata(activity, items.get(position).getAuthor().getObjectId(), items.get(position).getAuthor().getUsername(), items.get(position).getAuthor().getHeadSculpture());
                }
                if(isexist){
                    RYfriendManager.update(activity,items.get(position).getAuthor().getObjectId(), items.get(position).getAuthor().getUsername(), items.get(position).getAuthor().getHeadSculpture());
                }
                if (ismyself)
                    Toast.makeText(activity, "亲，这里不能跟自己聊天哦~", Toast.LENGTH_SHORT).show();
                else if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(activity, items.get(position).getAuthor().getObjectId(), items.get(position).getAuthor().getUsername());
                }
            }
        });

        return convertView;
    }
    private class HolderView{
        CircleImageView head;
        TextView name;
        TextView time;
        TextView lostname;
        TextView lostlocation;
        TextView losttime;
        TextView lostcontent;
        MyGirdView myGirdView;
        LinearLayout linearLayout;
    }

    /**
     *
     * @param items
     */
    public void additems(List<LostBack> items) {
        this.items.addAll(items);
        notifyDataSetChanged();

    }

    /**
     *
     * @param items
     */
    public void refreshitems(List<LostBack> items) {
        this.items.clear();
        this.items.addAll(items) ;
        notifyDataSetChanged();
    }

    public void clearitems(){
        this.items.clear();
        notifyDataSetChanged();
    }
}
