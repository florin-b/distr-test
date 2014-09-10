package com.distributie.model;

import java.util.HashMap;

import com.distributie.listeners.AsyncTaskListener;
import com.distributie.listeners.OperatiiBorderouriListener;

import android.content.Context;

public class OperatiiBorderouriDAOImpl implements OperatiiBorderouriDAO, AsyncTaskListener {

	private Context context;
	private OperatiiBorderouriListener event;

	public OperatiiBorderouriDAOImpl(Context context) {
		this.context = context;
	}

	@Override
	public void getDocEvents(String nrDoc, String tipEv) {

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("nrDoc", nrDoc);
		params.put("tipEv", tipEv);

		AsyncTaskWSCall call = new AsyncTaskWSCall("getDocEvents", params, (AsyncTaskListener) this, context);
		call.getCallResults();

	}

	@Override
	public void saveNewEventBorderou(HashMap<String, String> newEventData) {

		EncodeJSONData jsonEvLivrare = new EncodeJSONData(context, newEventData);
		String serializedData = jsonEvLivrare.encodeNewEventData();

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("serializedEvent", serializedData);

		AsyncTaskWSCall call = new AsyncTaskWSCall("saveNewEvent", params, (AsyncTaskListener) this, context);
		call.getCallResults();

	}

	@Override
	public void saveNewEventClient(HashMap<String, String> newEventData) {

		EncodeJSONData jsonEvLivrare = new EncodeJSONData(context, newEventData);
		String serializedData = jsonEvLivrare.encodeNewEventData();

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("serializedEvent", serializedData);

		AsyncTaskWSCall call = new AsyncTaskWSCall("saveNewEvent", params, (AsyncTaskListener) this, context);
		call.getCallResults();

	}

	@Override
	public void onTaskComplete(String methodName, String result) {
		if (event != null) {
			event.eventComplete(result, methodName);
		}

	}

	public void setEventListener(OperatiiBorderouriListener event) {
		this.event = event;
	}

}
