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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.DeleteFileListener;
import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.MerchandiseDetailActivity;
import com.hytc.nhytc.activity.PersonDetailDataActivity;
import com.hytc.nhytc.domain.Merchandise;
import com.hytc.nhytc.domain.ShuoShuo;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.tool.ShowTimeTools;
import com.hytc.nhytc.view.CircleImageView;
import com.hytc.nhytc.view.MyGirdView;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.DeleteListener;


/**
 * Created by Administrator on 2015/8/21.
 */
public class MyMerchandiseAcivityAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Merchandise> items;
    private BitmapUtils bitmapUtils;

    public MyMerchandiseAcivityAdapter(Activity activity, List<Merchandise> items) {
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
            convertView = inflater.inflate(R.layout.activity_my_merchandise_item,null);
            viewHolder.head = (CircleImageView) convertView.findViewById(R.id.iv_activity_market);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name_mer);
            viewHolder.faculty = (TextView) convertView.findViewById(R.id.tv_faculty_mer);
            viewHolder.time = (TextView) convertView.findViewById(R.id.tv_time_mer);
            viewHolder.money = (TextView) convertView.findViewById(R.id.tv_activity_market);
            viewHolder.describe = (TextView) convertView.findViewById(R.id.tv_mer_describe);
            viewHolder.myGirdView = (MyGirdView) convertView.findViewById(R.id.gl_activity_market_item);
            viewHolder.delete = (ImageView) convertView.findViewById(R.id.iv_delete_mer);

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
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog(items.get(position).getObjectId(), items.get(position).getPicturesNames(), position);
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
        ImageView delete;

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



    public void showdialog(final String ObJectid, final List<String> picnames,final int nowposition){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("商品移除");
        builder.setMessage("亲，您确定要移除这一商品么？");
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

    public void deleteitem(int dposition){
        this.items.remove(dposition);
        notifyDataSetChanged();
    }

    public void deleteData(final String ObJectid, final List<String> picnames,final int nowposition){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("商品移除中...");
        progressDialog.show();
        Merchandise merchandise = new Merchandise();
        merchandise.setObjectId(ObJectid);
        merchandise.delete(activity, new DeleteListener() {
            @Override
            public void onSuccess() {
                deleteitem(nowposition);
                progressDialog.dismiss();
                Toast.makeText(activity, "商品移除成功！", Toast.LENGTH_SHORT).show();
                deletepictures(picnames);
            }

            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(activity, "商品移除失败\n请检查网络连接", Toast.LENGTH_SHORT).show();
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
