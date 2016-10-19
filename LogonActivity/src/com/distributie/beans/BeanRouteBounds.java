package com.distributie.beans;

import com.google.android.gms.maps.model.LatLng;

public class BeanRouteBounds {

	private Address adresaDest;
	private LatLng pozMasina;

	public Address getAdresaDest() {
		return adresaDest;
	}

	public void setAdresaDest(Address adresaDest) {
		this.adresaDest = adresaDest;
	}

	public LatLng getPozMasina() {
		return pozMasina;
	}

	public void setPozMasina(LatLng pozMasina) {
		this.pozMasina = pozMasina;
	}

	@Override
	public String toString() {
		return "BeanRouteBounds [adresaDest=" + adresaDest + ", pozMasina=" + pozMasina + "]";
	}

}
