/**
 * @author florinb
 *
 */
package com.distributieTest.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.distributieTest.beans.Borderou;
import com.distributieTest.beans.Eveniment;
import com.distributieTest.listeners.AsyncTaskListener;
import com.distributieTest.listeners.CustomSpinnerListener;
import com.distributieTest.model.AsyncTaskWSCall;
import com.distributieTest.model.EncodeJSONData;
import com.distributieTest.model.HandleJSONData;
import com.distributieTest.model.InfoStrings;
import com.distributieTest.model.UserInfo;
import com.distributieTest.model.Utils;

public class Evenimente extends Activity implements AsyncTaskListener, CustomSpinnerListener {

	Button eventButton, showDetBtn;

	ProgressBar progressBarEvent;
	private Timer myEventTimer;
	private int progressVal = 0;
	private Handler eventHandler = new Handler();

	private BorderouriAdapter adapterBorderouri;
	private static ArrayList<HashMap<String, String>> listBorderouri = new ArrayList<HashMap<String, String>>();
	Spinner spinnerBorderouri;

	ProgressWheel pw;
	LinearLayout layoutEventOut, layoutEventIn, layoutTotalTrip, layoutDetButton;
	TextView textDateEventOut, textTimeEventOut, textKmEventOut, textDateEventIn, textTimeEventIn, textKmEventIn,
			textTripTime, textTripDistance;

