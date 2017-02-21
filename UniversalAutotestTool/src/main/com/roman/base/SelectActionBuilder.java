package com.roman.base;

/**
 * Created by roman on 21.02.2017.
 */
public class SelectActionBuilder implements IEnd, IMatch, IOption {

    private SelectAction actionInstance;

    public SelectActionBuilder(SelectAction action) {
        this.actionInstance = action;
    }

    @Override
    public void end() {
        actionInstance.execute();
        actionInstance = null;
    }

    @Override
    public IEnd contains(String text) {
        actionInstance.match(Match.Contain).optionValue(text);
        return this;
    }

    @Override
    public IEnd equals(String text) {
        actionInstance.match(Match.Equal).optionValue(text);
        return this;
    }

    @Override
    public IEnd startsWith(String text) {
        actionInstance.match(Match.StartWith).optionValue(text);
        return this;
    }

    @Override
    public IEnd endsWith(String text) {
        actionInstance.match(Match.EndWith).optionValue(text);
        return this;
    }

    @Override
    public IEnd option(String text) {
        actionInstance.optionValue(text);
        return this;
    }

    @Override
    public IMatch option() {
        return this;
    }
}
