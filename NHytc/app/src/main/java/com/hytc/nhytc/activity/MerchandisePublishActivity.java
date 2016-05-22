package com.hytc.nhytc.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.PublishPhotoGridAdapter;
import com.hytc.nhytc.domain.Merchandise;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.view.MyGirdView;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Administrator on 2016/2/9.
 */
public class MerchandisePublishActivity extends Activity implements View.OnClickListener{
    /**
     * title
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;
    private RelativeLayout title_rl;

    private PublishPhotoGridAdapter mAdapter;
    private ArrayList<String> mPhotoList = null;
    private ArrayList<String> mSelectPath;
    private static final int REQUEST_IMAGE = 2;

    /**
     * 选择照片并显示的gridview
     */
    private MyGirdView glPics;
    /**
     * 用户上传的图片资源
     */
    private ArrayList<String> imgurls = new ArrayList<String>();


    /* 用来标识请求照相功能的activity */
    private static final int CAMERA_WITH_DATA = 30231;
    /* 用来标识请求gallery的activity */
    private static final int PHOTO_PICKED_WITH_DATA = 3021;
    /* 用来标识请求裁 剪图片后的activity */
    private static final int CAMERA_CROP_DATA = 3022;
    private File PHOTO_DIR = null;
    // 照相机拍照得到的图片
    private File mCurrentPhotoFile;
    private String mFileName;
    private int camIndex = 0;
    private View dialogTakePic;


    private RelativeLayout moretopic;
    private TextView tv_topic;
    private EditText mername;
    private EditText merprice;
    private EditText meroldprice;
    private EditText describe;
    private int topic = 10;

    private ProgressDialog progressDialog;



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_market_publish);
        initTitle();
        getimgs();
    }



    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);
        title_rl = (RelativeLayout) this.findViewById(R.id.rl_publish_title_bar);

        titlename.setText("商品发布");
        ivinfo.setVisibility(View.GONE);
        tvinfo.setVisibility(View.VISIBLE);
        ivmore.setVisibility(View.GONE);



        glPics = (MyGirdView) findViewById(R.id.gl_market_pic_publish);
        moretopic = (RelativeLayout) this.findViewById(R.id.rl_choose_topic_market);
        tv_topic = (TextView) this.findViewById(R.id.tv_lost_publish);
        mername = (EditText) this.findViewById(R.id.et_name_mer);
        merprice = (EditText) this.findViewById(R.id.et_price);
        meroldprice = (EditText) this.findViewById(R.id.et_old_price);
        describe = (EditText) this.findViewById(R.id.et_mer_describe);
        moretopic.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("商品发布中 0% ...");

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 在这里写发布二手商品
                 */
                if(isCanPublish()){
                    title_rl.setClickable(false);
                    progressDialog.show();
                    upLoadPicture();
                }
            }
        });
    }


    /**
     * 从相册或者照相机选择照片进行裁剪并显示在girdView之中
     */
    private void getimgs() {
        mPhotoList = new ArrayList<String>();
        mPhotoList.add(String.valueOf(R.mipmap.camer_n));
        mAdapter = new PublishPhotoGridAdapter(MerchandisePublishActivity.this, mPhotoList);
        glPics.setAdapter(mAdapter);
        //选择照片为主图
        glPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MerchandisePublishActivity.this, MultiImageSelectorActivity.class);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mAdapter.addItems(mAdapter.getCount() - 1,mSelectPath);
            }
        }
    }

    private void creatDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择失物类型");
        final String[] a = this.getResources().getStringArray(R.array.market_topic);
        builder.setItems(a, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /**
                 * 此处是为了应对bmob的bug，数字集中查询会出错
                 * 所以topic数字都是瞎写的
                 *
                 */
                switch (which){
                    case 0:
                        topic = 5;
                        break;
                    case 1:
                        topic = 14;
                        break;
                    case 2:
                        topic = 23;
                        break;
                    case 3:
                        topic = 32;
                        break;
                    case 4:
                        topic = 41;
                        break;
                    case 5:
                        topic = 56;
                        break;
                    case 6:
                        topic = 67;
                        break;
                }
                tv_topic.setText(a[which]);
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_choose_topic_market:
                creatDialog();
                break;
        }
    }

    public boolean isCanPublish(){
        boolean isok = false;
        if(topic == 10){
            Toast.makeText(this,"请选择商品类型！",Toast.LENGTH_SHORT).show();
        }else if(mername.getText().toString().trim().equals("")){
            Toast.makeText(this,"商品名称不能为空！",Toast.LENGTH_SHORT).show();
        }else if(merprice.getText().toString().trim().equals("")){
            Toast.makeText(this,"商品卖价不能为空！",Toast.LENGTH_SHORT).show();
        }else if(meroldprice.getText().toString().trim().equals("")){
            Toast.makeText(this,"商品原价不能为空！",Toast.LENGTH_SHORT).show();
        }else if(describe.getText().toString().trim().equals("")){
            Toast.makeText(this,"商品描述不能为空！",Toast.LENGTH_SHORT).show();
        }else if(mAdapter.getCount() == 1){
            Toast.makeText(this,"图片不能为空！",Toast.LENGTH_SHORT).show();
        } else {
            isok = true;
        }
        return isok;
    }

    private void upLoadPicture() {
        String[] pics = getStringpics();
        final int count = pics.length;
        final String[] pictures = new String[count];
        final String[] fileNames = new String[count];
        BmobFile.uploadBatch(this, pics, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                // fileNames：文件名数组
                // urls        : url：文件地址数组
                // files     : BmobFile文件数组，`V3.4.1版本`开始提供，用于兼容新旧文件服务。
                if (urls.size() == count ) {
                    for (int i = 0; i < count; i++) {
                        pictures[i] = urls.get(i);
                        fileNames[i] = files.get(i).getFilename();
                    }
                    parseData(pictures, fileNames);
                }
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
                progressDialog.setMessage("商品发布中 " + totalPercent + "% ...");
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                title_rl.setClickable(true);
                Toast.makeText(MerchandisePublishActivity.this, "发表失败！", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void parseData(String[] urls,String[] fileNames) {
        Merchandise merchandise = new Merchandise();
        merchandise.setAuthor(BmobUser.getCurrentUser(this,User.class));
        merchandise.setType(topic);
        merchandise.setMerchandiseName(mername.getText().toString().trim());
        merchandise.setPrice(merprice.getText().toString().trim());
        merchandise.setOldPrice(meroldprice.getText().toString().trim());
        merchandise.setContent(describe.getText().toString().trim());
        merchandise.setPictures(Arrays.asList(urls));
        merchandise.setPicturesNames(Arrays.asList(fileNames));
        merchandise.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Toast.makeText(MerchandisePublishActivity.this, "发布成功！", Toast.LENGTH_SHORT).show();
                title_rl.setClickable(true);
                MerchandisePublishActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                title_rl.setClickable(true);
                progressDialog.dismiss();
                Toast.makeText(MerchandisePublishActivity.this, "发布失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取本地图片url
     * @return
     */
    public String[] getStringpics(){
        ArrayList<String> imgss = mAdapter.getItems();
        Object[] pics = imgss.toArray();
        final int finallenth = pics.length - 1;
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
