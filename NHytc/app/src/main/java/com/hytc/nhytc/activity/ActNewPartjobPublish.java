package com.hytc.nhytc.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hytc.nhytc.R;
import com.hytc.nhytc.domain.PartJob;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by DYY on 2016/5/11.
 */


/**
 * 利用插件绑定控件
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
    @Bind(R.id.et_partjob_time)
    EditText etPartjobTime;

    /**
     * title
     */
    private ImageView ivback;
    private TextView titlename;
    private ImageView ivinfo;
    private TextView tvinfo;
    private ImageView ivmore;
    private ProgressDialog dialog;

    /**
     * 发布界面的开始生存周期
     * @param savedInstanceState
     */
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
        dialog = new ProgressDialog(this);
        dialog.setMessage("数据上传中...");

        //标题栏的设置
        titlename.setText("兼职发布");
        ivinfo.setVisibility(View.GONE);
        ivmore.setVisibility(View.GONE);
        tvinfo.setText("发布");
        tvinfo.setVisibility(View.VISIBLE);
        tvinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPrepare()){
                    tvinfo.setClickable(false);
                    dialog.show();
                    getSort();
                }
            }
        });

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 判断各项是否为空
     * @return
     */
    private Boolean isPrepare() {
        Boolean result = false;
        if ("".equals(etPartjobName.getText().toString())) {
            Toast.makeText(this, "兼职名称不能为空哟~", Toast.LENGTH_SHORT).show();
        }
        else if ("".equals(etPartjobTime.getText().toString())) {
            Toast.makeText(this, "兼职时间不能为空哟~", Toast.LENGTH_SHORT).show();
        }else if ("".equals(etPartjobPlace.getText().toString())) {
            Toast.makeText(this, "兼职地点不能为空哟~", Toast.LENGTH_SHORT).show();
        } else if ("".equals(etPartjobRequire.getText().toString())) {
            Toast.makeText(this, "兼职要求不能为空哟~", Toast.LENGTH_SHORT).show();
        } else if ("".equals(etPartjobSalary.getText().toString())) {
            Toast.makeText(this, "薪酬不能为空哟~", Toast.LENGTH_SHORT).show();
        } else if ("".equals(etPartjobDescribe.getText().toString())) {
            Toast.makeText(this, "职位简介不能为空哟~", Toast.LENGTH_SHORT).show();
        } else if ("".equals(etPartjobTel.getText().toString())) {
            Toast.makeText(this, "联系方式不能为空哟~", Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }

        return result;
    }

    /**
     * 获取用于排序的值，默认内部发布的兼职排在顶部
     */
    public void getSort(){
        final BmobQuery<PartJob> query = new BmobQuery<PartJob>();
        query.addWhereLessThan("sort",5000);//小于5000的为普通用户的发布，5000+是内部发布
        query.setLimit(1);//返回1条数据
        query.order("-sort");
        //执行查询方法
        query.findObjects(this, new FindListener<PartJob>() {
            @Override
            public void onSuccess(List<PartJob> list) {
                Double sort = list.get(0).getSort();
                upLoadData(sort);
            }

            @Override
            public void onError(int i, String s) {
                dialog.dismiss();
                tvinfo.setClickable(true);
                Toast.makeText(ActNewPartjobPublish.this, "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 上传数据
     * @param sort
     */
    public void upLoadData(Double sort){
        PartJob job = new PartJob();
        job.setJob_Name(etPartjobName.getText().toString());
        job.setTime(etPartjobTime.getText().toString());
        job.setPlace(etPartjobPlace.getText().toString());
        job.setRequire(etPartjobRequire.getText().toString());
        job.setSalary(etPartjobSalary.getText().toString());
        job.setDescribe(etPartjobDescribe.getText().toString());
        job.setTel(etPartjobTel.getText().toString());
        job.setSort(sort + 0.001);
        job.save(ActNewPartjobPublish.this, new SaveListener() {
            @Override
            public void onSuccess() {
                dialog.dismiss();
                tvinfo.setClickable(true);
                Toast.makeText(ActNewPartjobPublish.this, "上传成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                dialog.dismiss();
                tvinfo.setClickable(true);
                Toast.makeText(ActNewPartjobPublish.this, "上传失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
