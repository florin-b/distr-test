package com.distributie.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.distributie.enums.EnumNetworkStatus;
import com.distributie.listeners.AsyncTaskListener;

public class JavaWSCall {

	private String methodName;
	private HashMap<String, String> params;
	private Context context;
	private AsyncTaskListener myListener;
	private ProgressDialog dialog;

	public JavaWSCall(String methodName, HashMap<String, String> params, AsyncTaskListener myListener, Context context) {
		this.myListener = myListener;
		this.methodName = methodName;
		this.params = params;
		this.context = context;

	}

	public JavaWSCall(Context context) {
		this.context = context;
	}

	public JavaWSCall(Context context, String methodName, HashMap<String, String> params) {
		this.context = context;
		this.methodName = methodName;
		this.params = params;
	}

	public JavaWSCall(Context context, AsyncTaskListener contextListener, String methodName, HashMap<String, String> params) {
		this.context = context;
		this.methodName = methodName;
		this.params = params;
	}

	public void getCallResults() {
		new WebServiceCall(this.myListener).execute();
	}

	private class WebServiceCall extends AsyncTask<Void, Void, String> {
		String errMessage = "";
		private AsyncTaskListener myListener;

		private WebServiceCall(AsyncTaskListener myListener) {
			super();
			this.myListener = myListener;
		}

		protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setMessage("Asteptati...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(Void... url) {
			String response = "";
			try {
				SoapObject request = new SoapObject("http://main", methodName);

				for (Entry<String, String> entry : params.entrySet()) {
					request.addProperty(entry.getKey(), entry.getValue());
				}

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE("http://10.1.0.58:8080/FlotaWS/services/FlotaWS?wsdl", 60000);

				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode("bflorin:bflorin".getBytes())));
				androidHttpTransport.call("http://main/" + methodName, envelope, null);
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

				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}

				if (!errMessage.equals("")) {
					Toast toast = Toast.makeText(context, errMessage, Toast.LENGTH_SHORT);
					toast.show();
				} else {
					myListener.onTaskComplete(methodName, result, EnumNetworkStatus.ON);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

}
