package com.roman.base;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Created by user on 22.02.2017.
 */
public class Waiter {
    public static void tryExecuting(Supplier action, int time, TimeUnit timeUnit) throws UniFrameworkException {
        Long seconds = timeUnit.toSeconds(time);
        Throwable actionException = null;
        for (int i = 0; i < seconds; i++) {
            try {
                action.get();
                return;
            } catch (Throwable t) {
                actionException = t;
            }
        }
        throw new UniFrameworkException(String.format("Could not execute action successfully during %d %s", time, timeUnit.toString()), actionException);
    }
}
