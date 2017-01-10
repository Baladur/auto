package com.roman.base;

/**
 * Created by roman on 27.11.2016.
 */
public enum Time {
    HOURS("HOURS", 1000 * 60 * 60),
    MINUTES("MINUTES", 1000 * 60),
    SECONDS("SECONDS", 1000);

    Time(String measure, int multiplier) {

        this.measure = measure;
        this.multiplier = multiplier;
    }

    public String getMeasure() { return measure; }
    public int getMultiplier() { return multiplier; }

    private String measure;
    private int multiplier;
}
