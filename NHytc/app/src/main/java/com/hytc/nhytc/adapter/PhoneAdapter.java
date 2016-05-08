package com.hytc.nhytc.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hytc.nhytc.R;


/**
 * Created by Administrator on 2015/8/17.
 */
public class PhoneAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private LayoutInflater inflater;

    private String[] armtype;

    private String[][] departments;
    private String[][] phones;

    public PhoneAdapter(Activity activity) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        phones = new String[][]{
                activity.getResources().getStringArray(R.array.phone1),
                activity.getResources().getStringArray(R.array.phone2),
                activity.getResources().getStringArray(R.array.phone3),
                activity.getResources().getStringArray(R.array.phone4),
                activity.getResources().getStringArray(R.array.phone5),
                activity.getResources().getStringArray(R.array.phone6),
                activity.getResources().getStringArray(R.array.phone7),
                activity.getResources().getStringArray(R.array.phone8),
                activity.getResources().getStringArray(R.array.phone9),
        };
        departments = new String[][]{
                activity.getResources().getStringArray(R.array.department1),
                activity.getResources().getStringArray(R.array.department2),
                activity.getResources().getStringArray(R.array.department3),
                activity.getResources().getStringArray(R.array.department4),
                activity.getResources().getStringArray(R.array.department5),
                activity.getResources().getStringArray(R.array.department6),
                activity.getResources().getStringArray(R.array.department7),
                activity.getResources().getStringArray(R.array.department8),
                activity.getResources().getStringArray(R.array.department9),
        };
        armtype = activity.getResources().getStringArray(R.array.departments);
    }


    @Override
    public int getGroupCount() {
        return armtype.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return phones[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return phones[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.phone_group_item,null);
        TextView textView = (TextView) view.findViewById(R.id.tv_group);
        ImageView imageView = (ImageView) view.findViewById(R.id.upOrDown);
        if(isExpanded){
            imageView.setImageResource(R.drawable.iconshang);
        }else {
            imageView.setImageResource(R.drawable.iconxia);
        }

        textView.setText(armtype[groupPosition]);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.phone_child_item,null);
        TextView textView = (TextView) view.findViewById(R.id.tv_name_phone);
        TextView textView1 = (TextView) view.findViewById(R.id.tv_phone_numbere);
        textView.setText(departments[groupPosition][childPosition]);
        final String phone = "0517" + phones[groupPosition][childPosition];
        textView1.setText(phone);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(phone);
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public void showDialog(final String phone){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(R.drawable.tophone);
        builder.setTitle("电话");
        builder.setMessage("是否拨打该电话？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                activity.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create();
        builder.show();
    }
}
