package com.distributie.model;

import java.io.IOException;
import java.net.ConnectException;
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

import com.distributie.beans.EvenimentNou;
import com.distributie.connectors.ConnectionStrings;
import com.distributie.enums.EnumNetworkStatus;
import com.distributie.listeners.AsyncTaskListener;

public class AsyncTaskWSCall {

	private String methodName;
	private HashMap<String, String> params;
	private Context context;
	private AsyncTaskListener contextListener;
	private AsyncTaskListener myListener;
	private ProgressDialog dialog;
	private EvenimentNou eveniment;

	public AsyncTaskWSCall(String methodName, HashMap<String, String> params, AsyncTaskListener myListener, Context context) {
		this.myListener = myListener;
		this.methodName = methodName;
		this.params = params;
		this.context = context;

	}

	public AsyncTaskWSCall() {

	}

	public AsyncTaskWSCall(Context context) {
		this.context = context;
	}

	public AsyncTaskWSCall(Context context, String methodName, HashMap<String, String> params) {
		this.context = context;
		this.methodName = methodName;
		this.params = params;
	}

	public void setEveniment(EvenimentNou eveniment) {
		this.eveniment = eveniment;
	}

	public AsyncTaskWSCall(Context context, AsyncTaskListener contextListener, String methodName, HashMap<String, String> params) {
		this.context = context;
		this.methodName = methodName;
		this.params = params;
		this.contextListener = contextListener;
	}

	public void getCallResults() {
		new WebServiceCall(this.myListener).execute();
	}

	private class WebServiceCall extends AsyncTask<Void, Void, String> {
		String errMessage = "";
		Exception exception = null;
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

				SoapObject request = new SoapObject(ConnectionStrings.getInstance().getNamespace(), methodName);

				for (Entry<String, String> entry : params.entrySet()) {
					request.addProperty(entry.getKey(), entry.getValue());
				}

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(ConnectionStrings.getInstance().getUrl(), 60000);

				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode("bflorin:bflorin".getBytes())));
				androidHttpTransport.call(ConnectionStrings.getInstance().getNamespace() + methodName, envelope, headerList);
				Object result = envelope.getResponse();
				response = result.toString();

			} catch (ConnectException con) {
				errMessage = con.toString();
				exception = con;

			} catch (Exception e) {
				exception = e;
				errMessage = e.toString();
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

				EnumNetworkStatus netStat = EnumNetworkStatus.ON;
				if (exception instanceof ConnectException) {
					handleTimeoutConnection();
					netStat = EnumNetworkStatus.OFF;
				}

				if (!errMessage.equals("") && !(exception instanceof ConnectException)) {
					Toast toast = Toast.makeText(context, errMessage, Toast.LENGTH_SHORT);
					toast.show();

				} else {

					if (exception == null)
						clearStoredObjects();

					myListener.onTaskComplete(methodName, result, netStat);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
		}

	}

	private void handleTimeoutConnection() {

		LocalStorage storage = new LocalStorage(context);
		try {
			storage.serObject(eveniment);
			myListener.onTaskComplete(methodName, "", EnumNetworkStatus.OFF);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void clearStoredObjects() {
		LocalStorage storage = new LocalStorage(context);
		storage.deleteAllObjects();
	}

	public void getCallResults2() {
		new WebServiceCall2(context, contextListener).execute();
	}

	public class WebServiceCall2 extends AsyncTask<Void, Void, String> {
		String errMessage = "";
		Context mContext;

		private AsyncTaskListener listener;

		private WebServiceCall2(Context context, AsyncTaskListener contextListener) {
			super();
			this.mContext = context;
			this.listener = contextListener;
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
				HttpTransportSE androidHttpTransport = new HttpTransportSE(ConnectionStrings.getInstance().getUrl(), 60000);

				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode("bflorin:bflorin".getBytes())));
				androidHttpTransport.call(ConnectionStrings.getInstance().getNamespace() + methodName, envelope, headerList);
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
					listener.onTaskComplete(methodName, result, EnumNetworkStatus.ON);

				}
			} catch (Exception e) {
				Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			}
		}

	}

}
