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
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;
import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.PublishPhotoGridAdapter;
import com.hytc.nhytc.domain.LostBack;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.view.MyGirdView;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;


/**
 * Created by Administrator on 2015/8/18.
 */
public class LostPublishActivity extends Activity implements View.OnClickListener {

    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;
    private RelativeLayout title_rl;


    private RelativeLayout function;
    private RelativeLayout relativeLayout;

    private TextView tvtopic;
    private TextView tvfunction;

    private EditText lost_name;
    private EditText lost_location;
    private EditText lost_time;
    private EditText content;

    private ProgressDialog progressDialog;

    private PublishPhotoGridAdapter mAdapter;
    private ArrayList<String> mPhotoList = null;
    private ArrayList<String> mSelectPath;
    private static final int REQUEST_IMAGE = 2;

    /**
     * 选择照片并显示的gridview
     */
    private MyGirdView glPics;

    private Integer topic = 10;
    private Integer choose_function = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_publish);
        initTitle();
        getimgs();
        relativeLayout.setOnClickListener(this);
        function.setOnClickListener(this);
    }



    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);
        title_rl = (RelativeLayout) this.findViewById(R.id.rl_publish_title_bar);


        titlename.setText("失物寻物");
        ivinfo.setVisibility(View.GONE);
        tvinfo.setVisibility(View.VISIBLE);
        ivmore.setVisibility(View.GONE);



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("发布中 0% ...");

        glPics = (MyGirdView) findViewById(R.id.gl_lost_pic_publish);

        relativeLayout = (RelativeLayout) this.findViewById(R.id.rl_choose_topic_lost);
        function = (RelativeLayout) this.findViewById(R.id.rl_choose_function_lost);
        tvtopic = (TextView) this.findViewById(R.id.tv_lost_publish);
        tvfunction = (TextView) this.findViewById(R.id.tv_lost_publish_function);
        lost_name = (EditText) this.findViewById(R.id.et_lost_name_lost_publish);
        lost_location = (EditText) this.findViewById(R.id.et_lost_location_lost_publish);
        lost_time = (EditText) this.findViewById(R.id.et_lost_time_lost_publish);
        content = (EditText) this.findViewById(R.id.et_content_lost_publish);


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
                 * 这里写发布失物招领的逻辑
                 */
                if(isOk()){
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
        mAdapter = new PublishPhotoGridAdapter(LostPublishActivity.this, mPhotoList);
        glPics.setAdapter(mAdapter);
        //选择照片为主图
        glPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LostPublishActivity.this, MultiImageSelectorActivity.class);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_choose_topic_lost:
                creatDialog();
                break;
            case R.id.rl_choose_function_lost:
                creatfunctionDialog();
                break;
        }
    }


    public void upLoadPicture(){
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
                        urls[i] = files[i].getFileUrl(LostPublishActivity.this);
                    }
                    parseData(urls, fileNames);
                }

            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                // curIndex    :表示当前第几个文件正在上传
                // curPercent  :表示当前上传文件的进度值（百分比）
                // total       :表示总的上传文件数
                // totalPercent:表示总的上传进度（百分比）
                progressDialog.setMessage("发布中 " + totalPercent + "% ...");
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                title_rl.setClickable(true);
                Toast.makeText(LostPublishActivity.this, "发表失败！", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void parseData(String[] urls,String[] fileNames) {
        LostBack lostBack = new LostBack();
        lostBack.setLostType(topic);
        lostBack.setLostfunction(choose_function);
        lostBack.setAuthor(BmobUser.getCurrentUser(this, User.class));
        lostBack.setContent(content.getText().toString().trim());
        lostBack.setLostName(lost_name.getText().toString().trim());
        lostBack.setLostLocation(lost_location.getText().toString().trim());
        lostBack.setLostTime(lost_time.getText().toString().trim());
        lostBack.setPictures(Arrays.asList(urls));
        lostBack.setPicturesNames(Arrays.asList(fileNames));
        lostBack.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Toast.makeText(LostPublishActivity.this, "发布成功！", Toast.LENGTH_SHORT).show();
                title_rl.setClickable(true);
                LostPublishActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                title_rl.setClickable(true);
                progressDialog.dismiss();
                Toast.makeText(LostPublishActivity.this, "发布失败！", Toast.LENGTH_SHORT).show();
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


    public Boolean isOk(){
        Boolean isok = false;
        if(topic == 10){
            Toast.makeText(this, "请选择失物类型！", Toast.LENGTH_SHORT).show();
        }else if(choose_function == 10){
            Toast.makeText(this, "请选择功能！", Toast.LENGTH_SHORT).show();
        }else if(lost_name.getText().toString().trim().equals("")){
            Toast.makeText(this,"失物名称不能为空！", Toast.LENGTH_SHORT).show();
        }else if(lost_location.getText().toString().trim().equals("")){
            Toast.makeText(this,"捡到失物地点不能为空！", Toast.LENGTH_SHORT).show();
        }else if(lost_time.getText().toString().trim().equals("")){
            Toast.makeText(this,"捡到失物时间不能为空！", Toast.LENGTH_SHORT).show();
        }else if(content.getText().toString().trim().equals("")){
            Toast.makeText(this,"失物描述不能为空！", Toast.LENGTH_SHORT).show();
        }else if(mAdapter.getCount() == 1){
            Toast.makeText(this, "发表失物寻物的图片不能为空！", Toast.LENGTH_SHORT).show();
        }else {
            isok = true;
        }
        return isok;
    }


    private void creatDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("类型选择");
        final String[] a = this.getResources().getStringArray(R.array.lost_topic);
        builder.setItems(a, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /**
                 * 此处不知道是不是bmob有bug，因为我查询topic为1的失物招领，但是它却当作0来查，没办法就用
                 * 6来代替0,45来代替1,12来代替2,23来代替3,34来代替4,
                 */
                switch (which){
                    case 0:
                        topic = 6;
                        break;
                    case 1:
                        topic = 45;
                        break;
                    case 2:
                        topic = 12;
                        break;
                    case 3:
                        topic = 23;
                        break;
                    case 4:
                        topic = 34;
                        break;
                }
                tvtopic.setText("#" + a[which]);
            }
        });
        builder.show();
    }



    private void creatfunctionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("功能选择");
        final String[] a = this.getResources().getStringArray(R.array.lost_function);
        builder.setItems(a, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        choose_function = 11;
                        break;
                    case 1:
                        choose_function = 22;
                        break;
                }
                tvfunction.setText("#" + a[which]);
            }
        });
        builder.show();
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

