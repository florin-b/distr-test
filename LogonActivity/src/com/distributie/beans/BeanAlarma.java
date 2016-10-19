package com.distributie.beans;

import com.distributie.enums.EnumTipAlarma;

public class BeanAlarma {

	private String codAlarma;
	private String numeAlarma;
	private EnumTipAlarma tipAlarma;
	private long idEveniment;
	private String codAdresa;

	public String getCodAlarma() {
		return codAlarma;
	}

	public void setCodAlarma(String codAlarma) {
		this.codAlarma = codAlarma;
	}

	public String getNumeAlarma() {
		return numeAlarma;
	}

	public void setNumeAlarma(String numeAlarma) {
		this.numeAlarma = numeAlarma;
	}

	public EnumTipAlarma getTipAlarma() {
		return tipAlarma;
	}

	public void setTipAlarma(EnumTipAlarma tipAlarma) {
		this.tipAlarma = tipAlarma;
	}

	public long getIdEveniment() {
		return idEveniment;
	}

	public void setIdEveniment(long idEveniment) {
		this.idEveniment = idEveniment;
	}

	public String getCodAdresa() {
		return codAdresa;
	}

	public void setCodAdresa(String codAdresa) {
		this.codAdresa = codAdresa;
	}

	@Override
	public String toString() {
		return "BeanAlarma [codAlarma=" + codAlarma + ", numeAlarma=" + numeAlarma + ", tipAlarma=" + tipAlarma + ", idEveniment=" + idEveniment
				+ ", codAdresa=" + codAdresa + "]";
	}

}
