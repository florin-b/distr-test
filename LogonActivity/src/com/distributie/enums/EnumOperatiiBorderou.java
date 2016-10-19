package com.distributie.enums;

public enum EnumOperatiiBorderou {
	GET_BORDEROURI("getBorderouri"), GET_FACTURI_BORDEROU("getFacturiBorderou"), GET_ARTICOLE_BORDEROU(
			"getArticoleBorderou");

	private String numeComanda;

	EnumOperatiiBorderou(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getNumeComanda() {
		return numeComanda;
	}

}
