package com.hytc.nhytc.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hytc.nhytc.R;
import com.hytc.nhytc.domain.Activities;
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
    private String picName = "";
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
    private int startTimeYear;
    private int startTimeMonth;
    private int startTimeDay;
    private int startTimeHour;
    private int startTimeMinute;
    private int endTimeYear;
    private int endTimeMonth;
    private int endTimeDay;
    private int endTimeHour;
    private int endTimeMinute;
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
    private List<String> list;
    private ArrayAdapter<String> themeAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_new_activities_publish);
        ButterKnife.bind(this);
        inittitle();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void inittitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        bitmapUtils = BitmapHelper.getBitmapUtils(this);

        calendar = Calendar.getInstance();
        list = new ArrayList<String>();

        list.add("聚餐");
        list.add("出游");
        list.add("游戏");
        list.add("学习");
        list.add("筹款");
        list.add("晚会");
        list.add("学术报告");
        list.add("个人表演");
        list.add("其他");
        themeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTheme.setAdapter(themeAdapter);
        spTheme.setVisibility(View.VISIBLE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("活动发布中 0% ...");
        nowYear = calendar.get(Calendar.YEAR);
        nowMonth = calendar.get(Calendar.MONTH);
        nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        nowMinute = calendar.get(Calendar.MINUTE);
        int nowMonthTemp = nowMonth + 1;
        tvStarttime.setText(nowYear + "-" + nowMonthTemp + "-" + nowDay + " | " + nowHour + ":" + nowMinute);
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
                themeSelected = themeAdapter.getItem(position);
                themeCode = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Toast.makeText(ActivitiesPublishActivity.this,themeAdapter.getItem(0),Toast.LENGTH_SHORT).show();
                themeSelected = themeAdapter.getItem(0);
                themeCode = 0;
            }
        });
    }

    @OnClick({R.id.tv_activities_publish_starttime, R.id.tv_activities_publish_endtime, R.id.bt_activities_publish_choosepic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_activities_publish_starttime:
                //开始时间
                startTime = "";
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ActivitiesPublishActivity.this.startTimeHour = hourOfDay;
                        ActivitiesPublishActivity.this.startTimeMinute = minute;
                        if (ActivitiesPublishActivity.this.startTimeMinute == 0) {
                            startTime = startTime + " | " + ActivitiesPublishActivity.this.startTimeHour + ":00";
                        } else if (startTimeMinute > 0 && startTimeMinute < 10) {
                            startTime = startTime + " | " + ActivitiesPublishActivity.this.startTimeHour + ":0" + ActivitiesPublishActivity.this.startTimeMinute;
                        } else {
                            startTime = startTime + " | " + ActivitiesPublishActivity.this.startTimeHour + ":" + ActivitiesPublishActivity.this.startTimeMinute;
                        }
                        tvStarttime.setText(startTime);
                    }
                }, nowHour, nowMinute, true);
                timePickerDialog.show();

                DatePickerDialog dpd = new DatePickerDialog(this, new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ActivitiesPublishActivity.this.startTimeYear = year;
                        ActivitiesPublishActivity.this.startTimeMonth = monthOfYear;
                        ActivitiesPublishActivity.this.startTimeDay = dayOfMonth;
                        int monthTemp = monthOfYear + 1;
                        startTime = ActivitiesPublishActivity.this.startTimeYear + "-" + monthTemp + "-" + startTimeDay;
                    }
                }, nowYear, nowMonth, nowDay);

                dpd.show();
                break;
            case R.id.tv_activities_publish_endtime:
                //结束时间
                endTime = "";
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ActivitiesPublishActivity.this.endTimeHour = hourOfDay;
                        ActivitiesPublishActivity.this.endTimeMinute = minute;
                        if (minute == 0) {
                            endTime = endTime + " | " + hourOfDay + ":00";
                        } else if (endTimeMinute > 0 && endTimeMinute < 10) {
                            endTime = endTime + " | " + ActivitiesPublishActivity.this.endTimeHour + ":0" + ActivitiesPublishActivity.this.endTimeMinute;
                        } else {
                            endTime = endTime + " | " + hourOfDay + ":" + minute;
                        }
                        tvEndtime.setText(endTime);
                    }
                }, ActivitiesPublishActivity.this.startTimeHour, ActivitiesPublishActivity.this.startTimeMinute, true);
                timePickerDialog1.show();

                DatePickerDialog dpd1 = new DatePickerDialog(this, new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ActivitiesPublishActivity.this.endTimeYear = year;
                        ActivitiesPublishActivity.this.endTimeMonth = monthOfYear;
                        ActivitiesPublishActivity.this.endTimeDay = dayOfMonth;
                        int monthOfYearTemp = monthOfYear + 1;
                        endTime = year + "-" + monthOfYearTemp + "-" + dayOfMonth;
                    }
                }, ActivitiesPublishActivity.this.startTimeYear, ActivitiesPublishActivity.this.startTimeMonth, ActivitiesPublishActivity.this.startTimeDay);
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
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                picPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0);
                bitmapUtils.display(ivPic, picPath);
            }
        }
    }

    public void upLoadPic(String picPath) {
        final BmobFile bmobFile = new BmobFile(new File(picPath));

        bmobFile.uploadblock(this, new UploadFileListener() {

            @Override
            public void onSuccess() {
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                picURL = bmobFile.getFileUrl(ActivitiesPublishActivity.this);
                picName = bmobFile.getFilename();//获取图片在服务器端的名字
                Toast.makeText(ActivitiesPublishActivity.this, "上传文件成功:", Toast.LENGTH_SHORT).show();
                parseData(picURL, picName);
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
                progressDialog.setMessage("活动发布中 " + value + "% ...");
                // curIndex    :表示当前第几个文件正在上传
                // curPercent  :表示当前上传文件的进度值（百分比）
                // total       :表示总的上传文件数
                // totalPercent:表示总的上传进度（百分比）
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(
                        ActivitiesPublishActivity.this,
                        "上传文件失败：" + msg,
                        Toast.LENGTH_SHORT
                ).show();
                progressDialog.dismiss();
            }
        });
    }

    public Boolean isOk() {
        Boolean result = true;

        if ("".equals(etName.getText().toString())) {
            result = false;
            Toast.makeText(this, "活动名称不能为空", Toast.LENGTH_SHORT).show();
        } else if ("".equals(tvStarttime.getText().toString())) {
            result = false;
            Toast.makeText(this, "开始时间还未选择", Toast.LENGTH_SHORT).show();
        } else if ("".equals(tvEndtime.getText().toString())) {
            result = false;
            Toast.makeText(this, "结束时间还选择", Toast.LENGTH_SHORT).show();
        } else if ("".equals(etPlace.getText().toString())) {
            result = false;
            Toast.makeText(this, "活动地点不能为空", Toast.LENGTH_SHORT).show();
        } else if ("".equals(etContent.getText().toString())) {
            result = false;
            Toast.makeText(this, "活动内容不能为空", Toast.LENGTH_SHORT).show();
        } else if ("".equals(etHolder.getText().toString())) {
            result = false;
            Toast.makeText(this, "活动举办者不能为空", Toast.LENGTH_SHORT).show();
        } else if (!isTimeOK()) {
            result = false;
            Toast.makeText(this, "活动时间不合法\n请检查开始时间与结束时间的正确性", Toast.LENGTH_SHORT).show();
        } else if ("".equals(etConnect.getText().toString().trim())) {
            result = false;
            Toast.makeText(this, "请填写联系方式", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public boolean isTimeOK() {
        boolean result = true;
        if (startTimeYear < nowYear || startTimeDay < nowDay || startTimeMonth < nowMonth) {
            result = false;
        } else if (endTimeYear < nowYear || endTimeDay < nowDay || endTimeMonth < nowMonth) {
            result = false;
        } else if (endTimeYear < startTimeYear) {
            result = false;
        } else if (endTimeMonth < startTimeMonth) {
            result = false;
        } else if (endTimeDay < startTimeDay) {
            result = false;
        } else if (endTimeHour < startTimeHour) {
            result = false;
        } else if (endTimeMinute < startTimeMinute) {
            result = false;
        }
        return result;
    }

    /**
     * 获取本地图片url
     *
     * @return
     */
    public String getStringpics() {
        String bmobpic = "";
        if (Double.valueOf(UriToSize(picPath)) > Double.valueOf("200")) {
            bmobpic = BitmapHelper.getImageThumbnail(picPath);
        } else {
            bmobpic = picPath;
        }
        return bmobpic;
    }

    /**
     * 通过传入的 本地路径 获取 图片大小
     *
     * @param s 图片的存储路径
     * @return KB MB
     */
    public String UriToSize(String s) {
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
     *
     * @param
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0";
        if (fileS == 0) {
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

    public void parseData(String s, String picName) {
        Activities activities = new Activities();
        activities.setName(etName.getText().toString().trim());
        activities.setContent(etContent.getText().toString().trim());
        activities.setStartTime(startTime.trim());
        activities.setEndTime(endTime.trim());
        activities.setHolder(etHolder.getText().toString().trim());
        activities.setMoreContent(etMoreContent.getText().toString().trim());
        activities.setPlace(etPlace.getText().toString().trim());
        activities.setTheme(themeSelected.trim());
        activities.setThemeCode(themeCode);
        activities.setConnection(etConnect.getText().toString().trim());
        activities.setPicName(picName);
        activities.setPicURL(s);
        activities.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Toast.makeText(ActivitiesPublishActivity.this, "活动发布成功！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(ActivitiesPublishActivity.this, "活动发布失败！", Toast.LENGTH_SHORT).show();
                tvinfo.setClickable(true);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ActivitiesPublish Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.hytc.nhytc.activity/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ActivitiesPublish Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.hytc.nhytc.activity/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
