package com.roman.base;

/**
 * Created by roman on 19.11.2016.
 */
public class ClassResolver {
    public static Class resolve(BaseElement element) throws UniFrameworkException {
        for (Class clazz : ElementDefaultClassBook.book()) {
            if (element.getClass().getSimpleName().equals(clazz.getSimpleName())) {
                return clazz;
            }
        }
        throw new UniFrameworkException("No default class name matches user defined class name");
    }
}
