package com.roman.kinopoisk.elements;

import com.roman.base.BaseElement;

public enum Button implements BaseElement {
	SEARCH_BTN,
	BEST_MOVIES,
	MORE_FACTS,
	SHOW_ALL,
	DIRECTOR,
	MOST_WANTED,
	;

	public String getName() { return name(); }
}