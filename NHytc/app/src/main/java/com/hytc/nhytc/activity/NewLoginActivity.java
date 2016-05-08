package com.hytc.nhytc.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hytc.nhytc.R;
import com.hytc.nhytc.domain.TimeMsgEB;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.tool.ControlOnclick;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import de.greenrobot.event.EventBus;


/**
 * Created by Administrator on 2015/8/22.
 */
public class NewLoginActivity extends Activity implements View.OnClickListener, TextWatcher {
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;

    /**
     * 界面数据
     */
    private EditText et_phone;
    private EditText et_stunumber;
    private EditText et_pwd;
    private EditText et_pwd_sure;
    private EditText et_code;
    private Spinner sp_faculty;
    private Spinner sp_gender;
    private Button bt_getcode;

    private ControlOnclick control;

    private ProgressDialog progressDialog;

    /**
     *短信验证
     */
    private TimeMsgEB msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newlogin);
        EventBus.getDefault().register(this);
        initTitle();
        initWdiget();
    }


    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);
        tvinfo.setVisibility(View.VISIBLE);
        tvinfo.setText("完成注册");
        titlename.setText("注册");
        tvinfo.setOnClickListener(this);
        ivinfo.setVisibility(View.GONE);
        ivmore.setVisibility(View.GONE);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean isCanRegister(){
        Boolean isok = false;
        if(et_phone.getText().toString().trim().equals("")){
            Toast.makeText(this,"注册手机号不能为空！",Toast.LENGTH_SHORT).show();
        }else if(et_stunumber.getText().toString().trim().equals("")){
            Toast.makeText(this,"学号不能为空！",Toast.LENGTH_SHORT).show();
        }else if(!ispwdok()){
            Toast.makeText(this,"密码为空或两次输入不同！",Toast.LENGTH_SHORT).show();
        }else if(et_code.getText().toString().trim().equals("")){
            Toast.makeText(this,"验证码不能为空！",Toast.LENGTH_SHORT).show();
        }else {
            isok = true;
        }
        return isok;
    }

    public boolean ispwdok(){
        Boolean isok = false;
        String pwd1 = et_pwd.getText().toString().trim();
        String pwd2 = et_pwd_sure.getText().toString().trim();
        if(pwd1.equals("") || !pwd1.equals(pwd2)) {
            return isok;
        }
        else {
            isok = true;
            return isok;
        }
    }

    public void initWdiget(){
        et_phone = (EditText) this.findViewById(R.id.et_phone_new_login);
        et_stunumber = (EditText) this.findViewById(R.id.et_stu_number_newlogin);
        et_pwd = (EditText) this.findViewById(R.id.et_password1_new_login);
        et_pwd_sure = (EditText) this.findViewById(R.id.et_password2_new_login);
        et_code = (EditText) this.findViewById(R.id.et_code_new_login);
        sp_faculty = (Spinner) this.findViewById(R.id.sp_faculty);
        sp_gender = (Spinner) this.findViewById(R.id.sp_gender);
        bt_getcode = (Button) this.findViewById(R.id.bt_sure_get_code);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("请稍等...");
        control = new ControlOnclick();

        msg = new TimeMsgEB();

        bt_getcode.setOnClickListener(this);
        bt_getcode.setEnabled(false);
        et_phone.addTextChangedListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_mark_titlebar:
                if(isCanRegister()){
                    progressDialog.show();
                    tvinfo.setClickable(false);
                        getUser().signOrLogin(this, et_code.getText().toString().trim(), new SaveListener() {
                            @Override
                            public void onSuccess() {
                                progressDialog.dismiss();
                                tvinfo.setClickable(true);
                                Toast.makeText(NewLoginActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();

                                /**
                                 * 跳转到登录界面界面
                                 */
                                Intent intent = new Intent();
                                intent.setClass(NewLoginActivity.this, LoginActivity.class);
                                startActivity(intent);
                                NewLoginActivity.this.finish();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                progressDialog.dismiss();
                                tvinfo.setClickable(true);
                                Log.e("newlogin", i + "     " + s);
                                if(i == 202){
                                    Toast.makeText(NewLoginActivity.this, "用户名已存在！", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(NewLoginActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
                break;
            case R.id.bt_sure_get_code:
                bt_getcode.setEnabled(false);
                final BmobQuery<User> query = new BmobQuery<>();
                query.addWhereEqualTo("mobilePhoneNumber", et_phone.getText().toString().trim());
                query.findObjects(this, new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> list) {
                        if (list.size() == 1) {
                            bt_getcode.setEnabled(true);
                            Toast.makeText(NewLoginActivity.this, "该用户已经注册！", Toast.LENGTH_SHORT).show();
                        } else {
                            sendcode(et_phone.getText().toString().trim());
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        bt_getcode.setEnabled(true);
                        Toast.makeText(NewLoginActivity.this, "获取失败\n请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
        }
    }

    private void sendcode(String trim) {
        BmobSMS.requestSMSCode(this, trim, "注册新", new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, BmobException e) {

                if (e == null) {//验证码发送成功
                    Toast.makeText(NewLoginActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    new Thread(new Timethrend()).start();
                } else {
                    Log.e("newlogin",e.toString());
                    bt_getcode.setEnabled(true);
                    Toast.makeText(NewLoginActivity.this, "验证码发送失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String phonenumber;
        phonenumber = et_phone.getText().toString().trim();
        if(phonenumber.length() != 11 ){
            bt_getcode.setEnabled(false);
        }else {
            bt_getcode.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private class Timethrend implements Runnable{
        @Override
        public void run() {
            msg.setI(60);
            while (msg.getI() != 0){
                msg.setI(msg.getI() - 1);
                EventBus.getDefault().post(msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    stopTimer();
                    Toast.makeText(NewLoginActivity.this,"计数器故障，请稍后再试！",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void onEventMainThread(TimeMsgEB msg) {
        if(msg.getI() == 59){
            et_phone.setEnabled(false);
        }
        if (msg.getI() == 0) {
            stopTimer();
            return;
        }
        if (msg.getI() < 10) {
            bt_getcode.setText("0" + msg.getI() + " s");
        } else {
            bt_getcode.setText(msg.getI() + " s");
        }
    }

    private void stopTimer() {
        bt_getcode.setEnabled(true);
        et_phone.setEnabled(true);
        bt_getcode.setText("再次获取");
    }


    public User getUser(){
        User user = new User();
        StringBuilder buildername = new StringBuilder(et_phone.getText().toString().trim());
        user.setUsername(buildername.substring(0, 3) + "****" + buildername.substring(7, 11));
        user.setMobilePhoneNumber(buildername.toString());
        user.setStu_number(et_stunumber.getText().toString().trim());
        user.setFaculty(sp_faculty.getSelectedItem().toString().trim());
        user.setPraise(0);
        user.setLoved(0);
        user.setAutograhp("什么也没说...");
        if(sp_gender.getSelectedItem().toString().trim().equals("男")){
            user.setGender(true);
            user.setHeadSculpture("http://115.29.51.191:8080/static/head/boy.jpg");
        }else {
            user.setGender(false);
            user.setHeadSculpture("http://115.29.51.191:8080/static/head/girl.jpg");
        }
        user.setPassword(et_pwd.getText().toString().trim());

        return user;
    }


}
