package com.distributie.model;

import java.util.HashMap;
import android.content.Context;

import com.distributie.enums.EnumNetworkStatus;
import com.distributie.enums.EnumOperatiiLogon;
import com.distributie.listeners.AsyncTaskListener;

public class LogonImpl implements Logon, AsyncTaskListener {

	private Context context;
	LogonListener listener;
	EnumOperatiiLogon numeComanda;
	HashMap<String, String> params;

	public LogonImpl(Context context) {
		this.context = context;
	}

	@Override
	public void performLogon(String user, String pass) {
		params = new HashMap<String, String>();
		params.put("userId", user);
		params.put("userPass", pass);
		params.put("ipAdr", "-1");
		numeComanda = EnumOperatiiLogon.USER_LOGON;
		performOperation();

	}

	public void getCodSofer(String codTableta) {
		params = new HashMap<String, String>();
		params.put("codTableta", codTableta);
		numeComanda = EnumOperatiiLogon.GET_COD_SOFER;
		performOperation();
	}

	private void performOperation() {
		AsyncTaskWSCall call = new AsyncTaskWSCall(numeComanda.getComanda(), params, (AsyncTaskListener) this, context);
		call.getCallResults();
	}

	public void onTaskComplete(String methodName, String result, EnumNetworkStatus networkStatus) {
		if (listener != null) {
			listener.logonComplete(numeComanda, result);
		}

	}

	public void setLogonListener(LogonListener listener) {
		this.listener = listener;
	}

}
