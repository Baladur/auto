package com.roman.model;

import java.util.List;

/**
 * This class has java type binding information. It is serializable in JSON.
 */
public class Type {
    public String scriptName;
    public String javaName;
    public String description;
    public List<Method> methods;

    public static final Type ERROR = new ErrorType();

    @Override
    public int hashCode() {
        return scriptName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o.hashCode() == this.hashCode();
    }

    @Override
    public String toString() {
        return scriptName;
    }

    public String getFinalName() {
        //cut package prefix
        String finalName = javaName.substring(javaName.lastIndexOf('.') + 1, javaName.length());

        return finalName;
    }


}
