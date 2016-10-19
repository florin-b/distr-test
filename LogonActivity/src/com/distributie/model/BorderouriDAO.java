package com.distributie.model;

import com.distributie.enums.TipBorderou;

public interface BorderouriDAO {
	public void getBorderouri(String codSofer, String tipOp, String interval);
	public void getFacturiBorderou(String nrBorderou, TipBorderou tipBorderou);
	public void getArticoleBorderou(String nrBorderou, String codClient, String codAdresa);
}
