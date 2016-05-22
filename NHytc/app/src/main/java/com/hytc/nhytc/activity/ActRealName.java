package com.hytc.nhytc.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hytc.nhytc.R;
import com.hytc.nhytc.domain.RealNameUser;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.tool.BitmapHelper;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by DYY on 2016/5/14.
 */
public class ActRealName extends Activity {
    @Bind(R.id.et_partjob_name)
    EditText etPartjobName;
    @Bind(R.id.first_upload)
    RelativeLayout firstUpload;
    @Bind(R.id.sec_upload)
    RelativeLayout secUpload;
    @Bind(R.id.hand_upload)
    RelativeLayout handUpload;
    @Bind(R.id.iden_num)
    EditText idenNum;
    @Bind(R.id.first_modify)
    TextView firstModify;
    @Bind(R.id.sec_modify)
    TextView secModify;
    @Bind(R.id.hand_modify)
    TextView handModify;

    /**
     * title
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;
    //三张照片的路径
    private String pic1_url = "";
    private String pic2_url = "";
    private String pic3_url = "";


    //3张照片的请求码
    private static final int REQUEST_IMAGE1 = 1;
    private static final int REQUEST_IMAGE2 = 2;
    private static final int REQUEST_IMAGE3 = 3;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局文件
        setContentView(R.layout.act_realname);
        //绑定字段
        ButterKnife.bind(this);
        //初始化界面
        inittitle();
        //得到用户提交的信息
        getSaveData(savedInstanceState);
    }

    /**
     * 获取照片
     *
     * @param view
     */
    @OnClick({R.id.first_upload, R.id.sec_upload, R.id.hand_upload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.first_upload:
                getPic(REQUEST_IMAGE1);
                break;
            case R.id.sec_upload:
                getPic(REQUEST_IMAGE2);
                break;
            case R.id.hand_upload:
                getPic(REQUEST_IMAGE3);
                break;
        }
    }

    /**
     * 获取本地照片
     */
    private void getPic(int code) {
        Intent intent = new Intent(ActRealName.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        startActivityForResult(intent, code);
    }

    /**
     * 回调函数 ，判断请求码获取一张图片的路径，并给id设置属性
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                pic1_url = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0);
                firstModify.setText("上传成功！");
                firstModify.setTextColor(getResources().getColor(R.color.red));
            } else if (requestCode == 2) {
                pic2_url = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0);
                secModify.setText("上传成功！");
                secModify.setTextColor(getResources().getColor(R.color.red));
            } else if (requestCode == 3) {
                pic3_url = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0);
                handModify.setText("上传成功！");
                handModify.setTextColor(getResources().getColor(R.color.red));
            }
        }
    }

    /**
     * 保存当前界面的状态与数据（类似操作系统中的保护现场）
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("url1", pic1_url);//键值对，将pic1_url的值存入url1
        outState.putString("url2", pic2_url);
        outState.putString("url3", pic3_url);
    }

    public void parseData(String[] urls, String[] fileNames){//s为上传服务器端的位置
        RealNameUser user = new RealNameUser();
        user.setApplicant(BmobUser.getCurrentUser(this,User.class));
        user.setApply_Name(etPartjobName.getText().toString());
        user.setIden_Num(idenNum.getText().toString().trim());
        user.setFirst_Pic(urls[0]);
        user.setSec_Pic(urls[1]);
        user.setHand_Pic(urls[2]);
        user.setPicturesNames(Arrays.asList(fileNames));
        user.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                tvinfo.setClickable(true);
                progressDialog.dismiss();
                Toast.makeText(ActRealName.this, "资料上传成功\n 请等待审核", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                tvinfo.setClickable(true);
                progressDialog.dismiss();
                Toast.makeText(ActRealName.this, "资料上传失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void inittitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("资料上传中 0% ...");

        titlename.setText("实名认证");
        ivinfo.setVisibility(View.GONE);
        ivmore.setVisibility(View.GONE);
        tvinfo.setText("确认");
        tvinfo.setVisibility(View.VISIBLE);
        tvinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isContent_null()){
                    tvinfo.setClickable(false);
                    progressDialog.show();
                    upLoadPic();
                }
            }
        });

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Boolean isContent_null() {
        Boolean result = false;
        if ("".equals(etPartjobName.getText().toString())) {
            Toast.makeText(this, "姓名不能为空哟~", Toast.LENGTH_SHORT).show();
        } else if ("".equals(idenNum.getText().toString())) {
            Toast.makeText(this, "身份证号码不能为空哟~", Toast.LENGTH_SHORT).show();
        } else if ("".equals(pic1_url)) {
            Toast.makeText(this, "需要上传身份证正面照哟~", Toast.LENGTH_SHORT).show();
        } else if ("".equals(pic2_url)) {
            Toast.makeText(this, "需要上传身份证反面照哟~", Toast.LENGTH_SHORT).show();
        } else if ("".equals(pic3_url)) {
            Toast.makeText(this, "需要上传手持身份证照哟~", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
    }

    /**
     * 得到保存的数据
     *
     * @param savedInstanceState
     */
    public void getSaveData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            pic1_url = savedInstanceState.getString("url1");
            pic2_url = savedInstanceState.getString("url2");
            pic3_url = savedInstanceState.getString("url3");
        }
        if (!"".equals(pic1_url)) {
            firstModify.setText("上传成功！");
            firstModify.setTextColor(getResources().getColor(R.color.red));
        }
        if (!"".equals(pic2_url)) {
            secModify.setText("上传成功！");
            secModify.setTextColor(getResources().getColor(R.color.red));
        }
        if (!"".equals(pic3_url)) {
            handModify.setText("上传成功！");
            handModify.setTextColor(getResources().getColor(R.color.red));
        }
    }


    /**
     * 获取本地图片url
     * @return
     */
    public String[] getStringpics(){
        /*ArrayList<String> imgss = mAdapter.getItems();
        Object[] pics = imgss.toArray();
        final int finallenth = pics.length - 1;*/

        String[] pics = new String[]{pic1_url,pic2_url,pic3_url};

        String[] bombpics = new String[3];
        for(int i=0; i<3;i++){
            //如果我要上传的图片是大于200kb
            if(Double.valueOf(UriToSize(pics[i])) > Double.valueOf("200")) {
                bombpics[i] = BitmapHelper.getImageThumbnail(pics[i]);
            }else {
                bombpics[i] = pics[i];
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

    public void upLoadPic(){
        final String[] pictures = new String[3];
        final String[] fileNames = new String[3];
        BmobFile.uploadBatch(this, getStringpics(), new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files,List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if(urls.size()==3){//如果数量相等，则代表文件全部上传完成
                    for (int i = 0; i < 3; i++) {
                        pictures[i] = urls.get(i);
                        fileNames[i] = files.get(i).getUrl();
                    }
                    parseData(pictures, fileNames);
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                tvinfo.setClickable(true);
                progressDialog.dismiss();
                Toast.makeText(ActRealName.this, "发表失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                progressDialog.setMessage("资料上传中 " + totalPercent + "% ...");
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });
    }

}
