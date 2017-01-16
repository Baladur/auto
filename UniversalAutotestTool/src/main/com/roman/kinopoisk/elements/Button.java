package com.roman.kinopoisk.elements;

import com.roman.base.BaseElement;

public enum Button implements BaseElement {
	SEARCH_BTN,
	BEST_MOVIES,
	MORE_FACTS,
	FIRST_ACTOR,
	TEXTS,
	SHOW_ALL,
	INFO_DIRECTOR,
	DIRECTOR,
	IN_MAIN,
	MOST_WANTED,
	;

	public String getName() { return name(); }
}