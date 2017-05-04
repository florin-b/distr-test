package com.distributie.pattern;

import java.util.List;

import com.distributie.beans.Articol;
import com.distributie.enums.EnumTipOperatie;

public interface ArticolePattern {
	public List<Articol> getTipArticole(List<Articol> listArticole, EnumTipOperatie tipOperatie);
}
