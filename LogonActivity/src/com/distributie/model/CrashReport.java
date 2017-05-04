package com.distributie.model;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.distributie.connectors.ConnectionStrings;
import com.distributie.view.R;

public class CrashReport extends Activity{

	static final String STACKTRACE = "stacktrace";
	Dialog commentsDialog;

	private static final String SOAP_ACTION = ConnectionStrings.getInstance().getNamespace()
			+ "sendMail";
	private static final String METHOD_NAME = "sendMail";

	private String crashDescription = "";

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		final String stackTrace = getIntent().getStringExtra(STACKTRACE);

		commentsDialog = new Dialog(CrashReport.this);
		commentsDialog.setContentView(R.layout.crashreportbox);
		commentsDialog.setTitle("Aplicatia s-a oprit!");
		commentsDialog.setCancelable(false);

		crashDescription = stackTrace;

		Button btnOkComment = (Button) commentsDialog
				.findViewById(R.id.btnOkCrash);
		btnOkComment.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				sendCrashMail();
				commentsDialog.dismiss();

			}
		});

		commentsDialog.show();
		commentsDialog.getWindow().setLayout(300, 200);

	}

	private void sendCrashMail() {

		try {
			sendCrashMail crash = new sendCrashMail(this);
			crash.execute(("dummy"));

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_LONG).show();
		}

	}

	private class sendCrashMail extends AsyncTask<String, Void, String> {

		Context mContext;
		private ProgressDialog dialog;

		private sendCrashMail(Context context) {
			super();
			this.mContext = context;
		}

		protected void onPreExecute() {
			this.dialog = new ProgressDialog(mContext);
			this.dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			this.dialog.setMessage("Raportare eroare...");
			this.dialog.setCancelable(false);
			this.dialog.show();
		}

		@Override
		protected String doInBackground(String... url) {
			String response = "";
			try {

				SoapObject request = new SoapObject(
						ConnectionStrings.getInstance().getNamespace(), METHOD_NAME);

				request.addProperty("msgBody", crashDescription);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;

				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						ConnectionStrings.getInstance().getUrl(), 25000);

				List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
				headerList.add(new HeaderProperty("Authorization", "Basic "
						+ org.kobjects.base64.Base64.encode("bflorin:bflorin"
								.getBytes())));

				androidHttpTransport.call(SOAP_ACTION, envelope, headerList);
				Object result = envelope.getResponse();
				response = result.toString();
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			

			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}

		}

	}

	
}
