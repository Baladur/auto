package com.roman;

/**
 * Created by roman on 27.11.2016.
 */
public enum Time {
    HOURS("HOURS"),
    MINUTES("MINUTES"),
    SECONDS("SECONDS");

    Time(String measure) {
        this.measure = measure;
    }

    public String getMeasure() { return measure; }

    private String measure;
}
