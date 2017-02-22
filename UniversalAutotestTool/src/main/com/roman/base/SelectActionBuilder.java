package com.roman.base;

import java.util.concurrent.TimeUnit;

/**
 * Created by roman on 21.02.2017.
 */
public class SelectActionBuilder implements IEnd, IMatch, IOption, IWithTime {

    private SelectAction actionInstance;

    public SelectActionBuilder(SelectAction action) {
        this.actionInstance = action;
    }

    @Override
    public void end() throws UniFrameworkException {
        Waiter.tryExecuting(() -> {
            actionInstance.execute();
            return null;
        }, actionInstance.getTime(), actionInstance.getTimeUnit());
        actionInstance = null;
    }

    @Override
    public IWithTime contains(String text) {
        actionInstance.match(Match.Contain).optionValue(text);
        return this;
    }

    @Override
    public IWithTime equals(String text) {
        actionInstance.match(Match.Equal).optionValue(text);
        return this;
    }

    @Override
    public IWithTime startsWith(String text) {
        actionInstance.match(Match.StartWith).optionValue(text);
        return this;
    }

    @Override
    public IWithTime endsWith(String text) {
        actionInstance.match(Match.EndWith).optionValue(text);
        return this;
    }

    @Override
    public IWithTime option(String text) {
        actionInstance.optionValue(text);
        return this;
    }

    @Override
    public IMatch option() {
        return this;
    }

    @Override
    public IEnd withSeconds(int seconds) {
        actionInstance.withTime(seconds, TimeUnit.SECONDS);
        return this;
    }

    @Override
    public IEnd withMinutes(int minutes) {
        actionInstance.withTime(minutes, TimeUnit.MINUTES);
        return this;
    }

    @Override
    public IEnd withHours(int hours) {
        actionInstance.withTime(hours, TimeUnit.HOURS);
        return this;
    }
}
