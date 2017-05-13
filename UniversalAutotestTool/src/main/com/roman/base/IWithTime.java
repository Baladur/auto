package com.roman.base;

/**
 * Created by user on 22.02.2017.
 */
public interface IWithTime extends IOrElse {
    IOrElse duringSeconds(int seconds);
    IOrElse duringMinutes(int minutes);
    IOrElse duringHours(int hours);
}
