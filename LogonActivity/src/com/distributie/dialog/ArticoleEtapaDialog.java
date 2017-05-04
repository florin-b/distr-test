package com.distributie.dialog;

import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.distributie.adapters.ArticoleEtapaAdapter;
import com.distributie.beans.Articol;
import com.distributie.beans.Etapa;
import com.distributie.enums.EnumOperatiiBorderou;
import com.distributie.enums.EnumTipOperatie;
import com.distributie.listeners.ArticoleEtapaListener;
import com.distributie.listeners.BorderouriDAOListener;
import com.distributie.model.BorderouriDAOImpl;
import com.distributie.model.ExceptionHandler;
import com.distributie.model.HandleJSONData;
import com.distributie.pattern.impl.ArticolePatternImpl;
import com.distributie.view.R;

public class ArticoleEtapaDialog extends Dialog implements BorderouriDAOListener {

	private ListView listViewArticole;
	private Context context;
	private Etapa etapa;
	private Button btnClose;
	private RadioButton radioInc, radioDesc;
	private List<Articol> listArticole;
	private ArticoleEtapaListener articoleListener;
	private TextView textInfo;
	private RadioGroup groupTipInc;

	public ArticoleEtapaDialog(Context context, Etapa etapa) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.context = context;
		this.etapa = etapa;
		setContentView(R.layout.articole_etapa_dialog);

		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(context));

		setupLayout();
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		getArticole();

	}

	private void setupLayout() {

		listViewArticole = (ListView) findViewById(R.id.listArticole);
		btnClose = (Button) findViewById(R.id.btnClose);
		setListenerCloseBtn();

		radioInc = (RadioButton) findViewById(R.id.radioIncarcare);
		radioDesc = (RadioButton) findViewById(R.id.radioDescarcare);
		groupTipInc = (RadioGroup) findViewById(R.id.radioTipOp);
		
		textInfo = (TextView) findViewById(R.id.textInfo);
		setItemsVisibility(false);

		setListenerRadioInc();
		setListenerRadioDesc();

	}

	private void setItemsVisibility(boolean isVisible) {
		if (isVisible) {
			groupTipInc.setVisibility(View.VISIBLE);
			textInfo.setVisibility(View.GONE);
		} else {
			groupTipInc.setVisibility(View.GONE);
			textInfo.setVisibility(View.VISIBLE);
		}
	}

	private void setListenerRadioInc() {
		radioInc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				filterArticoleEtapa(EnumTipOperatie.INCARCARE);

			}
		});
	}

	private void setListenerRadioDesc() {
		radioDesc.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				filterArticoleEtapa(EnumTipOperatie.DESCARCARE);

			}
		});
	}

	private void filterArticoleEtapa(EnumTipOperatie tipOperatie) {

		if (listArticole != null) {
			List<Articol> listArt = new ArticolePatternImpl().getTipArticole(listArticole, tipOperatie);
			listViewArticole.setAdapter(new ArticoleEtapaAdapter(context, listArt));
		}

	}

	private void setListenerCloseBtn() {
		btnClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (articoleListener != null)
					articoleListener.articoleEtapaClosed();
				dismiss();
			}
		});
	}

	public void setArticoleListener(ArticoleEtapaListener articoleListener) {
		this.articoleListener = articoleListener;
	}

	private void getArticole() {
		BorderouriDAOImpl bord = BorderouriDAOImpl.getInstance(context);
		bord.setBorderouEventListener(ArticoleEtapaDialog.this);
		bord.getArticoleBorderou(etapa.getDocument(), etapa.getCodClient(), etapa.getCodAdresaClient());
	}

	private void showArticole(String resultArticole) {

		HandleJSONData objListArticole = new HandleJSONData(context, resultArticole);
		this.listArticole = objListArticole.decodeJSONArticoleFactura();

		filterArticoleEtapa(EnumTipOperatie.DESCARCARE);
		setItemsVisibility(true);

	}

	@Override
	public void loadComplete(String result, EnumOperatiiBorderou methodName) {
		switch (methodName) {
		case GET_ARTICOLE_BORDEROU:
			showArticole(result);
			break;
		default:
			break;
		}

	}

}
