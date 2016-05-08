package com.hytc.nhytc.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.DeleteFileListener;
import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.ImageViewActivity;
import com.hytc.nhytc.domain.UserPhotosData;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.view.activity_transition.ActivityTransitionLauncher;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;


/**
 */
public class PicsGirdAdapter extends BaseAdapter {

    private ArrayList<String> picurls = new ArrayList<>();
    private Context context = null;
    private LayoutInflater inflater;
    private BitmapUtils bitmapUtils;
    private int limitcount;
    private ProgressDialog progressDialog;
    /**
     * 这个参数是留着在我的相册管理中使用的
     *
     */
    private Boolean isok = false;
    private ArrayList<UserPhotosData> filenames = new ArrayList<>();

    /**
     *
     */
    public PicsGirdAdapter(Context context, ArrayList<String> imgurls, int limitcount){
        this.context = context;
        this.picurls.addAll(imgurls);
        this.inflater = LayoutInflater.from(context);
        this.bitmapUtils = BitmapHelper.getBitmapUtils(context.getApplicationContext());
        this.limitcount = limitcount;

        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setMessage("图片删除中...");
    }

    @Override
    public int getCount() {
        int size = picurls.size();
        if(size>limitcount){
            size = limitcount;
        }
        return size;
    }

    @Override
    public Object getItem(int position) {
        return picurls.get(position);
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
            convertView = inflater.inflate(R.layout.img_gird_item,null);
            viewHolder.imgPic = (ImageView) convertView.findViewById(R.id.imgPic);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        bitmapUtils.display(viewHolder.imgPic,picurls.get(position));
        viewHolder.imgPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("imgurls", picurls);
                intent.putExtra("pagenum", position + 1);
                intent.setClass(context, ImageViewActivity.class);
                //context.startActivity(intent);
                ActivityTransitionLauncher.with((Activity) context).from(v).launch(intent);
            }
        });

        viewHolder.imgPic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isok) {
                    showDeleteDialog(filenames.get(position).getObjectId(), filenames.get(position).getFilename(),position);
                }
                return false;
            }
        });

        return convertView;
    }
    private static class ViewHolder {
        public ImageView imgPic;
    }

    public void additem(String url){
        this.picurls.add(0,url);
        notifyDataSetChanged();
    }

    /**
     * 该方法只会在我的个人相册里删除图片时才会使用
     *
     * 其作用相当于开关
     */
    public void setisok(){
        this.isok = true;
    }

    /**
     * 该方法只会在我的个人相册里删除图片时才会使用
     * @param filenames
     */
    public void setFilenames(ArrayList<UserPhotosData> filenames){
        this.filenames = filenames;
    }


    /**
     * 该方法只会在我的个人相册里删除图片时才会使用
     * @param filename
     */
    public void showDeleteDialog(final String id,final String filename, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("文件删除");
        builder.setMessage("亲，您确定要删除该图片么？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePhoto(id, filename,position);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create();
        builder.show();
    }

    /**
     * 该方法只会在我的个人相册里删除图片时才会使用
     * @param url
     */
    public void deletepic(String url, final int position){

        BmobProFile.getInstance(context).deleteFile(url, new DeleteFileListener() {

            @Override
            public void onError(int errorcode, String errormsg) {
                progressDialog.dismiss();
                Log.e("PicsGirdAdapter", errorcode + "      " + errormsg);
                Toast.makeText(context, "图片删除失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                setData(position);
                Toast.makeText(context, "图片删除成功！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 该方法只会在我的个人相册里删除图片时才会使用
     *
     */
    public void deletePhoto(String id, final String filename, final int position){
        UserPhotosData userPhotosData = new UserPhotosData();
        userPhotosData.setObjectId(id);
        progressDialog.show();
        userPhotosData.delete(context, new DeleteListener() {
            @Override
            public void onSuccess() {
                deletepic(filename, position);
            }

            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(context, "图片删除失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 该方法只会在我的个人相册里删除图片时才会使用
     *
     */
    public void setData(int position){
        this.picurls.remove(position);
        this.filenames.remove(position);
        notifyDataSetChanged();
    }


}
