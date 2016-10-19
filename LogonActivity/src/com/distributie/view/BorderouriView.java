/**
 * @author florinb
 *
 */
package com.distributie.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.distributie.beans.BeanAlarma;
import com.distributie.beans.BeanClientAlarma;
import com.distributie.beans.BeanEvenimentStop;
import com.distributie.beans.Borderou;
import com.distributie.beans.Eveniment;
import com.distributie.dialog.SugestieEvenimentDialog;
import com.distributie.enums.EnumMotivOprire;
import com.distributie.enums.EnumNetworkStatus;
import com.distributie.enums.EnumOperatiiBorderou;
import com.distributie.enums.EnumOperatiiEvenimente;
import com.distributie.enums.EnumTipAlarma;
import com.distributie.enums.TipBorderou;
import com.distributie.listeners.BorderouriDAOListener;
import com.distributie.listeners.CustomSpinnerListener;
import com.distributie.listeners.EvenimentDialogListener;
import com.distributie.listeners.OperatiiBorderouriListener;
import com.distributie.listeners.OperatiiEvenimenteListener;
import com.distributie.model.BorderouriDAOImpl;
import com.distributie.model.CurrentStatus;
import com.distributie.model.HandleJSONData;
import com.distributie.model.InfoStrings;
import com.distributie.model.OperatiiBorderouriDAOImpl;
import com.distributie.model.OperatiiEvenimente;
import com.distributie.model.UserInfo;
import com.distributie.model.Utils;

