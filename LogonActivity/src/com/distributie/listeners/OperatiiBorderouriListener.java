package com.distributie.listeners;

import com.distributie.enums.EnumNetworkStatus;
import com.distributie.enums.EnumOperatiiEvenimente;

public interface OperatiiBorderouriListener {
	public void eventComplete(String result, EnumOperatiiEvenimente methodName, EnumNetworkStatus networkStatus);
	
}
