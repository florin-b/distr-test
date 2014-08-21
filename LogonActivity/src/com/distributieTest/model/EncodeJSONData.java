package com.distributieTest.model;

import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

public class EncodeJSONData {

	private HashMap<String, String> paramData;
	private Context context;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public EncodeJSONData(Context context, HashMap<String, String> paramData) {
		this.context = context;
		this.paramData = paramData;
	}

	public String encodeNewEventData() {
		String jsonData = "";

		if (paramData.size() > 0) {

			JSONObject tempObject = new JSONObject();

			for (Entry<String, String> entry : paramData.entrySet()) {

				try {
					tempObject.put(entry.getKey(), entry.getValue());
				} catch (JSONException e) {
					Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
				}

			}

			jsonData = tempObject.toString();

		}

		return jsonData;
	}

}
