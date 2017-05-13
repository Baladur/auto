package com.roman;

import com.roman.base.Context;
import com.roman.base.ExpressionProcessor;
import com.roman.base.Interpreters;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;

import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
	    try {
	        String path = "test1.test";
            new ExpressionProcessor("elements.json", ".", Arrays.asList(".")).process(path);
        } catch (ProcessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            Operator value = Interpreters.VALUE.interpret("(5 + 4) / 2", new Context());
//        } catch (ProcessException e) {
//            e.printStackTrace();
//        }
    }
}
