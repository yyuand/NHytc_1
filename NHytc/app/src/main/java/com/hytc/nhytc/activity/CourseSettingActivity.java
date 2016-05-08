package com.hytc.nhytc.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hytc.nhytc.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/4/30.
 */
public class CourseSettingActivity extends Activity {
    @Bind(R.id.sp_week_setting)
    Spinner spWeekSetting;
    @Bind(R.id.rl_get_course)
    RelativeLayout rlGetCourse;

    private SharedPreferences sp;
    private int week;
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_setting);
        ButterKnife.bind(this);
        initTitle();
    }

    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        sp = getSharedPreferences("first",Activity.MODE_PRIVATE);
        week = sp.getInt("ourweek", 1);
        spWeekSetting.setSelection(week - 1);
        spWeekSetting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                savaData(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        tvinfo.setVisibility(View.GONE);
        titlename.setText("课表设置");
        ivinfo.setVisibility(View.GONE);
        ivmore.setVisibility(View.GONE);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void savaData(int i) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("ourweek",i);
        editor.apply();
    }

    @OnClick(R.id.rl_get_course)
    public void onClick() {
        Intent intent = new Intent(this, GetCourseActivity.class);
        startActivity(intent);
        finish();
    }
}
