package com.distributie.dialog;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.distributie.adapters.PozitieAdapter;
import com.distributie.listeners.PozitieLivrareListener;
import com.distributie.view.R;

public class PozitieLivrareDialog extends Dialog {

	private ListView listViewPozitii;
	private Context context;
	private PozitieLivrareListener pozitieListener;

	public PozitieLivrareDialog(Context context) {
		super(context);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pozitie_livrare_dialog);

		setupLayout();

	}

	public void setPozitiiLivrare(List<String> pozitiiLivrare) {
		String[] arrayPozitii = pozitiiLivrare.toArray(new String[pozitiiLivrare.size()]);
		listViewPozitii.setAdapter(new PozitieAdapter(context, arrayPozitii));

	}

	private void setupLayout() {
		listViewPozitii = (ListView) findViewById(R.id.listPozitii);

		listViewPozitii.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);

				if (pozitieListener != null)
					pozitieListener.pozitieSelected(item);

				dismiss();

			}

		});

	}

	public void setPozitieLivrareListener(PozitieLivrareListener listener) {
		this.pozitieListener = listener;
	}
}
