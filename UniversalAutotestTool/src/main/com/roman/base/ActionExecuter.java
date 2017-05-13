package com.roman.base;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * This class executes actions.
 *
 * @author ryakupov
 */
public class ActionExecuter {
    public static void tryExecute(AbstractAction action) {
        Long executionTimeMillis = action.getTimeUnit().toMillis(action.getTime());
        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < executionTimeMillis) {
            try {
                action.execute();
                return;
            } catch (AssertionError error) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Throwable t) {
                throw new AssertionError(t);
            }
        }
        action.getOrElseAction().run();
    }
}
