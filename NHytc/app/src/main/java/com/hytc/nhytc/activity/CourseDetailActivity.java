package com.hytc.nhytc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hytc.nhytc.R;
import com.hytc.nhytc.dbDAO.CourseDao;
import com.hytc.nhytc.domain.Lesson;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/1.
 */
public class CourseDetailActivity extends Activity {
    @Bind(R.id.tv_course_name)
    TextView tvCourseName;
    @Bind(R.id.tv_course_teacher)
    TextView tvCourseTeacher;
    @Bind(R.id.tv_course_room)
    TextView tvCourseRoom;
    @Bind(R.id.tv_course_count)
    TextView tvCourseCount;
    @Bind(R.id.tv_course_week)
    TextView tvCourseWeek;
    @Bind(R.id.tv_dan_shuang)
    TextView tvDSH;
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;

    private Lesson lesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        ButterKnife.bind(this);
        initTitle();
        initwidget();
    }

    private void initwidget() {
        lesson = (Lesson) getIntent().getSerializableExtra("courseid");
        tvCourseName.setText(lesson.getName());
        tvCourseTeacher.setText(lesson.getTeacher());
        tvCourseRoom.setText(lesson.getAdddress());
        StringBuilder count = new StringBuilder();
        count.append("周").append(lesson.getWeek()).append("第").append(lesson.getStart_course())
                .append("-").append(lesson.getEnd_course()).append("节课");
        tvCourseCount.setText(count.toString());
        StringBuilder week = new StringBuilder();
        if(lesson.getBo_we()){
            for(int[] i : lesson.getWeeks()){
                week.append("第").append(i[0]).append("-").append(i[1]).append("周").append("\n");
            }
        }else {
            week.append("第").append(lesson.getStart_week()).append("-").append(lesson.getEnd_week()).append("周");
        }
        tvCourseWeek.setText(week);
        switch (lesson.getIsdsh()){
            case 1:
                tvDSH.setText("（单周）");
                break;
            case 2:
                tvDSH.setText("（双周）");
                break;
            default:
                tvDSH.setText("");
                break;
        }
    }


    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        tvinfo.setVisibility(View.GONE);
        titlename.setText("课程详情");
        ivinfo.setVisibility(View.GONE);
        ivmore.setVisibility(View.GONE);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
