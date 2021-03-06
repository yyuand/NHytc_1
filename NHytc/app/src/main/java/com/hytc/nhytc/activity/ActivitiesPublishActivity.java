package com.hytc.nhytc.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hytc.nhytc.R;
import com.hytc.nhytc.domain.Activities;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.tool.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by dongf_000 on 2016/5/11 0011.
 */
public class ActivitiesPublishActivity extends Activity {
    @Bind(R.id.et_activites_pubulish_name)
    EditText etName;
    @Bind(R.id.sp_activities_publish_theme)
    Spinner spTheme;
    @Bind(R.id.tv_activities_publish_starttime)
    TextView tvStarttime;
    @Bind(R.id.tv_activities_publish_endtime)
    TextView tvEndtime;
    @Bind(R.id.et_activites_pubulish_place)
    EditText etPlace;
    @Bind(R.id.et_activites_pubulish_holder)
    EditText etHolder;
    @Bind(R.id.et_activites_pubulish_content)
    EditText etContent;
    @Bind(R.id.et_activites_pubulish_more)
    EditText etMoreContent;
    @Bind(R.id.bt_activities_publish_choosepic)
    Button btChoosepic;
    @Bind(R.id.iv_activities_publish_pic)
    ImageView ivPic;
    @Bind(R.id.et_activites_pubulish_connect)
    EditText etConnect;

    private String picURL = "";
    private String picPath = "";
    private String picName="";
    private static final int REQUEST_IMAGE = 2;

