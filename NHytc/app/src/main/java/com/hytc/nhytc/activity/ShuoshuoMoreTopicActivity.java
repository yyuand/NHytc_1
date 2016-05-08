package com.hytc.nhytc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hytc.nhytc.R;


/**
 * Created by Administrator on 2015/8/15.
 */
public class ShuoshuoMoreTopicActivity extends Activity implements View.OnClickListener {
    /**
     * title
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;

    /**
     *
     */
    private RelativeLayout relativeLayout1;
    private RelativeLayout relativeLayout2;
    private RelativeLayout relativeLayout3;
    private RelativeLayout relativeLayout4;
    private RelativeLayout relativeLayout5;
    private RelativeLayout relativeLayout6;
    private RelativeLayout relativeLayout7;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shuoshuo_moretopic);
        inittitle();
        initWidget();

    }

    private void initWidget() {
        relativeLayout1 = (RelativeLayout) this.findViewById(R.id.rl_more_topicitem1);
        relativeLayout2 = (RelativeLayout) this.findViewById(R.id.rl_more_topicitem2);
        relativeLayout3 = (RelativeLayout) this.findViewById(R.id.rl_more_topicitem3);
        relativeLayout4 = (RelativeLayout) this.findViewById(R.id.rl_more_topicitem4);
        relativeLayout5 = (RelativeLayout) this.findViewById(R.id.rl_more_topicitem5);
        relativeLayout6 = (RelativeLayout) this.findViewById(R.id.rl_more_topicitem6);
        relativeLayout7 = (RelativeLayout) this.findViewById(R.id.rl_more_topicitem7);

        relativeLayout1.setOnClickListener(this);
        relativeLayout2.setOnClickListener(this);
        relativeLayout3.setOnClickListener(this);
        relativeLayout4.setOnClickListener(this);
        relativeLayout5.setOnClickListener(this);
        relativeLayout6.setOnClickListener(this);
        relativeLayout7.setOnClickListener(this);
    }


    private void inittitle() {
        /**
         *
         */
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        titlename.setText("更多话题");
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

    private void goToActivity(int i){
        Intent intent = new Intent();
        intent.setClass(this, ShuoshuoTopicActivity.class);
        intent.putExtra("shuoshuo",i);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_more_topicitem1:
                goToActivity(0);
                break;
            case R.id.rl_more_topicitem2:
                goToActivity(1);
            break;
            case R.id.rl_more_topicitem3:
                goToActivity(2);
            break;
            case R.id.rl_more_topicitem4:
                goToActivity(3);
            break;
            case R.id.rl_more_topicitem5:
                goToActivity(4);
            break;
            case R.id.rl_more_topicitem6:
                goToActivity(5);
            break;
            case R.id.rl_more_topicitem7:
                goToActivity(6);
            break;
        }
    }
}
