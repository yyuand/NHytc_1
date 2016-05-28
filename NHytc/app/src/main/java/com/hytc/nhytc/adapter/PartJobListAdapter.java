package com.hytc.nhytc.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hytc.nhytc.R;
import com.hytc.nhytc.domain.PartJob;
import com.hytc.nhytc.tool.ShowTimeTools;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 向PartJob里填充partjobItem
 * Created by yyuand on 2016/5/26.
 */
public class PartJobListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;//LayoutInflater是用来找layout/下的xml布局文件，并且实例化
    private List<PartJob> items;

    /**
     * 适配器的构造函数
     * @param activity
     * @param items
     */
    public PartJobListAdapter(Activity activity,List<PartJob> items) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.items = items;
    }

    /**
     * 得到要添加的item的个数
     * 此处的i都表示为position
     * @return
     */
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;//listview滚动的时候快速设置值，不必每次都重新创建很多对象
        if (view == null) {
            view = inflater.inflate(R.layout.act_partjob_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);//View中的setTag(Onbect)表示给View添加一个格外的数据
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvJobnameItem.setText(items.get(i).getJob_Name());
        viewHolder.tvJobplaceItem.setText(items.get(i).getPlace());
        viewHolder.tvJobsalaryItem.setText(items.get(i).getSalary());
        //getCreatedAt()获取创建的时间
        viewHolder.tvTimePartjobItem.setText(ShowTimeTools.getShowTime(items.get(i).getCreatedAt()));
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.tv_jobname_item)
        TextView tvJobnameItem;
        @Bind(R.id.tv_jobplace_item)
        TextView tvJobplaceItem;
        @Bind(R.id.tv_jobsalary_item)
        TextView tvJobsalaryItem;
        @Bind(R.id.tv_time_partjob_item)
        TextView tvTimePartjobItem;
        @Bind(R.id.ll_jobdis_item)
        LinearLayout llJobdisItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 添加数据
     * @param items
     */
    public void addData(List<PartJob> items){
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 刷新数据
     * @param items
     */
    public void refreshData(List<PartJob> items){
        this.items.clear();//清除之前的数据，以便重新获取时间
        this.items.addAll(items);
        notifyDataSetChanged();
    }
}
