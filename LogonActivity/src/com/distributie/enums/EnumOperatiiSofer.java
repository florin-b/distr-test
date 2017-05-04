package com.distributie.enums;

public enum EnumOperatiiSofer {
	GET_SOFERI("getSoferi");

	private String nume;

	EnumOperatiiSofer(String nume) {
		this.nume = nume;
	}

	public String getNume() {
		return nume;
	}

}
