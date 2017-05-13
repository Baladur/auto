package com.roman.base;

import java.util.function.Supplier;

/**
 * This action is required when test must wait for some condition.
 *
 * @author ryakupov
 */
public class WaitAction extends AbstractAction {
    private Supplier<Boolean> condition;

    public WaitAction(Driver driver, Supplier<Boolean> condition) {
        super(driver);
        this.condition = condition;
        this.orElseAction = () -> { throw new AssertionError(LogMessages.failWaitAction(this)); };
    }

    public IWithTime orElse(Runnable orElseAction) {
        this.orElseAction = orElseAction;
        return this;
    }

    @Override
    protected void execute() {
        if (!condition.get()) {
            throw new AssertionError();
        }
    }


}
