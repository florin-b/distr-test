package com.distributie.model;

public interface BorderouriDAO {
	public void getBorderouri(String codSofer, String tipOp, String interval);
	public void getFacturiBorderou(String nrBorderou, String tipBorderou);
	public void getArticoleBorderou(String nrBorderou, String codClient);
}
