package com.distributie.view;

import java.util.HashMap;

import com.distributie.listeners.CustomSpinnerListener;

import android.view.View;
import android.widget.AdapterView;

public class CustomSpinnerClass implements
		android.widget.AdapterView.OnItemSelectedListener {

	private CustomSpinnerListener listener;

	public void setListener(CustomSpinnerListener listener) {
		this.listener = listener;
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		@SuppressWarnings("unchecked")
		HashMap<String, String> artMap = (HashMap<String, String>) arg0
				.getSelectedItem();

		if (listener != null) {
			listener.onSelectedSpinnerItem(arg0.getId(), artMap);
		}

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	

}
