package com.distributie.dialog;

import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.distributie.adapters.AdapterTipEveniment;
import com.distributie.beans.BeanAlarma;
import com.distributie.enums.EnumNetworkStatus;
import com.distributie.enums.EnumOperatiiEvenimente;
import com.distributie.listeners.EvenimentDialogListener;
import com.distributie.listeners.OperatiiBorderouriListener;
import com.distributie.listeners.OperatiiEvenimenteListener;
import com.distributie.model.CurrentStatus;
import com.distributie.model.OperatiiBorderouriDAOImpl;
import com.distributie.model.OperatiiEvenimente;
import com.distributie.model.UserInfo;
import com.distributie.view.R;

public class SugestieEvenimentDialog extends Dialog implements OperatiiEvenimenteListener, OperatiiBorderouriListener {

	private Context context;
	private List<BeanAlarma> listEvenimente;
	private ListView listViewEvenimente;
	private OperatiiEvenimente opEvenimente;
	private EvenimentDialogListener evenimentDialogListener;

	public SugestieEvenimentDialog(Context context, List<BeanAlarma> listEvenimente) {
		super(context);
		this.context = context;
		this.listEvenimente = listEvenimente;

		setContentView(R.layout.optiuni_eveniment_dialog);
		setTitle("Selectati motiv oprire");
		setCancelable(true);

		opEvenimente = new OperatiiEvenimente(context);
		opEvenimente.setOperatiiEvenimenteListener(this);
		this.setCancelable(false);
		setupLayout();
	}

	private void setupLayout() {

		listViewEvenimente = (ListView) findViewById(R.id.listEvenimente);

		AdapterTipEveniment adapter = new AdapterTipEveniment(context, listEvenimente);
		listViewEvenimente.setAdapter(adapter);
		setListViewListener();

		playWarningSound();

	}

	private void playWarningSound() {
		final MediaPlayer mp = MediaPlayer.create(context, R.raw.warning);
		mp.start();

	}

	private void setListViewListener() {

		listViewEvenimente.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				BeanAlarma eveniment = (BeanAlarma) parent.getAdapter().getItem(position);
				saveEveniment(eveniment);
				notifyDialog();

			}
		});

	}

	private void saveEveniment(BeanAlarma eveniment) {
		switch (eveniment.getTipAlarma()) {
		case EVENIMENT:
			saveEvenimentDeclarat(eveniment);
			break;
		case CLIENT:
			saveSosireClient(eveniment);
			break;
		default:
			break;
		}

	}

	private void saveEvenimentDeclarat(BeanAlarma eveniment) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("idEveniment", String.valueOf(eveniment.getIdEveniment()));
		params.put("codSofer", UserInfo.getInstance().getId()); //
		params.put("codBorderou", CurrentStatus.getInstance().getNrBorderou());

		params.put("codEveniment", eveniment.getCodAlarma());

		opEvenimente.saveNewStop(params);
	}

	private void saveSosireClient(BeanAlarma eveniment) {
		HashMap<String, String> newEventData = new HashMap<String, String>();
		newEventData.put("codSofer", UserInfo.getInstance().getId());
		newEventData.put("document", CurrentStatus.getInstance().getNrBorderou());
		newEventData.put("client", eveniment.getCodAlarma());
		newEventData.put("codAdresa", eveniment.getCodAdresa());
		newEventData.put("eveniment", "0");

		OperatiiBorderouriDAOImpl bordStatus = new OperatiiBorderouriDAOImpl(context);
		bordStatus.setEventListener(this);
		bordStatus.saveNewEventClient(newEventData);
	}

	private void notifyDialog() {
		if (evenimentDialogListener != null)
			evenimentDialogListener.evenimentDialogProduced();
		dismiss();
	}

	public void setEvenimentDialogListener(EvenimentDialogListener listener) {
		this.evenimentDialogListener = listener;
	}

	@Override
	public void opEventComplete(String result, EnumOperatiiEvenimente methodName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eventComplete(String result, EnumOperatiiEvenimente methodName, EnumNetworkStatus networkStatus) {
		// TODO Auto-generated method stub

	}

}
