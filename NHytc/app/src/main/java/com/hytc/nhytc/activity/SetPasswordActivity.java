package com.hytc.nhytc.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hytc.nhytc.R;
import com.hytc.nhytc.tool.MyRYTokenManager;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/3/1.
 */
public class SetPasswordActivity extends Activity {
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;


    private EditText oldpwd;
    private EditText newpwd;
    private EditText surenewpwd;
    private Button sure;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        initTitle();
        initwidget();
    }

    private void initwidget() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("密码修改中，请稍等...");

        oldpwd = (EditText) findViewById(R.id.old_pwd);
        newpwd = (EditText) findViewById(R.id.new_ped);
        surenewpwd = (EditText) findViewById(R.id.sure_new_ped);
        sure = (Button) findViewById(R.id.bt_set_pwd);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = oldpwd.getText().toString().trim();
                String s1 = newpwd.getText().toString().trim();
                String s2 = surenewpwd.getText().toString().trim();
                if (isOk(s, s1, s2)) {
                    sure.setClickable(false);
                    setPwd(s, s1);
                }
            }
        });
    }

    public void setPwd(String oldpwd,String newpwd){
        progressDialog.show();
        BmobUser.updateCurrentUserPassword(this, oldpwd, newpwd, new UpdateListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                progressDialog.dismiss();
                BmobUser.logOut(SetPasswordActivity.this);
                MyRYTokenManager.saveToken(SetPasswordActivity.this, "");

                Intent intent = new Intent();
                intent.setClass(SetPasswordActivity.this, LoginActivity.class);
                SetPasswordActivity.this.startActivity(intent);
                sure.setClickable(true);
                SetPasswordActivity.this.finish();

                Toast.makeText(SetPasswordActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                sure.setClickable(true);
                progressDialog.dismiss();
                // TODO Auto-generated method stub
                Toast.makeText(SetPasswordActivity.this, "密码修改失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        titlename.setText("找回密码");
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


    private Boolean isOk(String s,String s1,String s2){
        if(s.equals("")) {
            Toast.makeText(this, "原密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(s1.equals("")) {
            Toast.makeText(this,"新密码不能为空！",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!s1.equals(s2)) {
            Toast.makeText(this,"新密码与确认密码不相同！",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }


}
