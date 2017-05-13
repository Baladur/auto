package com.roman.kinopoisk.elements;

import com.roman.base.BaseElement;

public enum Button implements BaseElement {
	SEARCH_BTN,
	MOST_WANTED,
	CHANCE_BTN,
	RANDOM_MOVIE_BTN,
	RANDOM_FILM_NAME
	;

	public String getName() { return name(); }
}