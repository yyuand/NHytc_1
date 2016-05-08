package com.hytc.nhytc.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.hytc.nhytc.R;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.tool.RYConnectService;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;


import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;


/**
 * Created by Administrator on 2015/8/22.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText name;
    private EditText password;
    private Button to_login;
    private TextView textView1;
    private TextView textView2;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initwidget();
        isNeedLogin();
    }


    public void isNeedLogin(){
        /**
         * 如果是一件登陆的用户，则直接进入主页面
         */
        if(BmobUser.getCurrentUser(this) != null){

            /**
             * 如果已经登录过了，则直接跳转到主页
             */
            Intent intent_service = new Intent();
            intent_service.setClass(this, RYConnectService.class);
            startService(intent_service);


            Intent intent_login = new Intent();
            intent_login.setClass(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(intent_login);
            LoginActivity.this.finish();
        }
    }
    public void login(){
        progressDialog.show();
        BmobUser.loginByAccount(this, name.getText().toString().trim(), password.getText().toString().trim(), new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (user != null) {
                    /**
                     * 如果已经登录过了，则直接跳转到主页
                     */
                    Intent intent_service = new Intent();
                    intent_service.setClass(LoginActivity.this, RYConnectService.class);
                    startService(intent_service);


                    Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    Intent intent_login = new Intent();
                    intent_login.setClass(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent_login);
                    progressDialog.dismiss();
                    LoginActivity.this.finish();
                    to_login.setClickable(true);
                } else {
                    to_login.setClickable(true);
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initwidget() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("登录中...");

        name = (EditText) this.findViewById(R.id.et_name_login);
        password = (EditText) this.findViewById(R.id.et_password_login);
        to_login = (Button) findViewById(R.id.bt_login);
        textView1 = (TextView) findViewById(R.id.tv_register);
        textView2 = (TextView) findViewById(R.id.tv_forget_pwd);

        to_login.setOnClickListener(this);
        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                if(isCanLogin()) {
                    to_login.setClickable(false);
                    login();
                }
                break;
            case R.id.tv_register:
                Intent intent_newlogin = new Intent();
                intent_newlogin.setClass(this, NewLoginActivity.class);
                startActivity(intent_newlogin);
            break;
            case R.id.tv_forget_pwd:
                Intent intent_findpwd = new Intent();
                intent_findpwd.setClass(this, FindPasswordActivity.class);
                startActivity(intent_findpwd);
            break;
        }
    }


    public Boolean isCanLogin(){
        String phone = name.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        Boolean isok = false;
        if("".equals(phone)){
            Toast.makeText(LoginActivity.this, "账号不能为空！", Toast.LENGTH_SHORT).show();
        }else if("".equals(pwd)){
            Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
        }else {
            isok = true;
        }
        return isok;
    }

}
