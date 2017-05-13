package com.roman.util;

/**
 * Created by roman on 26.03.2017.
 */
public class MessageFormatter {
    public static String errorLine(InputTestReader reader) {
        return String.format("Line %d", reader.getLineCount());
    }
}
