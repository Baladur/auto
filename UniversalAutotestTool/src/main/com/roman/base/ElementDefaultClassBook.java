package com.roman.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 19.11.2016.
 */
public class ElementDefaultClassBook {
    private static List<Class> classes;
    static {
        classes = new ArrayList<>();
        classes.add(TextField.class);
        classes.add(TableObject.class);
        classes.add(TableRow.class);
        classes.add(TableColumn.class);
        classes.add(Button.class);
    }

    public static List<Class> book() {
        return classes;
    }
}
