package com.roman.base;

/**
 * Created by roman on 10.03.2017.
 */
public interface IOrElse extends IAction {
    public IAction orElse(Runnable orElseAction);
}
