package com.hytc.nhytc.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hytc.nhytc.R;
import com.hytc.nhytc.domain.ShowLove;
import com.hytc.nhytc.domain.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by Administrator on 2015/8/17.
 */
public class ShowLovePublishActivity extends Activity implements View.OnClickListener {
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;
    private RelativeLayout title_rl;

    private EditText etname;
    private EditText etcontant;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showlovepublish);
        initTitle();
    }
    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);
        title_rl = (RelativeLayout) this.findViewById(R.id.rl_publish_title_bar);

        etname = (EditText) this.findViewById(R.id.et_name_publish_love);
        etcontant = (EditText) this.findViewById(R.id.et_contant_love_publish);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("发表中...");

        ivback.setOnClickListener(this);
        title_rl.setOnClickListener(this);

        titlename.setText("我要表白");
        ivinfo.setVisibility(View.GONE);
        tvinfo.setVisibility(View.VISIBLE);
        ivmore.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_publish_title_bar:
                if(isempty()){
                    title_rl.setClickable(false);
                    publishMyLove();
                }
                break;
            case R.id.iv_back_titlebar:
                finish();
                break;

        }
    }


    public boolean isempty(){
        if("".equals(etcontant.getText().toString())){
            Toast.makeText(this,"表白啥也不说是不行滴~",Toast.LENGTH_SHORT).show();
            return false;
        }else if("".equals(etname.getText().toString())){
            Toast.makeText(this,"亲，你在向谁表白嘞？",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }

    public void publishMyLove(){
        ShowLove showLove = new ShowLove();
        showLove.setShowLoveName(etname.getText().toString());
        showLove.setContent(etcontant.getText().toString());
        showLove.setApproveCount(0);
        showLove.setCommentCount(0);
        showLove.setAuthor(BmobUser.getCurrentUser(this, User.class));
        showLove.setIsApprove(false);
        progressDialog.show();
        showLove.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                Toast.makeText(ShowLovePublishActivity.this,"发表成功！",Toast.LENGTH_SHORT).show();
                title_rl.setClickable(true);
                ShowLovePublishActivity.this.finish();
            }

            @Override
            public void onFailure(int i, String s) {
                title_rl.setClickable(true);
                progressDialog.dismiss();
                Toast.makeText(ShowLovePublishActivity.this,"咦？发表失败了，网络好像出了点问题！",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
