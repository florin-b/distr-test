package com.distributie.enums;

public enum EnumOperatiiAdresa {
	GET_ROUTE_BOUNDS("getRouteBounds");

	private String nume;

	EnumOperatiiAdresa(String nume) {
		this.nume = nume;
	}

	public String getNume() {
		return nume;
	}

}
