package com.distributie.beans;

import java.util.List;

public class BeanEvenimentStop {

	private boolean evenimentSalvat;
	private long idEveniment;
	private List<BeanClientAlarma> clientiAlarma;



	public boolean isEvenimentSalvat() {
		return evenimentSalvat;
	}

	public void setEvenimentSalvat(boolean evenimentSalvat) {
		this.evenimentSalvat = evenimentSalvat;
	}

	public long getIdEveniment() {
		return idEveniment;
	}

	public void setIdEveniment(long idEveniment) {
		this.idEveniment = idEveniment;
	}

	public List<BeanClientAlarma> getClientiAlarma() {
		return clientiAlarma;
	}

	public void setClientiAlarma(List<BeanClientAlarma> clientiAlarma) {
		this.clientiAlarma = clientiAlarma;
	}

	@Override
	public String toString() {
		return "BeanEvenimentStop [evenimentSalvat=" + evenimentSalvat + ", idEveniment=" + idEveniment + ", clientiAlarma=" + clientiAlarma + "]";
	}

}
