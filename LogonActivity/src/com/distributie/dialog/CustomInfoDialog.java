package com.distributie.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.distributie.listeners.InfoDialogListener;
import com.distributie.view.R;

public class CustomInfoDialog extends Dialog {

	private TextView textAlert;
	private Button btnOk;
	private String alertText;
	private InfoDialogListener infoListener;

	public CustomInfoDialog(Context context) {
		super(context);

		setContentView(R.layout.info_dialog);
		setTitle("Atentie!");

		setupLayout();

	}

	public void setInfoText(String alertText) {
		this.alertText = alertText;
	}

	private void setupLayout() {
		textAlert = (TextView) findViewById(R.id.textAlert);

		btnOk = (Button) findViewById(R.id.btnOk);
		setListenerBtnOk();

	}

	public void setInfoDialogListener(InfoDialogListener infoListener) {
		this.infoListener = infoListener;
	}

	@Override
	public void show() {
		textAlert.setText(alertText);
		super.show();
	}

	private void setListenerBtnOk() {
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (infoListener != null)
					infoListener.infoDialogTextRead();

				dismiss();

			}
		});
	}

}
