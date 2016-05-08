package com.hytc.nhytc.util;

import android.graphics.Color;

public class ColorUtils {
	// 用数字取fill颜色
	public static int getFillColor(int colorFlag) {
		if (colorFlag == 1) {
			//return Color.parseColor("#FF00FF");//95e987
			return Color.parseColor("#95e987");//95e987
		} else if (colorFlag == 2) {
			//return Color.parseColor("#912CEE");//ffb67e
			return Color.parseColor("#ffb67e");//ffb67e
		} else if (colorFlag == 3) {
			//return Color.parseColor("#8E8E38");//8cc7fe
			return Color.parseColor("#8cc7fe");//8cc7fe
		} else if (colorFlag == 4) {
			//return Color.parseColor("#8B4726");//7ba3eb
			return Color.parseColor("#7ba3eb");//7ba3eb
		} else if (colorFlag == 5) {
			//return Color.parseColor("#3A5FCD");//e3ade8
			return Color.parseColor("#e3ade8");//e3ade8
		} else if (colorFlag == 6) {
			//return Color.parseColor("#00CD00");//f9728b
			return Color.parseColor("#f9728b");//f9728b
		} else if (colorFlag == 7) {
			//return Color.parseColor("#00BFFF");//85e9cd
			return Color.parseColor("#85e9cd");//85e9cd
		} else if (colorFlag == 8) {
			//return Color.parseColor("#698B22");//f5a8cf
			return Color.parseColor("#f5a8cf");//f5a8cf
		} else if (colorFlag == 9) {
			//return Color.parseColor("#8B1C62");//a9e2a0
			return Color.parseColor("#a9e2a0");//a9e2a0
		} else if (colorFlag == 10) {
			//return Color.parseColor("#8968CD");//70cec7
			return Color.parseColor("#70cec7");//70cec7
		} else {
			return Color.parseColor("#EAEAEA");// 没有赋值,或赋值错误时,灰色
		}
	}

	// 用数字取边框颜色
	public static int getStrokeColor(int colorFlag) {
		if (colorFlag == 1) {
			return Color.parseColor("#5ca850");
		} else if (colorFlag == 2) {
			return Color.parseColor("#d39260");

		} else if (colorFlag == 3) {
			return Color.parseColor("#5e91c0");

		} else if (colorFlag == 4) {
			return Color.parseColor("#486db0");

		} else if (colorFlag == 5) {
			return Color.parseColor("#c789cd");

		} else if (colorFlag == 6) {
			return Color.parseColor("#c14b61");

		} else if (colorFlag == 7) {
			return Color.parseColor("#5dcaab");

		} else if (colorFlag == 8) {
			return Color.parseColor("#cd7aa4");

		} else if (colorFlag == 9) {
			return Color.parseColor("#75c269");

		} else if (colorFlag == 10) {
			return Color.parseColor("#42918b");

		} else {
			return Color.GRAY;// 没有赋值,或赋值错误时,灰色
		}
	}
}
