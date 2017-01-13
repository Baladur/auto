package com.roman.kinopoisk.elements;

import com.roman.base.BaseElement;

public enum TextField implements BaseElement {
	SEARCH,
	T1,
	;

	public String getName() { return name(); }
}