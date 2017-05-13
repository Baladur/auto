package com.roman.base;

import lombok.Getter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.concurrent.TimeUnit;

/**
 * Base class for all types of interactions with web page.
 * It implements common methods and has fields necessary to child classes.
 *
 * @author ryakupov
 */
@Getter
public abstract class AbstractAction implements IWithTime {
    protected Driver driver;
    protected int time;
    protected TimeUnit timeUnit;
    protected Runnable orElseAction;

    public int getTime() {
        return time;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public AbstractAction(Driver driver) {
        this.driver = driver;
        this.time = 1;
        this.timeUnit = TimeUnit.SECONDS;
        this.orElseAction = () -> { throw new AssertionError(LogMessages.failAbstractAction(this)); };
    }

    @Override
    public IOrElse duringSeconds(int seconds) {
        time = seconds;
        timeUnit = timeUnit.SECONDS;
        return this;
    }

    @Override
    public IOrElse duringMinutes(int minutes) {
        time = minutes;
        timeUnit = timeUnit.MINUTES;
        return this;
    }

    @Override
    public IOrElse duringHours(int hours) {
        time = hours;
        timeUnit = timeUnit.HOURS;
        return this;
    }

    @Override
    public IAction orElse(Runnable orElseAction) {
        this.orElseAction = orElseAction;
        return this;
    }

    /**
     * Every implementation of {@code AbstractAction} must define whole logic in this method.
     */
    protected void execute() {
        throw new NotImplementedException();
    }

    /**
     * This method is called from user code and makes implicit calls of {@code execute()} through the {@code ActionExecuter}.
     */
    public void end() {
        ActionExecuter.tryExecute(this);
    }
}
