package com.distributie.listeners;

import com.distributie.enums.EnumNetworkStatus;

public interface AsyncTaskListener {
	public void onTaskComplete(String methodName, String result, EnumNetworkStatus networkStatus);
}


