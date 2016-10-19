package com.distributie.enums;

public enum EnumMotivOprire {

	PANA("PANA", 1), PAUZA("PAUZA", 2), BLOCAJ_TRAFIC("BLOCAJ TRAFIC", 3), ALIMENTARE("ALIMENTARE", 4), CONVORBIRE_TELEFONICA(
			"CONVORBIRE TELEFONICA/CAUTARE CLIENT", 5), CONTROL_TRAFIC("CONTROL TRAFIC", 6);

	private String nume;
	private int cod;

	EnumMotivOprire(String nume, int cod) {
		this.nume = nume;
		this.cod = cod;
	}

	public String getNume() {
		return nume;
	}

	public int getCod() {
		return cod;
	}

}
