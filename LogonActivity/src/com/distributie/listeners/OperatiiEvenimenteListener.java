package com.distributie.listeners;

import com.distributie.enums.EnumOperatiiEvenimente;

public interface OperatiiEvenimenteListener {
	public void opEventComplete(String result, EnumOperatiiEvenimente methodName);
}
