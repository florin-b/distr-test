package com.distributieTest.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.distributieTest.listeners.AsyncTaskListener;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AsyncTaskWSCall {

	private String methodName;
	private HashMap<String, String> params;
	private Context context;

	public AsyncTaskWSCall(Context context) {
		this.context = context;
	}

	public AsyncTaskWSCall(Context context, String methodName, HashMap<String, String> params) {
		this.context = context;
		this.methodName = methodName;
		this.params = params;
	}

	public void getCallResults() {
		new WebServiceCall(this.context).execute();
	}

	private class WebServiceCall extends AsyncTask<Void, Void, String> {
		String errMessage = "";
		private AsyncTaskListener listener;

		private WebServiceCall(Context context) {
			super();
			this.listener = (AsyncTaskListener) context;
		}

		@Override
		protected String doInBackground(Void... url) {
			String response = "";
			try {
				SoapObject request = new SoapObject(ConnectionStrings.getInstance().getNamespace(), methodName);

				for (Entry<String, String> entry : params.entrySet()) {
					request.addProperty(entry.getKey(), entry.getValue());
				}

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(ConnectionStrings.getInstance().getUrl(),
						60000);

				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic "
						+ org.kobjects.base64.Base64.encode("bflorin:bflorin".getBytes())));
				androidHttpTransport.call(ConnectionStrings.getInstance().getNamespace() + methodName, envelope,
						headerList);
				Object result = envelope.getResponse();
				response = result.toString();
			} catch (Exception e) {
				errMessage = e.getMessage();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {

			try {

				if (!errMessage.equals("")) {
					Toast toast = Toast.makeText(context, errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					listener.onTaskComplete(methodName, result);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

}
