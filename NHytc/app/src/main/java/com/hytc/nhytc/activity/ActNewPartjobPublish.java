package com.hytc.nhytc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hytc.nhytc.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by DYY on 2016/5/11.
 */
public class ActNewPartjobPublish extends Activity {
	@Bind(R.id.et_partjob_name)
	EditText etPartjobName;
	@Bind(R.id.et_partjob_place)
	EditText etPartjobPlace;
	@Bind(R.id.et_partjob_require)
	EditText etPartjobRequire;
	@Bind(R.id.et_partjob_salary)
	EditText etPartjobSalary;
	@Bind(R.id.et_partjob_describe)
	EditText etPartjobDescribe;
	@Bind(R.id.et_partjob_tel)
	EditText etPartjobTel;

	/**
	 * title
	 */
	private ImageView ivback;
	private TextView titlename;
	private ImageView ivinfo;
	private TextView tvinfo;
	private ImageView ivmore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_new_partjob_publish);
		ButterKnife.bind(this);
		inittitle();

	}

	private void inittitle() {
		ivback = (ImageView) this.findViewById(R.id.iv_back_titlebar);
		titlename = (TextView) this.findViewById(R.id.tv_title_bar);
		ivinfo = (ImageView) this.findViewById(R.id.iv_mark_titlebar);
		tvinfo = (TextView) this.findViewById(R.id.tv_mark_titlebar);
		ivmore = (ImageView) this.findViewById(R.id.iv_add_titlebar);

		titlename.setText("兼职发布");
		ivinfo.setVisibility(View.GONE);
		ivmore.setVisibility(View.GONE);
		tvinfo.setText("发布");
		tvinfo.setVisibility(View.VISIBLE);
		tvinfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		ivback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
