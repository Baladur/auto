package com.roman.rzhd.elements;

import com.roman.base.BaseElement;

public enum Button implements BaseElement {
	CALENDAR_RIGHT,
	SUBMIT,
	;

	public String getName() { return name(); }
}