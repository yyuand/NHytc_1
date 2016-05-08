package com.hytc.nhytc.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import com.hytc.nhytc.domain.ShuoShuo;
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
 * Created by Administrator on 2015/8/15.
 */
public class ShuoshuoPubishActivity extends Activity implements View.OnClickListener {
    /**
     * title
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;
    private RelativeLayout title_rl;

    /**
     *
     */

    private RelativeLayout relativeLayout;

    private TextView tvtopic;

    private EditText content;

    private int topic = 0;


    /**
     *
     */
    private MyGirdView glPics;
    /**
     *
     */
    private ProgressDialog progressDialog;


    private PublishPhotoGridAdapter mAdapter;
    private ArrayList<String> mPhotoList = null;
    private ArrayList<String> mSelectPath;
    private static final int REQUEST_IMAGE = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shuoshuo_publish);
        inititems();
        initTitle();
        getimgs();
        relativeLayout.setOnClickListener(this);

    }


    /**
     */
    private void inititems() {
        glPics = (MyGirdView) findViewById(R.id.gl_shuoshuo_publish);
        /**
         */
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);
        title_rl = (RelativeLayout) this.findViewById(R.id.rl_publish_title_bar);

        relativeLayout = (RelativeLayout) this.findViewById(R.id.rl_choose_topic);
        tvtopic = (TextView) this.findViewById(R.id.tv_shuo_topic_publish);
        content = (EditText) this.findViewById(R.id.et_shuocontent);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("淮说发表中 0% ...");

    }

    public void initTitle() {

        titlename.setText("发表淮说");
        ivinfo.setVisibility(View.GONE);
        tvinfo.setVisibility(View.VISIBLE);
        ivmore.setVisibility(View.GONE);

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isok()) {
                    progressDialog.show();
                    title_rl.setClickable(false);
                    if (mAdapter.getItems().size() != 1) {
                        upLoadPicture();
                    } else {
                        parseData(null, null);
                    }
                } else {
                    Toast.makeText(ShuoshuoPubishActivity.this, "淮说内容不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    /**
     *
     */
    private void getimgs() {
        mPhotoList = new ArrayList<String>();
        mPhotoList.add(String.valueOf(R.mipmap.camer_n));
        mAdapter = new PublishPhotoGridAdapter(ShuoshuoPubishActivity.this, mPhotoList);
        glPics.setAdapter(mAdapter);
        glPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShuoshuoPubishActivity.this, MultiImageSelectorActivity.class);
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
            case R.id.rl_choose_topic:
                creatDialog();
            break;
        }
    }



    /**
     *
     * @return
     */
    public boolean isok(){
        return !"".equals(content.getText().toString().trim());
    }


    private void creatDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        final String[] a = this.getResources().getStringArray(R.array.say_topic);
        builder.setItems(a, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    tvtopic.setText("#");
                    topic = 0;
                }else {
                    tvtopic.setText("#" + a[which]);
                    topic = which;
                }
            }
        });
        builder.show();
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
                        urls[i] = files[i].getFileUrl(ShuoshuoPubishActivity.this);
                    }
                    parseData(urls,fileNames);
                }

            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                progressDialog.setMessage("淮说发表中 " + totalPercent + "% ...");
                // curIndex    :表示当前第几个文件正在上传
                // curPercent  :表示当前上传文件的进度值（百分比）
                // total       :表示总的上传文件数
                // totalPercent:表示总的上传进度（百分比）
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                title_rl.setClickable(true);
                Toast.makeText(ShuoshuoPubishActivity.this, "发表失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void parseData(String[] urls, String[] fileNames){

        ShuoShuo shuoShuo = new ShuoShuo();
        shuoShuo.setTopic(topic);
        if(urls != null) {
            shuoShuo.setPictures(Arrays.asList(urls));
            shuoShuo.setPicturesNames(Arrays.asList(fileNames));
        }else {
            shuoShuo.setPictures(null);
        }
        shuoShuo.setContent(content.getText().toString().trim());
        User user = BmobUser.getCurrentUser(this, User.class);
        shuoShuo.setAuthor(user);
        shuoShuo.setApproveCount(0);
        shuoShuo.setCommentCount(0);
        shuoShuo.setIsApprove(false);
        shuoShuo.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Toast.makeText(ShuoshuoPubishActivity.this, "发表成功！", Toast.LENGTH_SHORT).show();
                title_rl.setClickable(true);
                ShuoshuoPubishActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                title_rl.setClickable(true);
                Toast.makeText(ShuoshuoPubishActivity.this, "发表失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }



    /**
     * 获取本地图片url
     * @return
     */
    public String[] getStringpics(){
        ContentResolver cr = getContentResolver();
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
