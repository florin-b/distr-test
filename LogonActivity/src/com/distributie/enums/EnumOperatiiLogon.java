package com.distributie.enums;

public enum EnumOperatiiLogon {
	GET_COD_SOFER("getCodSofer"), USER_LOGON("userLogon");

	private String numeComanda;

	EnumOperatiiLogon(String numeComanda) {
		this.numeComanda = numeComanda;
	}

	public String getComanda() {
		return numeComanda;
	}

}
