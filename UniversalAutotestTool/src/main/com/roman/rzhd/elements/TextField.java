package com.roman.rzhd.elements;

import com.roman.base.BaseElement;

public enum TextField implements BaseElement {
	FROM,
	TO,
	;

	public String getName() { return name(); }
}