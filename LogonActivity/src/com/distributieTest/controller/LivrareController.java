package com.distributieTest.controller;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


import com.distributieTest.model.InfoStrings;
import com.distributieTest.view.LivrareView;

public class LivrareController  {

	private LivrareView livrareView;

	public LivrareController(LivrareView livrareView) {
		this.livrareView = livrareView;
		addEventListener();

	}

	private void addEventListener() {
		livrareView.addEventListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (!InfoStrings.isGPSEnabled(v.getContext())) {
						InfoStrings.showGPSDisabledAlert(v.getContext());
					} else {
						livrareView.buttonActionUp();
					}

					return true;
				case MotionEvent.ACTION_UP:
					livrareView.buttonActionDown();
					return true;

				}

				return false;
			}
		});
	}

	

}
