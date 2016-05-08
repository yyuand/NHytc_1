package com.hytc.nhytc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hytc.nhytc.R;
import com.hytc.nhytc.adapter.PhoneAdapter;


/**
 * Created by Administrator on 2015/8/17.
 */
public class PhoneActivity extends Activity {
    /**
     * title 控件声明
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;



    private ExpandableListView listView;
    private PhoneAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phont);
        initTitle();
        listView = (ExpandableListView) this.findViewById(R.id.eplist_activity_phone);
        listView.setGroupIndicator(null);
        adapter = new PhoneAdapter(this);
        listView.setDividerHeight(0);
        listView.setAdapter(adapter);

    }

    private void initTitle() {
        ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
        titlename = (TextView) this.findViewById(R.id.tv_title_bar);
        ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
        tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
        ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

        titlename.setText("部门电话");
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

}
