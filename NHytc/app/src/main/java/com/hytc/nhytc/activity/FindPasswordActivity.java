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
import android.widget.TextView;
import android.widget.Toast;


import com.hytc.nhytc.R;
import com.hytc.nhytc.domain.TimeMsgEB;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.tool.ControlOnclick;
import com.hytc.nhytc.tool.MyRYTokenManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.greenrobot.event.EventBus;


/**
 * Created by Administrator on 2015/8/22.
 */
public class FindPasswordActivity extends Activity implements View.OnClickListener, TextWatcher {
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;

    /**
     * 界面控件
     */
    private EditText phone;
    private EditText pwd;
    private EditText surepwd;
    private Button getcode;
    private EditText code;

    private ControlOnclick control;
    /**
     *短信验证
     */
    private TimeMsgEB msg;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpwd);
        EventBus.getDefault().register(this);
        initTitle();
        initwidget();
    }

    private void initwidget() {
        phone = (EditText) this.findViewById(R.id.et_phone_find_pwd);
        pwd = (EditText) this.findViewById(R.id.et_new_pwd);
        surepwd = (EditText) this.findViewById(R.id.et_sure_pwd);
        getcode = (Button) this.findViewById(R.id.bt_get_code);
        code = (EditText) this.findViewById(R.id.et_code_find_pwd);
        msg = new TimeMsgEB();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("密码重置中，请稍等...");
        control = new ControlOnclick();

        getcode.setOnClickListener(this);
        getcode.setEnabled(false);
        phone.addTextChangedListener(this);

    }


    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);
        tvinfo.setVisibility(View.VISIBLE);
        tvinfo.setText("确定");
        tvinfo.setOnClickListener(this);
        titlename.setText("找回密码");
        ivinfo.setVisibility(View.GONE);
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
        switch (v.getId()){
            case R.id.tv_mark_titlebar:
                if(isok()) {
                    tvinfo.setClickable(false);
                    progressDialog.show();
                    BmobUser.resetPasswordBySMSCode(this, code.getText().toString().trim(), surepwd.getText().toString(), new ResetPasswordByCodeListener() {

                        @Override
                        public void done(cn.bmob.v3.exception.BmobException e) {
                            progressDialog.dismiss();

                            if (e == null) {
                                Toast.makeText(FindPasswordActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.setClass(FindPasswordActivity.this, LoginActivity.class);
                                FindPasswordActivity.this.startActivity(intent);
                                tvinfo.setClickable(true);
                                FindPasswordActivity.this.finish();
                            } else {
                                tvinfo.setClickable(true);
                                Toast.makeText(FindPasswordActivity.this, "密码重置失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case R.id.bt_get_code:
                getcode.setEnabled(false);
                final BmobQuery<User> query = new BmobQuery<>();
                query.addWhereEqualTo("mobilePhoneNumber", phone.getText().toString());
                query.findObjects(this, new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> list) {
                        if (list.size() == 1) {
                            sendcode(phone.getText().toString().trim());
                        }else {
                            getcode.setEnabled(true);
                            Toast.makeText(FindPasswordActivity.this, "该用户尚未注册！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        getcode.setEnabled(true);
                        Toast.makeText(FindPasswordActivity.this, "获取失败\n请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String phonenumber;
        phonenumber = phone.getText().toString().trim();
        if(phonenumber.length() != 11 ){
            getcode.setEnabled(false);
        }else {
            getcode.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    public boolean isok(){
        boolean result = false;
        String s1 = phone.getText().toString().trim();
        String s2 = pwd.getText().toString().trim();
        String s3 = surepwd.getText().toString().trim();
        String s4 = code.getText().toString().trim();
        if(s1.equals("")){
            Toast.makeText(this,"手机号码不能为空！",Toast.LENGTH_SHORT).show();
        }else if(s2.equals("")){
            Toast.makeText(this,"新密码不能为空！",Toast.LENGTH_SHORT).show();
        }else if(!s2.equals(s3)){
            Toast.makeText(this,"新密码与确认密码不一致！",Toast.LENGTH_SHORT).show();
        }else if(s4.equals("")){
            Toast.makeText(this,"验证码不能为空！",Toast.LENGTH_SHORT).show();
        }else {
            result = true;
        }
        return result;
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
                    Toast.makeText(FindPasswordActivity.this,"计数器故障，请稍后再试！",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void onEventMainThread(TimeMsgEB msg) {
        if(msg.getI() == 59){
            phone.setEnabled(false);
        }
        if (msg.getI() == 0) {
            stopTimer();
            return;
        }
        if (msg.getI() < 10) {
            getcode.setText("0" + msg.getI() + " s");
        } else {
            getcode.setText(msg.getI() + " s");
        }
    }

    private void stopTimer() {
        getcode.setEnabled(true);
        phone.setEnabled(true);
        getcode.setText("再次获取");
    }

    public void sendcode(String phone){
        BmobSMS.requestSMSCode(this, phone, "密码找回", new RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, BmobException e) {

                if (e == null) {//验证码发送成功
                    Toast.makeText(FindPasswordActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    new Thread(new Timethrend()).start();
                } else {
                    getcode.setEnabled(true);
                    Toast.makeText(FindPasswordActivity.this, "验证码发送失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
