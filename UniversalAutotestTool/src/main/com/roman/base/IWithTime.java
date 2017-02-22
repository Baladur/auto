package com.roman.base;

/**
 * Created by user on 22.02.2017.
 */
public interface IWithTime extends IEnd {
    IEnd withSeconds(int seconds);
    IEnd withMinutes(int minutes);
    IEnd withHours(int hours);
}
