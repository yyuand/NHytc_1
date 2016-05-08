package com.hytc.nhytc.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hytc.nhytc.R;
import com.hytc.nhytc.dbDAO.CourseDao;
import com.hytc.nhytc.domain.CustomDate;
import com.hytc.nhytc.fragment.CourseTable;
import com.hytc.nhytc.fragment.NoCourseFragment;
import com.hytc.nhytc.tool.CourseTools;
import com.hytc.nhytc.util.DateUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/20.
 */
public class MainCourseActivity extends FragmentActivity{
    protected static final String TAG = "MainActivity";

    /**月视图 or 周视图*/
    @Bind(R.id.fl_view)
    FrameLayout mFrameLayout;



    /**
     * title
     */
    private ImageView ivback;
    private TextView tvinfo;
    private TextView title;
    private Spinner spinner;

    private CourseDao dao;
    private CourseTable table;
    private NoCourseFragment nocourse;
    private SharedPreferences sp;
    private int our_week;
    private int save_sys_week;
    private int sys_week;

    private boolean boo = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_main);
        ButterKnife.bind(this);
        initTitle();
        our_week = our_week == 0?1:our_week;
        dao = new CourseDao(this);

        table = new CourseTable();
        Bundle bundle = new Bundle();
        bundle.putInt("week", our_week);
        table.setArguments(bundle);
        table.setmContext(this);
        nocourse = new NoCourseFragment();

        //判断本地课表数据中是否有课表数据
        if(dao.isExistData()){
            //this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_view, table).commit();
            spinner.setVisibility(View.VISIBLE);
        }else {
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_view, nocourse).commit();
            title.setVisibility(View.VISIBLE);
            title.setText("我的课表");
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (boo) {
                    table = new CourseTable();
                    Bundle bundle = new Bundle();
                    bundle.putInt("week", position + 1);
                    table.setArguments(bundle);
                    table.setmContext(MainCourseActivity.this);


                    MainCourseActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_view, table).commit();
                } else {
                    boo = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        title = (TextView) findViewById(R.id.tv_title_bar);
        spinner = (Spinner) findViewById(R.id.sp_week);

        sp = getSharedPreferences("first", Activity.MODE_PRIVATE);
        //本地存储的学期周数
        our_week = sp.getInt("ourweek", 0);
        //本地存储的年周数
        save_sys_week = sp.getInt("sysweek", 1);
        //根据系统时间获取的年周数
        sys_week = CourseTools.getCurrentWeek();

        doWithWeek();
        Log.e("maincourse", our_week + "  " + save_sys_week + "  " + sys_week);
        tvinfo.setText("设置");
        tvinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainCourseActivity.this, CourseSettingActivity.class);
                startActivity(intent);
            }
        });
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        our_week = sp.getInt("ourweek", 1);
        spinner.setSelection(our_week - 1);
        if(dao == null){
            dao = new CourseDao(this);
        }


        table = new CourseTable();
        Bundle bundle = new Bundle();
        bundle.putInt("week", our_week);
        table.setArguments(bundle);
        table.setmContext(this);


        if(nocourse == null){
            nocourse = new NoCourseFragment();
        }

        if(dao.isExistData()){
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_view, table).commit();
            spinner.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
        }else {
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_view, nocourse).commit();
            title.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            title.setText("我的课表");
        }
    }

    public void doWithWeek(){
        if(sys_week != save_sys_week){
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("sysweek",sys_week);
            if(our_week != 25) {
                editor.putInt("ourweek", our_week + 1);
            }
            editor.apply();
        }
        Log.e("maincourse",our_week+"===");
        spinner.setSelection(our_week - 1);
    }


}






//设置选中日期
        /*if(mClickDate != null){
            CustomDate mShowDate = mClickDate;
            int curMonthDays = DateUtil.getMonthDays(mShowDate.year, mShowDate.month);
            //获取周日
            if (mShowDate.day - mShowDate.week+7 > curMonthDays){
                if (mShowDate.month == 12) {
                    mShowDate.month = 1;
                    mShowDate.year += 1;
                } else {
                    mShowDate.month += 1;
                }
                mShowDate.day = (mShowDate.day- mShowDate.week-1)+7-curMonthDays;
            }else{
                mShowDate.day = mShowDate.day - mShowDate.week+7;
            }
            table.setShowDate(mShowDate);
        }*/
