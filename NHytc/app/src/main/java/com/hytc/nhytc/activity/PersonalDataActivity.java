package com.hytc.nhytc.activity;

import android.app.Activity;
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
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.bmob.btp.callback.DeleteFileListener;
import com.bmob.btp.callback.UploadListener;
import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.PublishPhotoGridAdapter;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.view.CircleImageView;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/2/27.
 */
public class PersonalDataActivity extends AbActivity implements View.OnClickListener {
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;

    private User user;

    private CircleImageView head;
    private TextView name;
    private TextView sex;
    private TextView autograhp;
    private TextView faculty;
    private TextView stunumber;

    private RelativeLayout relativeLayout1;
    private RelativeLayout relativeLayout2;
    private RelativeLayout relativeLayout3;
    private RelativeLayout relativeLayout4;
    private RelativeLayout relativeLayout5;
    private RelativeLayout relativeLayout6;
    private RelativeLayout relativeLayout7;

    private BitmapUtils bitmapUtils;
    private LayoutInflater inflater;

    private ProgressDialog progressDialog;



    /* */
    private static final int CAMERA_WITH_DATA = 30231;
    /*  */
    private static final int PHOTO_PICKED_WITH_DATA = 3033;
    /*  */
    private static final int CAMERA_CROP_DATA = 3022;
    private File PHOTO_DIR = null;
    //
    private File mCurrentPhotoFile;
    private String mFileName;
    private View dialogTakePic;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persondata);
        initTitle();
        initwidget();
        datatowidget();
    }

    private void datatowidget() {
        bitmapUtils.display(head, user.getHeadSculpture());
        name.setText(user.getUsername());

        try {
            if(user.getGender()){
                sex.setText("帅哥");
            }else {
                sex.setText("美女");
            }
        }catch (Exception e){
            getuser(user.getObjectId());
        }

        /*if(user.getGender()){
            sex.setText("帅哥");
        }else {
            sex.setText("美女");
        }*/
        autograhp.setText(user.getAutograhp());
        faculty.setText(user.getFaculty());
        stunumber.setText(user.getStu_number());

    }

    public void getuser(String userid){
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId",userid);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(3));//此表示缓存3天
        progressDialog.show();
        query.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                progressDialog.dismiss();
                if(list.size() != 0){
                    user = list.get(0);
                    datatowidget();
                }
            }

            @Override
            public void onError(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(PersonalDataActivity.this,"信息获取失败\n请检查网络连接",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initwidget() {
        head = (CircleImageView) this.findViewById(R.id.iv_my_head);
        name = (TextView) this.findViewById(R.id.tv_name_personal);
        sex = (TextView) this.findViewById(R.id.tv_sex_personal);
        autograhp = (TextView) this.findViewById(R.id.tv_autograhp_personal);
        faculty = (TextView) this.findViewById(R.id.tv_faculty_personal);
        stunumber = (TextView) this.findViewById(R.id.tv_stunumber);
        bitmapUtils = BitmapHelper.getBitmapUtils(this);
        inflater = LayoutInflater.from(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("信息修改中...");
        tackphoto();



        relativeLayout1 = (RelativeLayout) this.findViewById(R.id.rl_activity_personal1);
        relativeLayout2 = (RelativeLayout) this.findViewById(R.id.rl_activity_personal2);
        relativeLayout3 = (RelativeLayout) this.findViewById(R.id.rl_activity_personal3);
        relativeLayout4 = (RelativeLayout) this.findViewById(R.id.rl_activity_personal4);
        relativeLayout5 = (RelativeLayout) this.findViewById(R.id.rl_activity_personal5);
        relativeLayout6 = (RelativeLayout) this.findViewById(R.id.rl_activity_personal6);
        relativeLayout7 = (RelativeLayout) this.findViewById(R.id.rl_activity_personal7);

        relativeLayout1.setOnClickListener(this);
        relativeLayout2.setOnClickListener(this);
        relativeLayout3.setOnClickListener(this);
        relativeLayout4.setOnClickListener(this);
        relativeLayout5.setOnClickListener(this);
        relativeLayout6.setOnClickListener(this);
        relativeLayout7.setOnClickListener(this);
    }

    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        user = BmobUser.getCurrentUser(this,User.class);

        titlename.setText("我的信息");
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

    /**
     * 请求修改的点击事件
     * 其中发送的请求类型
     * 1：修改头像
     * 2：修改昵称
     * 3：修改性别
     * 4：修改个性签名
     * 5：修改学院
     * 6：修改学号
     * 7：相册管理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_activity_personal1:
                AbDialogUtil.removeDialog(dialogTakePic);
                AbDialogUtil.showAlertDialog(dialogTakePic);
                break;
            case R.id.rl_activity_personal2:
                showsetDialog(2);
                break;
            case R.id.rl_activity_personal3:
                setSpinerdialog(3);
                break;
            case R.id.rl_activity_personal4:
                showsetDialog(4);
                break;
            case R.id.rl_activity_personal5:
                setSpinerdialog(5);
                break;
            case R.id.rl_activity_personal6:
                showsetDialog(6);
                break;
            case R.id.rl_activity_personal7:
                Intent intent = new Intent();
                intent.setClass(this,UpLoadMyPhotos.class);
                intent.putExtra("photos",true);
                startActivity(intent);
                break;
        }
    }

    /**
     * 显示text的dialog
     * @param type 请求的类型
     */
    public void showsetDialog(final Integer type){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = inflater.inflate(R.layout.activity_set_text_dialog,null);
        final EditText editText = (EditText) view.findViewById(R.id.et_set_text);
        switch (type){
            case 2:
                editText.setText(user.getUsername());
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                editText.setMaxLines(1);
                builder.setTitle("昵称修改");
                builder.setView(view);
                break;
            case 4:
                editText.setText(user.getAutograhp());
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
                builder.setTitle("个性签名修改");
                builder.setView(view);
                break;
            case 6:
                editText.setText(user.getStu_number());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setTitle("学号修改");
                builder.setView(view);
                break;
        }

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = editText.getText().toString();
                Log.e("PersonalDataActivity", content);
                setpersonaldata(type, content,null);
            }
        });
        builder.setNegativeButton("取消", null);

        builder.create();
        builder.show();

    }


    /**
     * 显示spiner的dialog
     * @param type 显示类型
     */
    public void setSpinerdialog(final Integer type){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View view1 = inflater.inflate(R.layout.activity_set_spiner_dialog,null);
        final Spinner spinner1 = (Spinner) view1.findViewById(R.id.sp_set_faculty);

        final View view = inflater.inflate(R.layout.activity_set_spiner_sex_dialog,null);
        final Spinner spinner = (Spinner) view.findViewById(R.id.sp_set_sex);

        switch (type){
            case 3:
                builder.setTitle("性别修改");
                builder.setView(view);
                break;
            case 5:
                builder.setTitle("院系修改");
                builder.setView(view1);
                break;
        }
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = "";
                switch (type) {
                    case 3:
                        content = spinner.getSelectedItem().toString();
                        break;
                    case 5:
                        content = spinner1.getSelectedItem().toString();
                        break;
                }
                setpersonaldata(type, content,null);
            }
        });
        builder.setNegativeButton("取消", null);

        builder.create();
        builder.show();
    }


    /**
     * 向服务器请求修改信息
     * @param type 请求类型
     * @param content 请求内容
     * @param headerfilename 只有当type为1的时候，也就是修改头像的时候，才会传入这个参数
     */
    public void setpersonaldata(final Integer type, final String content,final String headerfilename){
        User newuser = new User();

        final String oldHeaderfilename = BmobUser.getCurrentUser(this,User.class).getHeaderfilename();

        switch (type){
            case 1:
                newuser.setHeadSculpture(content);
                newuser.setHeaderfilename(headerfilename);
                break;
            case 2:
                newuser.setUsername(content);
                break;
            case 3:
                if("男".equals(content)) {
                    newuser.setGender(true);
                }else {
                    newuser.setGender(false);
                }
                break;
            case 4:
                newuser.setAutograhp(content);
                break;
            case 5:
                newuser.setFaculty(content);
                break;
            case 6:
                newuser.setStu_number(content);
                break;
        }

        progressDialog.show();
        newuser.update(this, user.getObjectId(), new UpdateListener() {
            @Override
            public void onSuccess() {
                switch (type) {
                    case 1:
                        bitmapUtils.display(head, content);
                        deletepicture(oldHeaderfilename);
                        break;
                    case 2:
                        name.setText(content);
                        break;
                    case 3:
                        if ("男".equals(content)) {
                            sex.setText("帅哥");
                        } else {
                            sex.setText("美女");
                        }
                        break;
                    case 4:
                        autograhp.setText(content);
                        break;
                    case 5:
                        faculty.setText(content);
                        break;
                    case 6:
                        stunumber.setText(content);
                        break;
                }
                progressDialog.dismiss();
                Toast.makeText(PersonalDataActivity.this, "信息修改成功！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e("PersonalDataActivity", i + "  " + s);
                Toast.makeText(PersonalDataActivity.this, "信息修改失败！", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }



    public void tackphoto(){
        String photo_dir = AbFileUtil.getImageDownloadDir(this);
        if (AbStrUtil.isEmpty(photo_dir)) {
            AbToastUtil.showToast(this, "");
        } else {
            PHOTO_DIR = new File(photo_dir);
        }
        dialogTakePic = View.inflate(this, R.layout.dialog_choose_photo, null);
        TextView tvCam = (TextView) dialogTakePic.findViewById(R.id.tvselectphoto);
        tvCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
                    AbDialogUtil.removeDialog(dialogTakePic);
                } catch (ActivityNotFoundException e) {
                    AbToastUtil.showToast(PersonalDataActivity.this, "");
                }
            }
        });
        TextView tvTake = (TextView) dialogTakePic.findViewById(R.id.tvtakephoto);
        tvTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbDialogUtil.removeDialog(dialogTakePic);
                doPickPhotoAction();
            }
        });
    }




    /**
     *
     */
    private void doPickPhotoAction() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            doTakePhoto();
        } else {
            AbToastUtil.showToast(this, "");
        }
    }

    /**
     *
     */
    protected void doTakePhoto() {
        try {
            mFileName = System.currentTimeMillis() + ".jpg";
            mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
            startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (Exception e) {
            AbToastUtil.showToast(this, "");
        }
    }

    /**
     *
     */
    public String getPath(Uri uri) {
        if (AbStrUtil.isEmpty(uri.getAuthority())) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        return path;
    }

    /**
     *
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PHOTO_PICKED_WITH_DATA:
                Uri uri = mIntent.getData();
                String currentFilePath = getPath(uri);
                //upLoadHeader(getStringpics(currentFilePath));
                if (!AbStrUtil.isEmpty(currentFilePath)) {
                    Intent intent1 = new Intent(PersonalDataActivity.this, CropImageActivity.class);
                    intent1.putExtra("PATH", currentFilePath);
                    startActivityForResult(intent1, CAMERA_CROP_DATA);
                } else {
                    AbToastUtil.showToast(PersonalDataActivity.this, "未在存储卡中找到这个文件");
                }
                break;
            case CAMERA_WITH_DATA:
                AbLogUtil.d(ShuoshuoPubishActivity.class, "" + mCurrentPhotoFile.getPath());
                String currentFilePath2 = mCurrentPhotoFile.getPath();
                //upLoadHeader(getStringpics(currentFilePath2));
                Intent intent2 = new Intent(PersonalDataActivity.this, CropImageActivity.class);
                intent2.putExtra("PATH", currentFilePath2);
                startActivityForResult(intent2, CAMERA_CROP_DATA);

                break;
            case CAMERA_CROP_DATA:
                String path = mIntent.getStringExtra("PATH");
                AbLogUtil.d(MerchandisePublishActivity.class, "裁剪后得到的图片的路径是 = " + path);
                upLoadHeader(getStringpics(path));
                /*mAdapter.addItem(mAdapter.getCount() - 1, path);
                camIndex++;*/
                break;
        }
    }


    public void upLoadHeader(String picpath){
        progressDialog.show();
        BTPFileResponse response = BmobProFile.getInstance(this).upload(picpath, new UploadListener() {
            @Override
            public void onSuccess(String s, String s1, BmobFile bmobFile) {
                setpersonaldata(1,bmobFile.getUrl(),s);
            }

            @Override
            public void onProgress(int i) {

            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    public void deletepicture(String picpath){
        BmobProFile.getInstance(this).deleteFile(picpath, new DeleteFileListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Toast.makeText(PersonalDataActivity.this, "信息修改成功！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(PersonalDataActivity.this, "信息修改成功！", Toast.LENGTH_SHORT).show();
            }
        });

    }








    /**
     * 获取本地图片url
     * @return
     */
    public String getStringpics(String picpath){
        //如果我要上传的图片是大于200kb
        if(Double.valueOf(UriToSize(picpath)) > Double.valueOf("200")) {
            return BitmapHelper.getImageThumbnail(picpath);
        }else {
            return picpath;
        }
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


        return fileSizeString;
    }


}
