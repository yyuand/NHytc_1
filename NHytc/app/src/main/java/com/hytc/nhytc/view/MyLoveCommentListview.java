package com.hytc.nhytc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Administrator on 2015/9/20.
 */
public class MyLoveCommentListview extends ListView {


    public MyLoveCommentListview(Context context) {
        super(context);
    }

    public MyLoveCommentListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLoveCommentListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
