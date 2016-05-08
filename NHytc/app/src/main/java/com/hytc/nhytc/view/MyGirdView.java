package com.hytc.nhytc.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2015/7/9.
 * 重写GirdView的onMeasure方法，用于避免listview嵌套滑动失效
 * 其中onMeasure函数决定了组件显示的高度与宽度；
 * makeMeasureSpec函数中第一个函数决定布局空间的大小，第二个参数是布局模式
 * MeasureSpec.AT_MOST的意思就是子控件需要多大的控件就扩展到多大的空间
 */
public class MyGirdView extends GridView {

    public MyGirdView(Context context){
        super(context);
    }

    public MyGirdView(Context context,
                      AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
