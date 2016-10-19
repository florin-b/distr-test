package com.distributie.enums;

public enum EnumTipEtapa {
	SFARSIT_INCARCARE("SF_INC"), START_BORD("START"), SOSIRE("SOSIRE"), STOP_BORD("STOP");

	private String etapa;

	EnumTipEtapa(String etapa) {
		this.etapa = etapa;
	}

	public String toString() {
		return etapa;
	}

}
