package com.hytc.nhytc.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * 常用单位转换的辅助类
 * @author liulongling
 *
 */
public class DensityUtils
{
	private DensityUtils()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * dpתpx
	 * 
	 * @param context
	 * @param dpVal
	 * @return
	 */
	public static int dp2px(Context context, float dpVal)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, context.getResources().getDisplayMetrics());
	}

	/**
	 * spתpx
	 * 
	 * @param context
	 * @param spVal
	 * @return
	 */
	public static int sp2px(Context context, float spVal)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, context.getResources().getDisplayMetrics());
	}

	/**
	 * pxתdp
	 * 
	 * @param context
	 * @param pxVal
	 * @return
	 */
	public static float px2dp(Context context, float pxVal)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (pxVal / scale);
	}

	/**
	 * pxתsp
	 * 
	 * @param context
	 * @param pxVal
	 * @return
	 */
	public static float px2sp(Context context, float pxVal)
	{
		return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
	}

}
