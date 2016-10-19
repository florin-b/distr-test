package com.distributie.model;

import com.distributie.enums.EnumOperatiiLogon;

public interface LogonListener {
	public void logonComplete(EnumOperatiiLogon methodName, String result);
}
