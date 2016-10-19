package com.distributie.model;

import java.util.HashMap;

import com.distributie.beans.BeanRouteBounds;
import com.distributie.listeners.OperatiiAdresaListener;

public interface OperatiiAdresa {
	void getRouteBounds(HashMap<String, String> params);

	void setOperatiiAdresaListener(OperatiiAdresaListener listener);

	BeanRouteBounds deserializeRouteBounds(String result);
}
