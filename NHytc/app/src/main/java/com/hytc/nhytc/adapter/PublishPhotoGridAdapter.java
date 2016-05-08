package com.hytc.nhytc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.hytc.nhytc.R;
import com.hytc.nhytc.tool.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/8/15.
 */
public class PublishPhotoGridAdapter extends BaseAdapter{

    private Context context;
    private BitmapUtils bitmapUtils;
    private ArrayList<String> imgurls;
    private LayoutInflater inflater;


    public PublishPhotoGridAdapter(Context context, ArrayList<String> imgurls) {
        this.context = context;
        this.bitmapUtils = BitmapHelper.getBitmapUtils(context.getApplicationContext());
        this.imgurls = imgurls;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imgurls.size();
    }

    @Override
    public Object getItem(int position) {
        return imgurls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.pickphoto_grid_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imgbtDelete = (ImageButton) convertView.findViewById(R.id.imgbtDelete);
            viewHolder.imgPic = (ImageView) convertView.findViewById(R.id.imgPic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == imgurls.size() - 1) {
            viewHolder.imgbtDelete.setVisibility(View.GONE);
            viewHolder.imgPic.setImageResource(R.mipmap.cam_photo);
        } else {
            viewHolder.imgbtDelete.setVisibility(View.VISIBLE);
            bitmapUtils.display(viewHolder.imgPic, imgurls.get(position));
        }
        final int a = position;
        viewHolder.imgbtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItems(a);
            }
        });
        return convertView;
    }

    /**
     *  @param position   the position
     * @param imagePaths the image paths
     */
    public void addItem(int position,String imagePaths) {
        imgurls.add(position, imagePaths);
        notifyDataSetChanged();
    }

    /**
     *
     * @param imagePaths the image paths
     */
    public void addItems(int position,ArrayList<String> imagePaths) {
        imgurls.addAll(position,imagePaths);
        notifyDataSetChanged();
    }

    /**
     *
     * @param position
     */
    public void removeItems(int position) {
        imgurls.remove(position);
        notifyDataSetChanged();
    }

    /**
     */
    public void clearItems() {
        imgurls.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        ImageView imgPic;
        ImageButton imgbtDelete;
    }

    public ArrayList<String> getItems() {
        return imgurls;
    }
}
