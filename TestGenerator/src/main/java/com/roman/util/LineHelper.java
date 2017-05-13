package com.roman.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by roman on 23.04.2017.
 */
public class LineHelper {
    private static final Pattern INDENT = Pattern.compile("^(\\t*)");

    public static int countOfIndents(String line) {
        Matcher m = INDENT.matcher(line);
        if (m.find()) {
            return m.group(1).length();
        }
        return 0;
    }
}
