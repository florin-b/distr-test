package com.distributie.listeners;

import com.distributie.enums.EnumOperatiiAdresa;

public interface OperatiiAdresaListener {
	void opAdresaComplete(EnumOperatiiAdresa methodName, String result);
}