    private BitmapUtils bitmapUtils;
    /**
     * title
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;
    private Calendar calendar;
    private int startTimeYear =1970;
    private int startTimeMonth =0;
    private int startTimeDay =0;
    private int startTimeHour =0;
    private int startTimeMinute =0;
    private int endTimeYear =0;
    private int endTimeMonth =0;
    private int endTimeDay =0;
    private int endTimeHour =0;
    private int endTimeMinute =0;
    private int nowYear;
    private int nowMonth;
    private int nowDay;
    private int nowHour;
    private int nowMinute;
    private int themeCode;
    private ProgressDialog progressDialog;
    private String themeSelected;
    private String startTime;
    private String endTime;
    private String HourTemp;
    private String MinuteTemp;
    private List<String> list;
    private ArrayAdapter<String> themeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_new_activities_publish);
        ButterKnife.bind(this);
        inittitle();
    }

    private void inittitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);
        calendar=Calendar.getInstance();
        bitmapUtils = BitmapHelper.getBitmapUtils(this);

        list=new ArrayList<String>();

        list.add("聚餐");
        list.add("出游");
        list.add("游戏");
        list.add("学习");
        list.add("筹款");
        list.add("晚会");
        list.add("学术报告");
        list.add("个人表演");
        list.add("其他");
        themeAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTheme.setAdapter(themeAdapter);
        spTheme.setVisibility(View.VISIBLE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("活动发布中 0% ...");
        nowYear=calendar.get(Calendar.YEAR);
        nowMonth=calendar.get(Calendar.MONTH);
        nowDay=calendar.get(Calendar.DAY_OF_MONTH);
        nowHour=calendar.get(Calendar.HOUR_OF_DAY);
        nowMinute=calendar.get(Calendar.MINUTE);
        //为开始时间赋初值
        startTimeYear = nowYear;
        startTimeMonth = nowMonth;
        startTimeDay = nowDay;
        startTimeMinute =nowMinute;
        startTimeHour = nowHour;

        int nowMonthTemp=nowMonth+1;

        if(nowMinute >=0 && nowMinute <10){
            MinuteTemp = "0" + nowMinute;
        }
        else
            MinuteTemp = nowMinute+"";
        if(nowHour >=0 && nowHour <10){
            HourTemp = "0"+ nowHour;
        }
        else
            HourTemp =""+ nowHour;
        tvStarttime.setText(nowYear+"/"+nowMonthTemp+"/"+nowDay+" - "+HourTemp+":"+MinuteTemp);
        tvEndtime.setText(nowYear+"/"+nowMonthTemp+"/"+nowDay+" - "+HourTemp+":"+MinuteTemp);
        titlename.setText("活动发布");
        ivinfo.setVisibility(View.GONE);
        tvinfo.setVisibility(View.VISIBLE);
        ivmore.setVisibility(View.GONE);
        tvinfo.setText("发布");
        tvinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOk()) {
                    tvinfo.setClickable(false);
                    progressDialog.show();
                    upLoadPic(getStringpics());
                }
            }
        });
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ActivitiesPublishActivity.this, themeAdapter.getItem(position), Toast.LENGTH_SHORT).show();
                themeSelected=themeAdapter.getItem(position);
                themeCode = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Toast.makeText(ActivitiesPublishActivity.this,themeAdapter.getItem(0),Toast.LENGTH_SHORT).show();
                themeSelected = themeAdapter.getItem(0);
                themeCode = 0;
            }
        });
        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/
    }

    @OnClick({R.id.rl_activities_publish_starttime, R.id.rl_activities_publish_endtime, R.id.bt_activities_publish_choosepic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_activities_publish_starttime:
                //开始时间
                startTime = "";
                TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        ActivitiesPublishActivity.this.startTimeHour=hourOfDay;
                        ActivitiesPublishActivity.this.startTimeMinute=minute;
                        /*if(ActivitiesPublishActivity.this.startTimeMinute==0){
                            startTime=startTime+" - "+ ActivitiesPublishActivity.this.startTimeHour+":00";
                        }
                        else if(startTimeMinute > 0 && startTimeMinute < 10){
                            startTime=startTime+" - "+ ActivitiesPublishActivity.this.startTimeHour+":0"+ ActivitiesPublishActivity.this.startTimeMinute;
                        }else {
                            startTime=startTime+" - "+ ActivitiesPublishActivity.this.startTimeHour+":"+ ActivitiesPublishActivity.this.startTimeMinute;
                        }*/
                        if(startTimeMinute >=0 && startTimeMinute <10){
                            MinuteTemp = "0"+startTimeMinute;
                        }
                        else
                            MinuteTemp = startTimeMinute+"";
                        if(startTimeHour >=0 && startTimeHour <10){
                            HourTemp = "0"+ startTimeHour;
                        }
                        else
                            HourTemp =""+startTimeHour;
                        startTime=startTime+" - "+ HourTemp+":"+ MinuteTemp;
                        tvStarttime.setText(startTime);
                    }
                },nowHour,nowMinute,true);
                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        ActivitiesPublishActivity.this.startTimeHour = nowHour;
                        ActivitiesPublishActivity.this.startTimeMinute = nowMinute;
                        if(startTimeMinute >=0 && startTimeMinute <10){
                            MinuteTemp = "0"+startTimeMinute;
                        }
                        else
                            MinuteTemp = startTimeMinute+"";
                        if(startTimeHour >=0 && startTimeHour <10){
                            HourTemp = "0"+ startTimeHour;
                        }
                        else
                            HourTemp =""+startTimeHour;
                        startTime=startTime+" - "+ HourTemp+":"+ MinuteTemp;
                        tvStarttime.setText(startTime);
                    }
                });
                timePickerDialog.show();
                DatePickerDialog dpd=new DatePickerDialog(this, new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ActivitiesPublishActivity.this.startTimeYear=year;
                        ActivitiesPublishActivity.this.startTimeMonth=monthOfYear;
                        ActivitiesPublishActivity.this.startTimeDay=dayOfMonth;
                        int monthTemp=monthOfYear+1;
                        startTime=ActivitiesPublishActivity.this.startTimeYear+"/"+monthTemp+"/"+startTimeDay;
                    }
                }, nowYear, nowMonth, nowDay);
                dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        ActivitiesPublishActivity.this.startTimeYear = nowYear;
                        ActivitiesPublishActivity.this.startTimeMonth = nowMonth;
                        ActivitiesPublishActivity.this.startTimeDay = nowDay;
                        int monthTemp= nowMonth+1;
                        startTime=ActivitiesPublishActivity.this.startTimeYear+"/"+monthTemp+"/"+startTimeDay;
                    }
                });
                dpd.show();
                break;
            case R.id.rl_activities_publish_endtime:
                //结束时间
                endTime = "";
                TimePickerDialog timePickerDialog1=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ActivitiesPublishActivity.this.endTimeHour=hourOfDay;
                        ActivitiesPublishActivity.this.endTimeMinute=minute;
                        /*if(minute==0){
                            endTime=endTime+" - "+hourOfDay+":00";
                        }
                        else if(endTimeMinute>0 && endTimeMinute<10){
                            endTime=endTime+" - "+ActivitiesPublishActivity.this.endTimeHour+":0"+ActivitiesPublishActivity.this.endTimeMinute;
                        }else {
                            endTime=endTime+" - "+hourOfDay+":"+minute;
                        }*/
                        if(endTimeMinute >=0 && endTimeMinute <10){
                            MinuteTemp = "0"+endTimeMinute;
                        }
                        else
                            MinuteTemp = ""+endTimeMinute;
                        if(endTimeHour >=0 && endTimeHour <10){
                            HourTemp = "0"+ endTimeHour;
                        }
                        else
                            HourTemp = ""+endTimeHour;
                        endTime=endTime+" - "+ HourTemp+":"+ MinuteTemp;
                        tvEndtime.setText(endTime);
                    }
                },nowHour,nowMinute,true);
                timePickerDialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        ActivitiesPublishActivity.this.endTimeHour= startTimeHour;
                        ActivitiesPublishActivity.this.endTimeMinute=startTimeMinute;
                        if(endTimeMinute >=0 && endTimeMinute <10){
                            MinuteTemp = "0"+endTimeMinute;
                        }
                        else
                            MinuteTemp = ""+endTimeMinute;
                        if(endTimeHour >=0 && endTimeHour <10){
                            HourTemp = "0"+ endTimeHour;
                        }
                        else
                            HourTemp = ""+endTimeHour;
                        endTime=endTime+" - "+ HourTemp+":"+ MinuteTemp;
                        tvEndtime.setText(endTime);
                    }
                });
                timePickerDialog1.show();

                DatePickerDialog dpd1=new DatePickerDialog(this, new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ActivitiesPublishActivity.this.endTimeYear=year;
                        ActivitiesPublishActivity.this.endTimeMonth=monthOfYear;
                        ActivitiesPublishActivity.this.endTimeDay=dayOfMonth;
                        int monthOfYearTemp=monthOfYear+1;
                        endTime=year+"/"+monthOfYearTemp+"/"+dayOfMonth;
                    }
                }, nowYear, nowMonth, nowDay);
                dpd1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        ActivitiesPublishActivity.this.endTimeYear=nowYear;
                        ActivitiesPublishActivity.this.endTimeMonth=nowMonth;
                        ActivitiesPublishActivity.this.endTimeDay=nowDay;
                        int monthOfYearTemp=nowMonth+1;
                        endTime=nowYear+"/"+monthOfYearTemp+"/"+nowDay;
                    }
                });
                dpd1.show();
                break;


            case R.id.bt_activities_publish_choosepic:
                //选择图片
                Intent intent = new Intent(this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                // 选择模式
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                picPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0);
                bitmapUtils.display(ivPic, picPath);
            }
        }
    }

    public void upLoadPic(String picPath){
        final BmobFile bmobFile = new BmobFile(new File(picPath));

        bmobFile.uploadblock(this, new UploadFileListener() {

            @Override
            public void onSuccess() {
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                picURL = bmobFile.getFileUrl(ActivitiesPublishActivity.this);
                picName = bmobFile.getFilename();//获取图片在服务器端的名字
                Toast.makeText(ActivitiesPublishActivity.this, "上传图片成功"/*+picURL+"picName"+picName*/, Toast.LENGTH_SHORT).show();
                parseData(picURL,picName);
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
                progressDialog.setMessage("活动发布中 " + value + "% ...");
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(ActivitiesPublishActivity.this, "图片失败失败，请检查网络连接" , Toast.LENGTH_SHORT).show();
                Log.i("活动发布失败原因",msg);
                progressDialog.dismiss();
                tvinfo.setClickable(true);
            }
        });

    }


    /**
     * 获取本地图片url
     * @return
     */
    public String getStringpics(){
        String bmobpic = "";
        if(Double.valueOf(UriToSize(picPath)) > Double.valueOf("200")) {
            bmobpic = BitmapHelper.getImageThumbnail(picPath);
        }else {
            bmobpic = picPath;
        }
        return bmobpic;
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

    public void parseData(String picURL1,String picName1) {
        Activities activities=new Activities();
        User user = BmobUser.getCurrentUser(this, User.class);
        activities.setAuthor(user);
        activities.setName(etName.getText().toString().trim());
        activities.setContent(etContent.getText().toString().trim());
        activities.setStartTime(startTime.trim());
        activities.setEndTime(endTime.trim());
        activities.setHolder(etHolder.getText().toString().trim());
        if(etMoreContent.getText().toString().trim().equals("")){
            //更多为空
            activities.setMoreContent("未填写");
        }
        else
            activities.setMoreContent(etMoreContent.getText().toString().trim());
        activities.setPlace(etPlace.getText().toString().trim());
        activities.setTheme(themeSelected.trim());
        activities.setThemeCode(themeCode);
        activities.setConnection(etConnect.getText().toString().trim());
        activities.setPicName(picName1);
        activities.setPicURL(picURL1);
        activities.save(ActivitiesPublishActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Toast.makeText(ActivitiesPublishActivity.this,"活动发布成功！", Toast.LENGTH_SHORT).show();
                //finish();
            }

            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(ActivitiesPublishActivity.this,"活动发布失败！",Toast.LENGTH_SHORT).show();
                Log.i("活动发布对象数据上传失败","未填写原因描述");
                tvinfo.setClickable(true);
            }
        });
    }

    public Boolean isOk(){
        Boolean result = true;
        if("".equals(etName.getText().toString())){
            result = false;
            Toast.makeText(this,"活动名称不能为空",Toast.LENGTH_SHORT).show();
        }else if("".equals(tvStarttime.getText().toString())){
            result = false;
            Toast.makeText(this,"开始时间还未选择",Toast.LENGTH_SHORT).show();
        }else if("".equals(tvEndtime.getText().toString())){
            result = false;
            Toast.makeText(this,"结束时间还选择",Toast.LENGTH_SHORT).show();
        }else if("结束时间".equals(etPlace.getText().toString())){
            result = false;
            Toast.makeText(this,"活动地点不能为空",Toast.LENGTH_SHORT).show();
        }else if("".equals(etContent.getText().toString())){
            result = false;
            Toast.makeText(this,"活动内容不能为空",Toast.LENGTH_SHORT).show();
        } else if("".equals(etHolder.getText().toString())){
            result = false;
            Toast.makeText(this,"活动举办者不能为空",Toast.LENGTH_SHORT).show();
        }else if(!isTimeOK()){
            result = false;
            Toast.makeText(this,"活动时间不合法\n请检查开始时间与结束时间的正确性",Toast.LENGTH_SHORT).show();
        }else if("".equals(etConnect.getText().toString().trim())){
            result=false;
            Toast.makeText(this,"请填写联系方式",Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public boolean isTimeOK(){
        boolean result=true;
        if(startTimeYear > nowYear){
            //Toast.makeText(this,"1",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(startTimeMonth > nowMonth) {
            //Toast.makeText(this,"3",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(startTimeDay > nowDay) {
            //Toast.makeText(this,"4",Toast.LENGTH_SHORT).show();
            return false;
        }
        //检测当前时间与开始时间

        if(startTimeYear < endTimeYear)
            return true;
        else if(startTimeMonth < endTimeMonth)
            return true;
        else if(startTimeDay < endTimeDay)
            return true;
        else if(startTimeHour < endTimeHour)
            return true;
        else if(startTimeMinute < endTimeMinute)
            return true;

        /*
        if(endTimeYear < startTimeYear)
        {
            Toast.makeText(this,"5",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(endTimeMonth < startTimeMonth && startTimeYear == endTimeYear)
        {
            Toast.makeText(this,"6",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(endTimeDay < startTimeDay && startTimeMonth==endTimeMonth )
        {
            Toast.makeText(this,"7",Toast.LENGTH_SHORT).show();
            result=false;
        }
        else if(endTimeHour < startTimeHour &&startTimeDay ==endTimeDay)
        {
            Toast.makeText(this,"8",Toast.LENGTH_SHORT).show();
            result=false;
        }
        else if(endTimeMinute < startTimeMinute && startTimeHour ==endTimeHour)
        {
            Toast.makeText(this,"9",Toast.LENGTH_SHORT).show();
            return false;
        }*/
        return result;
    }
}
