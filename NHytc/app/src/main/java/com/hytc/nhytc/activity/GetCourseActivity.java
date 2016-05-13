package com.hytc.nhytc.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hytc.nhytc.R;
import com.hytc.nhytc.dbDAO.CourseDao;
import com.hytc.nhytc.domain.Lesson;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.tool.CourseTools;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/27.
 */
public class GetCourseActivity extends Activity implements View.OnClickListener {
    @Bind(R.id.et_stu_number)
    EditText etStuNumber;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.chb_remember)
    CheckBox chbRemember;


    private ImageView ivCode;
    private TextView tvGetCodeAgain;
    private EditText etCode;
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;

    private String code_pic_url = "";
    private String cookie = "";

    private View dialogview;
    private LayoutInflater inflater;
    private BitmapUtils bitmapUtils;
    private CourseDao dao;

    /**
     * 一开始设置为false，当点击换一张验证码时，设置为true，此时在网络获取验证码时，如果获取成功，那么
     * 就判断一下isok ，如果为true，就调用一下显示dialog，否则就不调用
     */
    private Boolean isok = false;

    private Boolean isSave = false;
    private SharedPreferences sp;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_course);
        ButterKnife.bind(this);
        initTitle();
        parse_data();
        chbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSave = isChecked ? true : false;
            }
        });
    }

    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("课程数据获取中，请稍等...");
        inflater = LayoutInflater.from(this);
        bitmapUtils = BitmapHelper.getBitmapUtils(this);
        dao = new CourseDao(this);
        sp = getSharedPreferences("first",Activity.MODE_PRIVATE);
        String stu_num = sp.getString("stunum","");
        String stu_pwd = sp.getString("stupwd","");
        Boolean sava = sp.getBoolean("issave",false);
        chbRemember.setChecked(sava);
        etStuNumber.setText(stu_num);
        etPassword.setText(stu_pwd);

        tvinfo.setVisibility(View.VISIBLE);
        tvinfo.setText("继续");
        titlename.setText("获取课表");
        tvinfo.setOnClickListener(this);
        ivmore.setVisibility(View.GONE);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mark_titlebar:
                if (!"".equals(cookie)) {
                    if(isok()){
                        if(isSave) {
                            saveData();
                        }else {
                            deleteData();
                        }
                        getDialog();
                    }
                } else {
                    Toast.makeText(GetCourseActivity.this, "获取验证码失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



    private void parse_data() {
        RequestParams params = new RequestParams();
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, "http://115.29.51.191:8080/get_code", params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject data = new JSONObject(responseInfo.result);
                    if ("SUCCESS".equals(data.get("status"))) {
                        JSONObject back_data = data.getJSONObject("data");
                        code_pic_url = "http://115.29.51.191:8080/static/code/" + back_data.getString("code_path") + ".jpg";
                        cookie = back_data.getString("cookie");
                        if(isok){
                            bitmapUtils.display(ivCode, code_pic_url);
                            isok = false;
                        }
                    } else {
                        Toast.makeText(GetCourseActivity.this, "请检查网络连接！", Toast.LENGTH_SHORT).show();
                        isok = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(GetCourseActivity.this, "请检查网络连接！", Toast.LENGTH_SHORT).show();
                    isok = false;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(GetCourseActivity.this, "请检查网络连接！", Toast.LENGTH_SHORT).show();
                isok = false;
            }
        });
    }


    public void getDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("验证码");
        dialogview = inflater.inflate(R.layout.dialog_get_course, null);
        ivCode = (ImageView) dialogview.findViewById(R.id.iv_code);
        tvGetCodeAgain = (TextView) dialogview.findViewById(R.id.tv_get_code_again);
        etCode = (EditText) dialogview.findViewById(R.id.et_code);
        tvGetCodeAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isok = true;
                parse_data();
            }
        });
        dialog.setView(dialogview);
        dialog.setNegativeButton("取消", null);
        bitmapUtils.display(ivCode, code_pic_url);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getCourseData();
            }
        });
        dialog.create();
        dialog.show();
    }


    public void getCourseData(){
        progressDialog.show();
        RequestParams params = new RequestParams();
        params.addBodyParameter("cookie",cookie);
        params.addBodyParameter("code", etCode.getText().toString());
        params.addBodyParameter("stu_number", etStuNumber.getText().toString());
        params.addBodyParameter("pwd", etPassword.getText().toString());
        params.addBodyParameter("year", "2015-2016");
        params.addBodyParameter("count", "1");
        params.addBodyParameter("is_get_nown", "no");
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, "http://115.29.51.191:8080/get_course", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    List<Lesson> lessons = new ArrayList<Lesson>();
                    if ("SUCCESS".equals(jsonObject.getString("status"))) {
                        JSONArray items = jsonObject.getJSONArray("data");
                        List<String> lesson_data = new ArrayList<String>();
                        for (int i = 0; i < items.length(); i++) {
                            JSONArray datas = items.getJSONArray(i);
                            lesson_data.clear();
                            for (int j = 0; j < datas.length(); j++) {
                                lesson_data.add(datas.getString(j));
                            }
                            lessons.addAll(CourseTools.doWithCourse(lesson_data));
                        }
                        dao.deleteAllData();
                        dao.addData(lessons);
                        progressDialog.dismiss();
                        GetCourseActivity.this.finish();
                    } else {
                        Toast.makeText(GetCourseActivity.this, "课程数据获取失败！", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Toast.makeText(GetCourseActivity.this, "课程数据获取失败！", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(GetCourseActivity.this, "课程数据获取失败！", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private Boolean isok(){
        Boolean isok = false;
        if("".equals(etStuNumber.getText().toString())){
            Toast.makeText(this,"学号不能为空！",Toast.LENGTH_SHORT).show();
        }else if("".equals(etPassword.getText().toString())){
            Toast.makeText(this,"密码不能为空！",Toast.LENGTH_SHORT).show();
        }else {
            isok = true;
        }
        return isok;
    }


    private void saveData(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("stunum",etStuNumber.getText().toString());
        editor.putString("stupwd",etPassword.getText().toString());
        editor.putBoolean("issave",isSave);
        editor.apply();
    }

    private void deleteData() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("stunum","");
        editor.putString("stupwd","");
        editor.putBoolean("issave", isSave);
        editor.apply();
    }


}
