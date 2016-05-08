package com.hytc.nhytc.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbLogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.bmob.btp.callback.UploadListener;
import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.PicsGirdAdapter;
import com.hytc.nhytc.adapter.PublishPhotoGridAdapter;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.domain.UserPhotosData;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.view.MyGirdView;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Administrator on 2016/2/28.
 */
public class UpLoadMyPhotos extends AbActivity {
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;


    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog2;
    private ProgressDialog progressDialog3;
    private RelativeLayout setphotos;
    private ImageView uploadpic;
    private MyGirdView myGirdView;
    private PicsGirdAdapter adapter;
    private User user;
    private Intent intent;

    private ArrayList<String> mSelectPath;
    private static final int REQUEST_IMAGE = 2;

    private LayoutInflater inflater;

    private ArrayList<String> pics;

    private Boolean isFormWhere;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_my_photos);
        initTitle();
        initwidget();
        loadMyPhotos();
    }

    private void loadMyPhotos() {
        progressDialog2.show();
        BmobQuery<UserPhotosData> query = new BmobQuery<>();

        /**
         * 如果isFormWhere为true的话，那么该界面就是由个人详情跳转过来的，所以这里的
         * userid直接就从本地缓存user中获取
         *
         * 如果为false，那么就是陌生人查看某人详情时顺便查看相册的了
         * 这时会传过来一个userid，当然也有可能在查看某人详情是获取信息失败，
         * 那么如果再跳转到这个界面，此时传过来的userid就是null了，此处应该注意一下
         */
        if(isFormWhere) {
            query.addWhereEqualTo("userid", user.getObjectId());
        }else {
            String userid = intent.getStringExtra("userid");
            if("null".equals(userid)){
                Toast.makeText(this,"信息获取失败\n请检查网络连接！",Toast.LENGTH_SHORT).show();
                return;
            }else {
                query.addWhereEqualTo("userid", userid);
            }
        }
        query.order("-createdAt");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(3));//此表示缓存3天
        query.findObjects(this, new FindListener<UserPhotosData>() {
            @Override
            public void onSuccess(List<UserPhotosData> list) {
                progressDialog2.dismiss();
                for (UserPhotosData data : list) {
                    pics.add(data.getPhotourl());
                }

                if (pics.size() == 0) {
                    UpLoadMyPhotos.this.findViewById(R.id.tv_my_photos).setVisibility(View.VISIBLE);
                }

                adapter = new PicsGirdAdapter(UpLoadMyPhotos.this, pics, 90);
                if (isFormWhere) {
                    adapter.setisok();
                    ArrayList<UserPhotosData> photosDatas = new ArrayList<>();
                    for (UserPhotosData photosData : list) {
                        photosDatas.add(photosData);
                    }
                    adapter.setFilenames(photosDatas);
                }
                myGirdView.setAdapter(adapter);
            }

            @Override
            public void onError(int i, String s) {
                progressDialog2.dismiss();
                Toast.makeText(UpLoadMyPhotos.this, "图片地址加载失败\n请检查网络连接！", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void initwidget() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("图片上传中 0% ...");
        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setMessage("图片地址加载中...");
        progressDialog3 = new ProgressDialog(this);
        progressDialog3.setMessage("图片删除中...");
        pics = new ArrayList<>();
        inflater = LayoutInflater.from(this);
        uploadpic = (ImageView) this.findViewById(R.id.imgPic_set_photos);
        myGirdView = (MyGirdView) this.findViewById(R.id.gl_activity_set_photos);
        user = BmobUser.getCurrentUser(this, User.class);

        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpLoadMyPhotos.this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                // 选择模式
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);

                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
    }

    private void initTitle() {
        intent = getIntent();
        setphotos = (RelativeLayout) this.findViewById(R.id.rl_set_photos);
        /**
         * 如果是true，那么该页面就是由我的信息页面跳转过来的
         * 如果为false，那么就是查看个人详情进入该界面的
         */
        isFormWhere = intent.getBooleanExtra("photos",false);
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);



        if(isFormWhere) {

            titlename.setText("我的相册");
        }else {
            titlename.setText("相册");
            setphotos.setVisibility(View.GONE);
        }
        ivinfo.setVisibility(View.GONE);
        tvinfo.setVisibility(View.GONE);
        ivmore.setVisibility(View.GONE);

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if(mSelectPath.size() != 0){
                    upLoadPicture();
                }
            }
        }
    }


    public void upDataToBmob(String filename,final String url){
        UserPhotosData photosData = new UserPhotosData();
        photosData.setUserid(user.getObjectId());
        photosData.setPhotourl(url);
        photosData.setFilename(filename);
        photosData.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();

                adapter.additem(url);
                UpLoadMyPhotos.this.findViewById(R.id.tv_my_photos).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }


    public void upLoadPicture(){
        progressDialog.show();
        String[] pics = getStringpics();
        final int count = pics.length;

        BmobProFile.getInstance(this).uploadBatch(pics, new UploadBatchListener() {

            @Override
            public void onSuccess(boolean isFinish, String[] fileNames, String[] urls, BmobFile[] files) {
                // isFinish ：批量上传是否完成
                // fileNames：文件名数组
                // urls        : url：文件地址数组
                // files     : BmobFile文件数组，`V3.4.1版本`开始提供，用于兼容新旧文件服务。
                if (urls.length == count && urls[urls.length - 1] != null) {
                    for (int i = 0; i < urls.length; i++) {
                        urls[i] = files[i].getFileUrl(UpLoadMyPhotos.this);
                    }
                    parseData(urls, fileNames);
                    Toast.makeText(UpLoadMyPhotos.this, "图片上传成功！", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                progressDialog.setMessage("图片上传中 " + totalPercent + "% ...");
                // curIndex    :表示当前第几个文件正在上传
                // curPercent  :表示当前上传文件的进度值（百分比）
                // total       :表示总的上传文件数
                // totalPercent:表示总的上传进度（百分比）
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                Toast.makeText(UpLoadMyPhotos.this, "上传失败！", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void parseData(String[] urls, String[] fileNames) {
        for(int j=0;j<urls.length;j++){
            upDataToBmob(fileNames[j],urls[j]);
        }
    }

    /**
     * 获取本地图片url
     * @return
     */
    public String[] getStringpics(){
        Object[] pics = mSelectPath.toArray();
        final int finallenth = pics.length;
        String[] bombpics = new String[finallenth];
        for(int i=0; i<finallenth;i++){
            //如果我要上传的图片是大于200kb
            if(Double.valueOf(UriToSize((String) pics[i])) > Double.valueOf("200")) {
                bombpics[i] = BitmapHelper.getImageThumbnail((String) pics[i]);
            }else {
                bombpics[i] = (String) pics[i];
            }
        }
        return bombpics;
    }


    /**
     * 通过传入的 本地路径 获取 图片大小
     * @param s 图片的存储路径
     * @return KB MB
     */
    public String UriToSize(String s){
        File file = new File(s);
        long blockSize = 0;
        try {
            blockSize = getFileSize(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     * @param
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception
    {
        long size = 0;
        if (file.exists()){
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        }
        else{
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 转换文件大小
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize="0";
        if(fileS==0){
            return wrongSize;
        }

        fileSizeString = String.valueOf((double) fileS / 1024);

        /*if (fileS < 1024){
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576){
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        }
        else if (fileS < 1073741824){
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }
        else{
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }*/

        return fileSizeString;
    }



}
