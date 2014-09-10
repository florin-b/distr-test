package com.distributie.model;

import java.util.HashMap;
import android.content.Context;
import com.distributie.listeners.AsyncTaskListener;
import com.distributie.listeners.BorderouriDAOListener;

public class BorderouriDAOImpl implements BorderouriDAO, AsyncTaskListener {

	private Context context;
	private BorderouriDAOListener borderouriEvents;

	public BorderouriDAOImpl(Context context) {
		this.context = context;
	}

	@Override
	public void getBorderouri(String codSofer, String tipOp, String interval) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("codSofer", codSofer);
		params.put("tip", tipOp);
		params.put("interal", interval);

		AsyncTaskWSCall call = new AsyncTaskWSCall("getBorderouri", params, (AsyncTaskListener) this, context);
		call.getCallResults();

	}

	@Override
	public void getFacturiBorderou(String nrBorderou, String tipBorderou) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nrBorderou", nrBorderou);
		params.put("tipBorderou", tipBorderou);

		AsyncTaskWSCall call = new AsyncTaskWSCall("getFacturiBorderou", params, (AsyncTaskListener) this, context);
		call.getCallResults();

	}

	@Override
	public void getArticoleBorderou(String nrBorderou, String codClient) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nrBorderou", nrBorderou);
		params.put("codClient", codClient);

		AsyncTaskWSCall call = new AsyncTaskWSCall("getArticoleBorderou", params, (AsyncTaskListener) this, context);
		call.getCallResults();

	}

	public void setBorderouEventListener(BorderouriDAOListener borderouriEvents) {
		this.borderouriEvents = borderouriEvents;
	}

	@Override
	public void onTaskComplete(String methodName, String result) {
		if (borderouriEvents != null) {
			borderouriEvents.loadComplete(result, methodName);
		}

	}

}
