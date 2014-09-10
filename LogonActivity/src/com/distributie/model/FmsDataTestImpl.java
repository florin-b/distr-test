package com.distributie.model;

import java.util.HashMap;

import android.content.Context;

import com.distributie.listeners.AsyncTaskListener;
import com.distributie.listeners.FmsTestListener;

public class FmsDataTestImpl implements FmsDataTest, AsyncTaskListener {

	private Context context;

	FmsTestListener listener;

	public FmsDataTestImpl(Context context) {
		this.context = context;
	}

	@Override
	public void getFmsData(String fmsMac) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("fmsMAC", fmsMac);

		AsyncTaskWSCall call = new AsyncTaskWSCall("getFmsTestData", params, (AsyncTaskListener) this, context);
		call.getCallResults();

	}

	@Override
	public void onTaskComplete(String methodName, String result) {
		if (listener != null) {
			listener.testComplete(result);
		}

	}

	public void setFmsTestListener(FmsTestListener listener) {
		this.listener = listener;
	}

}
