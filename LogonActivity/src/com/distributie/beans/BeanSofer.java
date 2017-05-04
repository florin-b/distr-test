package com.distributie.beans;

public class BeanSofer {
	private String nume;
	private String filiala;
	private String codTableta;

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getFiliala() {
		return filiala;
	}

	public void setFiliala(String filiala) {
		this.filiala = filiala;
	}

	public String getCodTableta() {
		return codTableta;
	}

	public void setCodTableta(String codTableta) {
		this.codTableta = codTableta;
	}

	@Override
	public String toString() {
		return "BeanSofer [nume=" + nume + ", filiala=" + filiala + ", codTableta=" + codTableta + "]";
	}

}