public class BorderouriView extends Activity implements CustomSpinnerListener, BorderouriDAOListener, OperatiiBorderouriListener, OperatiiEvenimenteListener,
		EvenimentDialogListener {

	@InjectView(R.id.showDetBordBtn)
	Button showDetBtn;

	@InjectView(R.id.progress_bar_event)
	ProgressBar progressBarEvent;

	@InjectView(R.id.spinnerBorderouri)
	Spinner spinnerBorderouri;

	@InjectView(R.id.layoutOutEvent)
	LinearLayout layoutEventOut;

	@InjectView(R.id.layoutInEvent)
	LinearLayout layoutEventIn;

	@InjectView(R.id.layoutTotalTrip)
	LinearLayout layoutTotalTrip;

	@InjectView(R.id.layoutBorderouri)
	LinearLayout layoutBorderouri;

	@InjectView(R.id.layoutDetBtn)
	LinearLayout layoutDetButton;

	@InjectView(R.id.textDateEventOut)
	TextView textDateEventOut;

	@InjectView(R.id.textTimeEventOut)
	TextView textTimeEventOut;

	@InjectView(R.id.textKmEventOut)
	TextView textKmEventOut;

	@InjectView(R.id.textDateEventIn)
	TextView textDateEventIn;

	@InjectView(R.id.textTimeEventIn)
	TextView textTimeEventIn;

	@InjectView(R.id.textKmEventIn)
	TextView textKmEventIn;

	@InjectView(R.id.textTripTime)
	TextView textTripTime;

	@InjectView(R.id.textTripDistance)
	TextView textTripDistance;

	@InjectView(R.id.layoutNoBord)
	RelativeLayout layoutNoBord;

	private Timer myEventTimer;
	private int progressVal = 0;
	private Handler eventHandler = new Handler();

	private BorderouriAdapter adapterBorderouri;
	private static ArrayList<HashMap<String, String>> listBorderouri = new ArrayList<HashMap<String, String>>();

	CustomSpinnerClass spinnerClass = new CustomSpinnerClass();
	private OperatiiBorderouriDAOImpl newEvent;

	private Timer timer;
	private TimerTask timerTask;
	private Handler handler = new Handler();
	private OperatiiEvenimente opEvenimente;
	private Button eventButton, saveIncarcareButton;

	private enum EnumTipEveniment {
		SFARSIT_INCARCARE, START_STOP_BORDDEROU;
	}

	private EnumTipEveniment evenimentBorderou;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);
		setContentView(R.layout.evenimente);
		ButterKnife.inject(this);

		newEvent = new OperatiiBorderouriDAOImpl(this);
		newEvent.setEventListener(this);

		opEvenimente = new OperatiiEvenimente(this);
		opEvenimente.setOperatiiEvenimenteListener(this);

		InitialUISetup();

		startTimerTask();

	}

	private void startTimerTask() {

		timer = new Timer();
		initializeTimerTask();
		timer.schedule(timerTask, 300000, 300000);

	}

	public void stopTimerTask() {

		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	public void initializeTimerTask() {

		timerTask = new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						getEvenimente();

					}
				});
			}
		};
	}

	private void getEvenimente() {

		if (CurrentStatus.getInstance().getNrBorderou() != null) {

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("codBorderou", CurrentStatus.getInstance().getNrBorderou());

			opEvenimente.getEvenimentStop(params);
		}
	}

	private void incarcaEvenimente(BeanEvenimentStop evenimentStop) {

		if (!evenimentStop.isEvenimentSalvat()) {
			List<BeanAlarma> listEv = new ArrayList<BeanAlarma>();

			for (BeanClientAlarma client : evenimentStop.getClientiAlarma()) {
				BeanAlarma b = new BeanAlarma();
				b.setTipAlarma(EnumTipAlarma.CLIENT);
				b.setNumeAlarma("Sosire " + client.getNume());
				b.setCodAlarma(client.getCodClient());
				b.setCodAdresa(client.getCodAdresa());
				listEv.add(b);
			}

			for (EnumMotivOprire motiv : EnumMotivOprire.values()) {

				BeanAlarma b = new BeanAlarma();
				b.setTipAlarma(EnumTipAlarma.EVENIMENT);
				b.setNumeAlarma(motiv.getNume());
				b.setCodAlarma(String.valueOf(motiv.getCod()));
				b.setIdEveniment(evenimentStop.getIdEveniment());
				listEv.add(b);
			}

			stopTimerTask();

			SugestieEvenimentDialog sugestie = new SugestieEvenimentDialog(BorderouriView.this, listEv);
			sugestie.setEvenimentDialogListener(this);
			sugestie.show();

		}
	}

	private void InitialUISetup() {

		try {

			ActionBar actionBar = getActionBar();
			actionBar.setTitle("Borderouri ");
			actionBar.setDisplayHomeAsUpEnabled(true);

			layoutEventOut.setVisibility(View.INVISIBLE);
			layoutEventIn.setVisibility(View.INVISIBLE);

			progressBarEvent.setVisibility(View.INVISIBLE);

			textDateEventOut.setText("");
			textTimeEventOut.setText("");
			textKmEventOut.setText("");
			textDateEventIn.setText("");
			textTimeEventIn.setText("");
			textKmEventIn.setText("");
			layoutTotalTrip.setVisibility(View.INVISIBLE);
			textTripTime.setText("");
			textTripDistance.setText("");

			eventButton = (Button) findViewById(R.id.saveEvent);
			eventButton.setVisibility(View.GONE);
			eventButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.out1, 0, 0, 0);
			eventButton.setText("\t\tStart borderou");
			eventButton.setOnTouchListener(new myEventBtnOnTouchListener());

			saveIncarcareButton = (Button) findViewById(R.id.saveIncarcare);
			saveIncarcareButton.setText("Sfarsit incarcare");
			saveIncarcareButton.setOnTouchListener(new myEventBtnOnTouchListener());
			saveIncarcareButton.setVisibility(View.GONE);

			layoutDetButton.setVisibility(View.GONE);

			showDetBtn.setVisibility(View.VISIBLE);

			adapterBorderouri = new BorderouriAdapter(this, listBorderouri, R.layout.custom_row_list_borderouri, new String[] { "nrCrt", "codBorderou",
					"dataBorderou", "tipBorderou", "eveniment" }, new int[] { R.id.textNrCrt, R.id.textCodBorderou, R.id.textDataBorderou,
					R.id.textTipBorderou, R.id.textEvenimentBorderou });

			spinnerBorderouri.setAdapter(adapterBorderouri);
			spinnerBorderouri.setOnItemSelectedListener(spinnerClass);

			spinnerClass.setListener(this);

			layoutBorderouri.setVisibility(View.INVISIBLE);
			layoutNoBord.setVisibility(View.GONE);

			performIncarcaBorderouri();

		} catch (Exception ex) {
			Toast.makeText(BorderouriView.this, ex.toString(), Toast.LENGTH_SHORT).show();
		}

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

					progressBarEvent.setVisibility(View.VISIBLE);
					progressBarEvent.setProgress(0);
					progressVal = 0;
					myEventTimer = new Timer();
					myEventTimer.schedule(new UpdateProgress(), 35, 15);

					return true;

				case MotionEvent.ACTION_UP:
					if (progressBarEvent.getVisibility() == View.VISIBLE) {

						myEventTimer.cancel();
						myEventTimer = null;
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

	@OnClick(R.id.showDetBordBtn)
	public void myDetBtnOnClickListener() {
		Intent nextScreen = new Intent(BorderouriView.this, Livrare.class);
		startActivity(nextScreen);
		finish();
	}

	class UpdateProgress extends TimerTask {
		public void run() {
			progressVal++;
			if (progressBarEvent.getProgress() == 50) {
				myEventTimer.cancel();
				myEventTimer = null;
				eventHandler.post(new Runnable() {
					public void run() {
						progressBarEvent.setVisibility(View.INVISIBLE);

						performAction();
					}
				});

			} else {
				progressBarEvent.setProgress(progressVal);

			}

		}
	}

	@Override
	public void finish() {
		stopTimerTask();
		super.finish();
	}

	private void performAction() {

		if (evenimentBorderou == EnumTipEveniment.START_STOP_BORDDEROU) {
			eventButton.setEnabled(false);
			saveStartStopEvent();
		} else if (evenimentBorderou == EnumTipEveniment.SFARSIT_INCARCARE) {
			saveIncarcareButton.setEnabled(false);
			setSfarsitIncarcareBorderou();
		}

	}

	private void getSfarsitIncarcareBorderou() {

		if (CurrentStatus.getInstance().getNrBorderou() != "") {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("document", CurrentStatus.getInstance().getNrBorderou());
			params.put("codSofer", UserInfo.getInstance().getId());
			opEvenimente.getSfarsitIncarcare(params);
		}

	}

	private void setSfarsitIncarcareBorderou() {

		if (CurrentStatus.getInstance().getNrBorderou() != "") {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("document", CurrentStatus.getInstance().getNrBorderou());
			params.put("codSofer", UserInfo.getInstance().getId());
			opEvenimente.setSfarsitIncarcare(params);
		}

	}

	private void saveStartStopEvent() {

		try {

			String localStrDocNr = CurrentStatus.getInstance().getNrBorderou();
			HashMap<String, String> newEventData = new HashMap<String, String>();
			newEventData.put("codSofer", UserInfo.getInstance().getId());
			newEventData.put("document", localStrDocNr);
			newEventData.put("client", localStrDocNr);
			newEventData.put("codAdresa", " ");
			newEventData.put("eveniment", CurrentStatus.getInstance().getEveniment());

			newEvent.saveNewEventBorderou(newEventData, null);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}

	}

	public void performIncarcaBorderouri() {
		try {

			BorderouriDAOImpl bord = BorderouriDAOImpl.getInstance(this);
			bord.setBorderouEventListener(this);
			bord.getBorderouri(UserInfo.getInstance().getId(), "d", "-1");

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private void populateListBorderouri(String borderouri) {

		HandleJSONData objListBorderouri = new HandleJSONData(this, borderouri);
		ArrayList<Borderou> borderouriArray = objListBorderouri.decodeJSONBorderouri();

		if (borderouriArray.size() > 0) {

			if (evenimentBorderou == EnumTipEveniment.START_STOP_BORDDEROU)
				setBordDetVisibility(true);

			listBorderouri.clear();
			int selectedPosition = -1;
			layoutBorderouri.setVisibility(View.VISIBLE);
			layoutNoBord.setVisibility(View.GONE);
			spinnerBorderouri.setEnabled(true);

			HashMap<String, String> temp;

			for (int i = 0; i < borderouriArray.size(); i++) {
				temp = new HashMap<String, String>();

				temp.put("nrCrt", String.valueOf(i + 1) + ".");
				temp.put("codBorderou", borderouriArray.get(i).getNumarBorderou());
				temp.put("dataBorderou", borderouriArray.get(i).getDataEmiterii());
				temp.put("tipBorderou", InfoStrings.getStringTipBorderou(borderouriArray.get(i).getTipBorderou()).toString());
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

			layoutBorderouri.setVisibility(View.GONE);
			layoutNoBord.setVisibility(View.VISIBLE);

		}

	}

	private void setBordDetVisibility(boolean isVisible) {

		if (isVisible) {
			layoutDetButton.setVisibility(View.VISIBLE);

			layoutEventOut.setVisibility(View.VISIBLE);
			textDateEventOut.setVisibility(View.VISIBLE);
			textTimeEventOut.setVisibility(View.VISIBLE);
			textKmEventOut.setVisibility(View.VISIBLE);
			showDetBtn.setVisibility(View.VISIBLE);
		}

	}

	public void getStartStopBorderou() {
		newEvent.getDocEvents(CurrentStatus.getInstance().getNrBorderou(), "0");
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

						textTimeEventOut.setText(evenimenteArray.get(i).getOra().substring(0, 2) + ":" + evenimenteArray.get(i).getOra().substring(2, 4) + ":"
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
						textTimeEventIn.setText(evenimenteArray.get(i).getOra().substring(0, 2) + ":" + evenimenteArray.get(i).getOra().substring(2, 4) + ":"
								+ evenimenteArray.get(i).getOra().substring(4, 6));
						textKmEventIn.setText(evenimenteArray.get(i).getDistantaKM());

						eventButton.setVisibility(View.INVISIBLE);
						layoutDetButton.setVisibility(View.GONE);
						layoutEventIn.setVisibility(View.VISIBLE);

						layoutTotalTrip.setVisibility(View.VISIBLE);

						textTripTime.setText(getTripTime(textDateEventOut.getText().toString(), textDateEventIn.getText().toString()));

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
			Toast.makeText(BorderouriView.this, ex.toString(), Toast.LENGTH_LONG).show();
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
	public void onSelectedSpinnerItem(int spinnerId, HashMap<String, String> map) {
		if (spinnerId == R.id.spinnerBorderouri) {

			CurrentStatus.getInstance().setNrBorderou(map.get("codBorderou"));
			CurrentStatus.getInstance().setEveniment(map.get("eveniment"));
			CurrentStatus.getInstance().setTipBorderou(TipBorderou.valueOf(map.get("tipBorderou")));

			getSfarsitIncarcareBorderou();

		}

	}

	private void handleSfarsitIncarcareEvent(String result) {

		if (!result.equals("-1") && (result.contains("#") || result.equals("1"))) {
			eventButton.setVisibility(View.VISIBLE);
			eventButton.setEnabled(true);
			saveIncarcareButton.setVisibility(View.GONE);
			evenimentBorderou = EnumTipEveniment.START_STOP_BORDDEROU;
			spinnerBorderouri.setEnabled(false);
			getStartStopBorderou();
		} else {
			eventButton.setVisibility(View.GONE);
			saveIncarcareButton.setVisibility(View.VISIBLE);
			saveIncarcareButton.setEnabled(true);
			evenimentBorderou = EnumTipEveniment.SFARSIT_INCARCARE;

		}

	}

	public void loadComplete(String result, EnumOperatiiBorderou methodName) {
		switch (methodName) {
		case GET_BORDEROURI:
			populateListBorderouri(result);
			break;
		default:
			break;
		}

	}

	@Override
	public void eventComplete(String result, EnumOperatiiEvenimente methodName, EnumNetworkStatus networkStatus) {

		switch (methodName) {
		case GET_DOC_EVENTS:
			populateEventsList(result);
			break;
		case SAVE_NEW_EVENT:

			performIncarcaBorderouri();

			if (CurrentStatus.getInstance().getEveniment().equals("S")) {
				CurrentStatus.getInstance().setNrBorderou("0");
			}
			break;
		default:
			break;

		}

	}

	@Override
	public void opEventComplete(String result, EnumOperatiiEvenimente methodName) {
		switch (methodName) {
		case CHECK_STOP:
			incarcaEvenimente(opEvenimente.deserializeEvenimentStop(result));
			break;
		case SET_SFARSIT_INC:
		case GET_SFARSIT_INC:
			handleSfarsitIncarcareEvent(result);
			break;
		default:
			break;
		}

	}

	@Override
	public void evenimentDialogProduced() {
		startTimerTask();

	}

}
