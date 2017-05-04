package com.distributie.pattern.impl;

import java.util.ArrayList;
import java.util.List;

import com.distributie.beans.Articol;
import com.distributie.enums.EnumTipOperatie;
import com.distributie.pattern.ArticolePattern;

public class ArticolePatternImpl implements ArticolePattern {

	@Override
	public List<Articol> getTipArticole(List<Articol> listArticole, EnumTipOperatie tipOperatie) {

		List<Articol> articole = new ArrayList<Articol>();

		for (Articol art : listArticole)
			if (art.getTipOperatiune().equals(tipOperatie))
				articole.add(art);

		return articole;
	}

}
