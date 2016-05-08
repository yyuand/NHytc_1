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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.DeleteFileListener;
import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.PersonDetailDataActivity;
import com.hytc.nhytc.domain.LostBack;
import com.hytc.nhytc.domain.Merchandise;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.tool.ShowTimeTools;
import com.hytc.nhytc.view.CircleImageView;
import com.hytc.nhytc.view.MyGirdView;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.DeleteListener;

/**
 * Created by Administrator on 2016/2/8.
 */
public class LostMyActivityAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<LostBack> items;
    private BitmapUtils bitmapUtils;

    public LostMyActivityAdapter(Activity activity, List<LostBack> items) {
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
        HolderView holderView = null;
        if(convertView == null){
            holderView = new HolderView();
            convertView = inflater.inflate(R.layout.activity_lost_list_my_item,null);
            holderView.head = (CircleImageView) convertView.findViewById(R.id.iv_my_lost_head);
            holderView.name = (TextView) convertView.findViewById(R.id.tv_my_username_lost);
            holderView.time = (TextView) convertView.findViewById(R.id.tv_my_time_lost);
            holderView.lostname = (TextView) convertView.findViewById(R.id.tv_my_lost_name);
            holderView.lostlocation = (TextView) convertView.findViewById(R.id.tv_my_lost_location);
            holderView.losttime = (TextView) convertView.findViewById(R.id.tv_my_lost_time);
            holderView.lostcontent = (TextView) convertView.findViewById(R.id.tv_my_content_lost_list_item);
            holderView.myGirdView = (MyGirdView) convertView.findViewById(R.id.gl_my_picture_lost_list_item);
            holderView.linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_my_context_lost_item);
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
                /**
                 * 此处写删除代码
                 */
                showdialog(items.get(position).getObjectId(), items.get(position).getPicturesNames(), position);
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

    public void deleteitem(int dposition){
        this.items.remove(dposition);
        notifyDataSetChanged();
    }

    public void showdialog(final String ObJectid, final List<String> picnames,final int nowposition){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("失物招领移除");
        builder.setMessage("亲，您确定要移除这一失物招领么？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteData(ObJectid,picnames,nowposition);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create();
        builder.show();
    }


    public void deleteData(final String ObJectid, final List<String> picnames,final int nowposition){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("失物招领移除中...");
        progressDialog.show();
        LostBack lostBack = new LostBack();
        lostBack.setObjectId(ObJectid);
        lostBack.delete(activity, new DeleteListener() {
            @Override
            public void onSuccess() {
                deleteitem(nowposition);
                progressDialog.dismiss();
                Toast.makeText(activity, "失物招领移除成功！", Toast.LENGTH_SHORT).show();
                deletepictures(picnames);
            }

            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(activity, "失物招领移除失败\n请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deletepictures(List<String> picnames){
        if(picnames != null) {
            for (String picname : picnames) {
                BmobProFile.getInstance(activity).deleteFile(picname, new DeleteFileListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }
        }
    }
}
