package com.roman.model;

import java.util.ArrayList;

/**
 * Created by roman on 24.04.2017.
 */
public class ErrorType extends Type {
    public ErrorType() {
        this.scriptName = "error";
        this.methods = new ArrayList<>();
    }
}
