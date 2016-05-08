package com.hytc.nhytc.util;

public interface Callback {
	void onBefore();

	boolean onRun();

	void onAfter(boolean b);
}
