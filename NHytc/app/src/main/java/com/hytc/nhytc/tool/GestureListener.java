package com.hytc.nhytc.tool;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * 实现监听左右滑动的事件，哪个view需要的时候直接setOnTouchListener就可以用了
 *
 */
public class GestureListener extends SimpleOnGestureListener{
	private static final String TAG = "GestureListener";
	private final static int VERTICALMINISTANCE = 200 ; //表示向左滑动的最小距离
	private final static int MINVELOCITY = 10 ;  //表示向左滑动的最小的加速度
	private GestureDetector gestureDetector;

	public GestureListener(Context context) {
		super();
		gestureDetector = new GestureDetector(context, this);
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		/*// 根据velocityX（x方向上的加速度），velocityY（y方向上的加速度）
		// 根据两个速度的大小比较可以判断出手势是左右侧滑还是上下滑动
		if (Math.abs(velocityX) > Math.abs(velocityY) ) {
			//表示
		}*/

		//大于设定的最小滑动距离并且在水平/竖直方向速度绝对值大于设定的最小速度，则执行相应方法（表示向左滑动）
		if(e1.getX()-e2.getX() > VERTICALMINISTANCE && Math.abs(velocityX) > MINVELOCITY){
			//左滑一系列的操作  
			left();
		}

		//表示右划
		if (e2.getX()-e1.getX() > VERTICALMINISTANCE && Math.abs(velocityX) > MINVELOCITY) {
			//右滑一系列的操作    
			right();
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 向左滑的时候调用的方法，子类应该重写
	 * @return
	 */
	public boolean left() {
		return false;
	}

	/**
	 * 向右滑的时候调用的方法，子类应该重写
	 * @return
	 */
	public boolean right() {
		return false;
	}

	public GestureDetector getGestureDetector() {
		return gestureDetector;
	}

	public void setGestureDetector(GestureDetector gestureDetector) {
		this.gestureDetector = gestureDetector;
	}
	
}
