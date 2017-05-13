package com.roman.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

/**
 * Created by roman on 05.03.2017.
 */
public @interface TestInfo {
    public String name();
    public String description();
}
