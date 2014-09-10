package com.distributie.model;

import java.util.HashMap;

import android.content.Context;

import com.distributie.listeners.AsyncTaskListener;

public class LogonImpl implements Logon, AsyncTaskListener {

	private Context context;
	LogonListener listener;

	public LogonImpl(Context context) {
		this.context = context;
	}

	@Override
	public void performLogon(String user, String pass) {

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("userId", user);
		params.put("userPass", pass);
		params.put("ipAdr", "-1");

		AsyncTaskWSCall call = new AsyncTaskWSCall("userLogon", params, (AsyncTaskListener) this, context);
		call.getCallResults();

	}

	@Override
	public void onTaskComplete(String methodName, String result) {
		if (listener != null) {
			listener.logonComplete(result);
		}

	}

	public void setLogonListener(LogonListener listener) {
		this.listener = listener;
	}

}
