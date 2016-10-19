package com.distributie.enums;

public enum TipEveniment {

	NOU(0), ARHIVAT(1);

	private int codEveniment;

	TipEveniment(int codEveniment) {
		this.codEveniment = codEveniment;
	}

	public int getCod() {
		return codEveniment;
	}

}
