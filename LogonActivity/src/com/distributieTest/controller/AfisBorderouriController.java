package com.distributieTest.controller;

import com.distributieTest.view.AfisBorderouriView;

public class AfisBorderouriController {

	private AfisBorderouriView afisBorderouriView;

	public AfisBorderouriController(AfisBorderouriView afisBorderouriView) {
		this.afisBorderouriView = afisBorderouriView;
	}

	public void IncarcaBorderouriInit(String intervalAfisare) {
		afisBorderouriView.performIncarcaBorderouri(intervalAfisare);
	}

}
