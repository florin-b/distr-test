/**
 * @author florinb
 *
 */
package com.distributie.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.distributie.beans.BeanAlarma;
import com.distributie.beans.BeanClientAlarma;
import com.distributie.beans.BeanEvenimentStop;
import com.distributie.dialog.SugestieEvenimentDialog;
import com.distributie.enums.EnumMotivOprire;
import com.distributie.enums.EnumNetworkStatus;
import com.distributie.enums.EnumOperatiiEvenimente;
import com.distributie.enums.EnumTipAlarma;
import com.distributie.listeners.EvenimentDialogListener;
import com.distributie.listeners.OperatiiBorderouriListener;
import com.distributie.listeners.OperatiiEvenimenteListener;
import com.distributie.model.CurrentStatus;
import com.distributie.model.OperatiiBorderouriDAOImpl;
import com.distributie.model.OperatiiEvenimente;

public class MainMenu extends Activity implements OperatiiBorderouriListener, OperatiiEvenimenteListener, EvenimentDialogListener {

	GridView mainGridView;

	private String[] btnNames = { "Etape", "Istoric", "Iesire" };

	private int[] btnIcons = new int[] { R.drawable.delivery, R.drawable.history, R.drawable.exit };

	private Handler handler = new Handler();
	private OperatiiBorderouriDAOImpl opBordeoruri;
	private Timer timer;
	private TimerTask timerTask;
	private OperatiiEvenimente opEvenimente;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.LRTheme);
		setContentView(R.layout.main_menu);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Distributie");
		actionBar.setDisplayHomeAsUpEnabled(true);

		try {

			this.mainGridView = (GridView) findViewById(R.id.mainGridView);
			this.mainGridView.setAdapter(new ButtonAdapter(this));

		} catch (Exception ex) {
			Toast.makeText(getBaseContext(), ex.toString(), Toast.LENGTH_LONG).show();
		}

		/*
		 * opBordeoruri = new OperatiiBorderouriDAOImpl(this);
		 * opBordeoruri.setEventListener(this);
		 * 
		 * opEvenimente = new OperatiiEvenimente(this);
		 * opEvenimente.setOperatiiEvenimenteListener(this);
		 * 
		 * startTimerTask();
		 */

		Intent nextScreen = new Intent(MainMenu.this, AfisEtape.class);
		startActivity(nextScreen);
		finish();

	}

	private void startTimerTask() {

		timer = new Timer();
		initializeTimerTask();
		// timer.schedule(timerTask, 300000, 300000);

		timer.schedule(timerTask, 5000, 5000);

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

			SugestieEvenimentDialog sugestie = new SugestieEvenimentDialog(MainMenu.this, listEv);
			sugestie.setEvenimentDialogListener(this);
			sugestie.show();

		}
	}

	public class ButtonAdapter extends BaseAdapter {
		private Context mContext;

		public ButtonAdapter(Context c) {
			this.mContext = c;
		}

		public int getCount() {
			return getNrBtns(); // nr. butoane
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			Button btn;
			Typeface font = Typeface.SERIF;

			if (convertView == null) {

				btn = new Button(this.mContext);
				btn.setLayoutParams(new GridView.LayoutParams(130, 110));

			} else {
				btn = (Button) convertView;
			}

			btn.setText(getBtnName(position));

			btn.setCompoundDrawablesWithIntrinsicBounds(0, getBtnIcon(position), 0, 0);

			btn.setId(position);
			btn.setTextSize(19);
			btn.setTextColor(android.graphics.Color.rgb(11, 86, 111));
			btn.setTypeface(font);
			btn.setOnClickListener(new MyOnClickListener(position));

			btn.setBackgroundResource(R.drawable.grid_button_style);

			return btn;

		}

		class MyOnClickListener implements OnClickListener {
			private final int position;

			public MyOnClickListener(int position) {
				this.position = position;
			}

			public void onClick(View v) {

				String selectedBtnName = getBtnName(this.position);

				// info
				if (selectedBtnName.equalsIgnoreCase("Sofer")) {

					try {

						Intent nextScreen = new Intent(MainMenu.this, User.class);
						startActivity(nextScreen);

						finish();

					} catch (Exception e) {
						Toast.makeText(MainMenu.this, e.toString(), Toast.LENGTH_SHORT).show();
					}

				}

				

				// livrare
				

				// etape
				if (selectedBtnName.equalsIgnoreCase("Etape")) {

					try {

						Intent nextScreen = new Intent(MainMenu.this, AfisEtape.class);
						startActivity(nextScreen);

						finish();

					} catch (Exception e) {
						Toast.makeText(MainMenu.this, e.toString(), Toast.LENGTH_SHORT).show();
					}

				}

				// test
				if (selectedBtnName.equalsIgnoreCase("Test")) {

					try {

						Intent nextScreen = new Intent(MainMenu.this, TestConnection.class);
						startActivity(nextScreen);

						finish();

					} catch (Exception e) {
						Toast.makeText(MainMenu.this, e.toString(), Toast.LENGTH_SHORT).show();
					}

				}

				// istoric
				if (selectedBtnName.equalsIgnoreCase("Istoric")) {

					try {

						Intent nextScreen = new Intent(MainMenu.this, AfisBorderouri.class);
						startActivity(nextScreen);

						finish();

					} catch (Exception e) {
						Toast.makeText(MainMenu.this, e.toString(), Toast.LENGTH_SHORT).show();
					}

				}

				// exit
				if (selectedBtnName.equalsIgnoreCase("Iesire")) {

					System.exit(0);

				}

			}

		}

	}

	private void showCustomDialog(String bordStarted) {

		boolean isBordStarted = Boolean.valueOf(bordStarted);

		if (!isBordStarted) {
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.dialog_start_bord);
			dialog.setTitle("Atentie!");

			TextView text = (TextView) dialog.findViewById(R.id.text);
			text.setText("Marcati evenimentul Start Borderou!");

			Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startTimerTask();
					dialog.dismiss();
				}
			});

			dialog.show();
			stopTimerTask();
		}
	}

	private int getNrBtns() {
		return btnNames.length;
	}

	private String getBtnName(int btnPos) {
		return this.btnNames[btnPos];

	}

	private int getBtnIcon(int btnPos) {
		return this.btnIcons[btnPos];
	}

	@Override
	public void finish() {
		stopTimerTask();
		super.finish();
	}

	@Override
	public void eventComplete(String result, EnumOperatiiEvenimente methodName, EnumNetworkStatus networkStatus) {
		switch (methodName) {
		case CHECK_BORD_STARTED:
			showCustomDialog(result);
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
		default:
			break;
		}

	}

	@Override
	public void evenimentDialogProduced() {
		startTimerTask();

	}
}
