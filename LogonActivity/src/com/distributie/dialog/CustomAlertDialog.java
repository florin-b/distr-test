package com.distributie.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.distributie.enums.EnumOpConfirm;
import com.distributie.listeners.AlertDialogListener;
import com.distributie.view.R;

public class CustomAlertDialog extends Dialog {

	private TextView textAlert;
	private Button btnOk, btnCancel;
	private AlertDialogListener listener;
	private EnumOpConfirm tipOperatie;
	private String alertText;

	public CustomAlertDialog(Context context) {
		super(context);

		setContentView(R.layout.alert_dialog);
		setTitle("Atentie!");

		setupLayout();

	}

	public void setTipOperatie(EnumOpConfirm tipOperatie) {
		this.tipOperatie = tipOperatie;
	}

	public void setAlertText(String alertText) {
		this.alertText = alertText;
	}

	private void setupLayout() {
		textAlert = (TextView) findViewById(R.id.textAlert);

		btnOk = (Button) findViewById(R.id.btnOk);
		setListenerBtnOk();

		btnCancel = (Button) findViewById(R.id.btnCancel);
		setListenerBtnCancel();

	}

	public void setOKButtonText(String buttonText) {
		btnOk.setText(buttonText);
	}

	public void setCancelButtonText(String buttonText) {
		btnCancel.setText(buttonText);
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
				if (listener != null)
					listener.alertDialogOk(tipOperatie);
				dismiss();

			}
		});
	}

	private void setListenerBtnCancel() {
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (listener != null)
					listener.alertDialogCancel(tipOperatie);
				dismiss();

			}
		});
	}

	public void setAlertDialogListener(AlertDialogListener listener) {
		this.listener = listener;
	}

}
