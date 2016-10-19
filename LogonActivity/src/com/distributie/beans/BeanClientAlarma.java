package com.distributie.beans;

public class BeanClientAlarma {

	private String nume;
	private String codBorderou;
	private String codClient;
	private String codAdresa;

	public String getCodBorderou() {
		return codBorderou;
	}

	public void setCodBorderou(String codBorderou) {
		this.codBorderou = codBorderou;
	}

	public String getCodClient() {
		return codClient;
	}

	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	public String getCodAdresa() {
		return codAdresa;
	}

	public void setCodAdresa(String codAdresa) {
		this.codAdresa = codAdresa;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	@Override
	public String toString() {
		return "BeanClientAlarma [nume=" + nume + ", codBorderou=" + codBorderou + ", codClient=" + codClient + ", codAdresa=" + codAdresa + "]";
	}

}
