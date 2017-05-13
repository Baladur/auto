package com.roman.interpretation;

import com.roman.base.Interpreters;
import com.roman.util.ProcessException;

import java.util.Optional;

/**
 * Created by roman on 07.04.2017.
 */
public class ChainedInterpreter<T extends Interpreter> implements Interpreter {
    private T nextResponsible;

    public T withNextResponsible(T interpreter) {
        nextResponsible = interpreter;
        return (T)this;
    }

    public T getNextResponsible() throws ProcessException {
        return Optional.ofNullable(nextResponsible).orElse((T)Interpreters.ERROR);
    }
}