	CustomSpinnerClass spinnerClass = new CustomSpinnerClass();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);
		setContentView(R.layout.evenimente);

		InitialUISetup();

	}

	private void InitialUISetup() {

		try {

			ActionBar actionBar = getActionBar();
			actionBar.setTitle("Borderouri");
			actionBar.setDisplayHomeAsUpEnabled(true);

			layoutEventOut = (LinearLayout) findViewById(R.id.layoutOutEvent);
			layoutEventOut.setVisibility(View.INVISIBLE);

			layoutEventIn = (LinearLayout) findViewById(R.id.layoutInEvent);
			layoutEventIn.setVisibility(View.INVISIBLE);

			progressBarEvent = (ProgressBar) findViewById(R.id.progress_bar_event);
			progressBarEvent.setVisibility(View.INVISIBLE);

			textDateEventOut = (TextView) findViewById(R.id.textDateEventOut);
			textDateEventOut.setText("");
			textTimeEventOut = (TextView) findViewById(R.id.textTimeEventOut);
			textTimeEventOut.setText("");
			textKmEventOut = (TextView) findViewById(R.id.textKmEventOut);
			textKmEventOut.setText("");

			textDateEventIn = (TextView) findViewById(R.id.textDateEventIn);
			textDateEventIn.setText("");
			textTimeEventIn = (TextView) findViewById(R.id.textTimeEventIn);
			textTimeEventIn.setText("");
			textKmEventIn = (TextView) findViewById(R.id.textKmEventIn);
			textKmEventIn.setText("");

			layoutTotalTrip = (LinearLayout) findViewById(R.id.layoutTotalTrip);
			layoutTotalTrip.setVisibility(View.INVISIBLE);

			textTripTime = (TextView) findViewById(R.id.textTripTime);
			textTripTime.setText("");

			textTripDistance = (TextView) findViewById(R.id.textTripDistance);
			textTripDistance.setText("");

			eventButton = (Button) findViewById(R.id.saveEvent);
			eventButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.out1, 0, 0, 0);
			eventButton.setText("\t\tStart borderou");
			eventButton.setVisibility(View.INVISIBLE);
			eventButton.setOnTouchListener(new myEventBtnOnTouchListener());

			layoutDetButton = (LinearLayout) findViewById(R.id.layoutDetBtn);
			layoutDetButton.setVisibility(View.GONE);
			showDetBtn = (Button) findViewById(R.id.showDetBordBtn);
			showDetBtn.setVisibility(View.VISIBLE);
			showDetBtn.setOnClickListener(new myDetBtnOnClickListener());

			pw = (ProgressWheel) findViewById(R.id.pw_spinner);
			pw.setVisibility(View.INVISIBLE);

			spinnerBorderouri = (Spinner) findViewById(R.id.spinnerBorderouri);
			adapterBorderouri = new BorderouriAdapter(this, listBorderouri, R.layout.custom_row_list_borderouri,
					new String[] { "nrCrt", "codBorderou", "dataBorderou", "tipBorderou", "eveniment" }, new int[] {
							R.id.textNrCrt, R.id.textCodBorderou, R.id.textDataBorderou, R.id.textTipBorderou,
							R.id.textEvenimentBorderou });

			spinnerBorderouri.setAdapter(adapterBorderouri);
			spinnerBorderouri.setOnItemSelectedListener(spinnerClass);
			spinnerClass.setListener(this);

			performIncarcaBorderouri();

		} catch (Exception ex) {
			Toast.makeText(Evenimente.this, ex.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	private void startSpinner() {
		pw.setVisibility(View.VISIBLE);
		eventButton.setEnabled(false);
		pw.spin();

	}

	private void stopSpinner() {
		pw.setVisibility(View.INVISIBLE);
		pw.stopSpinning();
		eventButton.setEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:

			Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
			startActivity(nextScreen);
			finish();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	class myEventBtnOnTouchListener implements Button.OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			try {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					if (!InfoStrings.isGPSEnabled(v.getContext())) {
						InfoStrings.showGPSDisabledAlert(v.getContext());
					} else {

						progressBarEvent.setVisibility(View.VISIBLE);
						progressBarEvent.setProgress(0);
						progressVal = 0;
						myEventTimer = new Timer();
						myEventTimer.schedule(new UpdateProgress(), 40, 25);
					}

					return true;

				case MotionEvent.ACTION_UP:
					if (progressBarEvent.getVisibility() == View.VISIBLE) {

						myEventTimer.cancel();
						progressBarEvent.setVisibility(View.INVISIBLE);
						return true;
					}

				}
			} catch (Exception ex) {
				Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
			}

			return false;
		}

	}

	class myDetBtnOnClickListener implements Button.OnClickListener {
		public void onClick(View arg0) {
			Intent nextScreen = new Intent(Evenimente.this, Livrare.class);
			startActivity(nextScreen);

			finish();

		}

	}

	class UpdateProgress extends TimerTask {
		public void run() {
			progressVal++;
			if (50 == progressBarEvent.getProgress()) {
				eventHandler.post(new Runnable() {
					public void run() {

						progressBarEvent.setVisibility(View.INVISIBLE);
						performSaveNewEvent();

					}
				});

				myEventTimer.cancel();
			} else {
				progressBarEvent.setProgress(progressVal);

			}

		}
	}

	private void performSaveNewEvent() {

		try {

			startSpinner();

			String localStrDocNr = InfoStrings.getNrBorderou(Evenimente.this);

			HashMap<String, String> newEventData = new HashMap<String, String>();
			newEventData.put("codSofer", UserInfo.getInstance().getId());
			newEventData.put("document", localStrDocNr);
			newEventData.put("client", localStrDocNr);
			newEventData.put("codAdresa", " ");
			newEventData.put("eveniment", InfoStrings.getEveniment(Evenimente.this));
			newEventData.put("truckData", getTruckServiceData());

			EncodeJSONData jsonEvLivrare = new EncodeJSONData(this, newEventData);
			String serializedData = jsonEvLivrare.encodeNewEventData();

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("serializedEvent", serializedData);

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "saveNewEvent", params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	public void performIncarcaBorderouri() {
		try {

			startSpinner();

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("codSofer", UserInfo.getInstance().getId());
			params.put("tip", "d");
			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "getBorderouri", params);
			call.getCallResults();

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void populateListBorderouri(String borderouri) {

		HandleJSONData objListBorderouri = new HandleJSONData(this, borderouri);
		ArrayList<Borderou> borderouriArray = objListBorderouri.decodeJSONBorderouri();

		if (borderouriArray.size() > 0) {

			listBorderouri.clear();
			int selectedPosition = -1;
			spinnerBorderouri.setEnabled(true);

			HashMap<String, String> temp;

			for (int i = 0; i < borderouriArray.size(); i++) {
				temp = new HashMap<String, String>();

				temp.put("nrCrt", String.valueOf(i + 1) + ".");
				temp.put("codBorderou", borderouriArray.get(i).getNumarBorderou());
				temp.put("dataBorderou", borderouriArray.get(i).getDataEmiterii());
				temp.put("tipBorderou", InfoStrings.getStringTipBorderou(borderouriArray.get(i).getTipBorderou()));
				temp.put("eveniment", borderouriArray.get(i).getEvenimentBorderou());

				if (selectedPosition == -1) {
					if (borderouriArray.get(i).getEvenimentBorderou().equals("P")) {
						selectedPosition = i;
					}
				}

				listBorderouri.add(temp);
			}

			spinnerBorderouri.setAdapter(adapterBorderouri);

			if (selectedPosition != -1) {
				spinnerBorderouri.setSelection(selectedPosition);
				spinnerBorderouri.setEnabled(false);
			} else {
				eventButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.out1, 0, 0, 0);
				eventButton.setText("\t\tStart borderou");
				eventButton.setVisibility(View.VISIBLE);
				spinnerBorderouri.setEnabled(true);
			}

		} else {
			listBorderouri.clear();
			spinnerBorderouri.setAdapter(adapterBorderouri);

			layoutDetButton.setVisibility(View.GONE);
			layoutTotalTrip.setVisibility(View.GONE);
			eventButton.setVisibility(View.INVISIBLE);
			layoutEventOut.setVisibility(View.INVISIBLE);
			layoutEventIn.setVisibility(View.INVISIBLE);

			Toast.makeText(getApplicationContext(), "Nu exista borderouri!", Toast.LENGTH_LONG).show();
		}

	}

	private String getTruckServiceData() {
		String truckParams = "0@0";

		try {
			Context con;
			SharedPreferences pref;

			con = createPackageContext("com.android.dataservice", 0);
			pref = con.getSharedPreferences("TRUCK_DATA", Context.MODE_MULTI_PROCESS);

			truckParams = pref.getString("params", "0@0");

		} catch (Exception ex) {
			truckParams = "0@0";
			Log.e("Error", ex.toString());
		}

		return truckParams;

	}

	public void performGetBorderouEvents() {

		try {
			startSpinner();

			HashMap<String, String> params = new HashMap<String, String>();

			params.put("nrDoc", InfoStrings.getNrBorderou(getApplicationContext()));
			params.put("tipEv", "0");

			AsyncTaskWSCall call = new AsyncTaskWSCall(this, "getDocEvents", params);
			call.getCallResults();

		} catch (Exception ex) {

			Toast.makeText(getBaseContext(), ex.toString(), Toast.LENGTH_LONG).show();

		}

	}

	private void populateEventsList(String eventsData) {

		try {

			HandleJSONData objListEvenimente = new HandleJSONData(this, eventsData);
			ArrayList<Eveniment> evenimenteArray = objListEvenimente.decodeJSONEveniment();

			if (evenimenteArray.size() > 0) {

				layoutEventOut.setVisibility(View.INVISIBLE);
				layoutEventIn.setVisibility(View.INVISIBLE);
				eventButton.setVisibility(View.INVISIBLE);
				layoutDetButton.setVisibility(View.GONE);
				layoutTotalTrip.setVisibility(View.GONE);

				for (int i = 0; i < evenimenteArray.size(); i++) {

					if (evenimenteArray.get(i).getEveniment().equals("P")) {

						textDateEventOut.setText(evenimenteArray.get(i).getData());

						textTimeEventOut.setText(evenimenteArray.get(i).getOra().substring(0, 2) + ":"
								+ evenimenteArray.get(i).getOra().substring(2, 4) + ":"
								+ evenimenteArray.get(i).getOra().substring(4, 6));
						textKmEventOut.setText(evenimenteArray.get(i).getDistantaKM());
						layoutEventOut.setVisibility(View.VISIBLE);

						eventButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.in1, 0, 0, 0);
						eventButton.setText("\t\tStop borderou");
						eventButton.setVisibility(View.VISIBLE);

						layoutDetButton.setVisibility(View.VISIBLE);

					}
					if (evenimenteArray.get(i).getEveniment().equals("S")) {

						textDateEventIn.setText(evenimenteArray.get(i).getData());
						textTimeEventIn.setText(evenimenteArray.get(i).getOra().substring(0, 2) + ":"
								+ evenimenteArray.get(i).getOra().substring(2, 4) + ":"
								+ evenimenteArray.get(i).getOra().substring(4, 6));
						textKmEventIn.setText(evenimenteArray.get(i).getDistantaKM());

						eventButton.setVisibility(View.INVISIBLE);
						layoutDetButton.setVisibility(View.GONE);
						layoutEventIn.setVisibility(View.VISIBLE);

						layoutTotalTrip.setVisibility(View.VISIBLE);

						textTripTime.setText(getTripTime(textDateEventOut.getText().toString(), textDateEventIn
								.getText().toString()));

						double startDistance = Double.valueOf(textKmEventOut.getText().toString());
						double stopDistance = Double.valueOf(textKmEventIn.getText().toString());
						double tripDistance = stopDistance - startDistance;

						textTripDistance.setText(String.valueOf(tripDistance));

					}

				}

			} else {

				// nu exista evenimente
				layoutEventOut.setVisibility(View.INVISIBLE);
				layoutEventIn.setVisibility(View.INVISIBLE);

				layoutDetButton.setVisibility(View.GONE);
				layoutTotalTrip.setVisibility(View.GONE);

				eventButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.out1, 0, 0, 0);
				eventButton.setText("\t\tStart borderou");
				eventButton.setVisibility(View.VISIBLE);

			}

		} catch (Exception ex) {
			Toast.makeText(Evenimente.this, ex.toString(), Toast.LENGTH_LONG).show();
		}

	}

	private String getTripTime(String timeStart, String timeStop) {

		String[] strDataStart = timeStart.trim().split("-");
		String[] strOraStart = timeStart.trim().split(":");

		Calendar cal1 = Calendar.getInstance();
		cal1.set(Calendar.YEAR, Integer.valueOf("20" + strDataStart[2]));
		cal1.set(Calendar.MONTH, Utils.getMonthNumber(strDataStart[1]));
		cal1.set(Calendar.DAY_OF_MONTH, Integer.valueOf(strDataStart[0]));
		cal1.set(Calendar.HOUR, Integer.valueOf(strOraStart[0]));
		cal1.set(Calendar.MINUTE, Integer.valueOf(strOraStart[1]));

		String[] strDataStop = timeStop.trim().split("-");
		String[] strOraStop = timeStop.trim().split(":");

		Calendar cal2 = Calendar.getInstance();
		cal2.set(Calendar.YEAR, Integer.valueOf("20" + strDataStop[2]));
		cal2.set(Calendar.MONTH, Utils.getMonthNumber(strDataStop[1]));
		cal2.set(Calendar.DAY_OF_MONTH, Integer.valueOf(strDataStop[0]));
		cal2.set(Calendar.HOUR, Integer.valueOf(strOraStop[0]));
		cal2.set(Calendar.MINUTE, Integer.valueOf(strOraStop[1]));

		long milisecs = cal2.getTimeInMillis() - cal1.getTimeInMillis();
		String strTotalTime = Utils.getDuration(milisecs);

		return strTotalTime;

	}

	@Override
	public void onBackPressed() {

		Intent nextScreen = new Intent(getApplicationContext(), MainMenu.class);
		startActivity(nextScreen);
		finish();
		return;
	}

	@Override
	public void onTaskComplete(String methodName, String result) {
		if (methodName.equals("getBorderouri")) {
			stopSpinner();
			populateListBorderouri(result);
		}

		if (methodName.equals("saveNewEvent")) {
			stopSpinner();
			performIncarcaBorderouri();

			if (InfoStrings.getEveniment(Evenimente.this).equals("S")) {
				InfoStrings.setNrBorderou(Evenimente.this, "0");
			}
		}

		if (methodName.equals("getDocEvents")) {
			stopSpinner();
			populateEventsList(result);
		}

	}

	@Override
	public void onSelectedSpinnerItem(int spinnerId, HashMap<String, String> map) {
		if (spinnerId == R.id.spinnerBorderouri) {
			InfoStrings.setNrBorderou(getApplicationContext(), map.get("codBorderou"));
			InfoStrings.setEveniment(getApplicationContext(), map.get("eveniment"));
			InfoStrings.setTipBorderou(getApplicationContext(), map.get("tipBorderou"));

			performGetBorderouEvents();

		}

	}

}
