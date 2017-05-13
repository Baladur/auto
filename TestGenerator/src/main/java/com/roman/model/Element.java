package com.roman.model;

import java.util.List;

/**
 * Created by roman on 30.04.2017.
 */
public class Element {
    public String name;
    public List<String> paths;
    public String toString() {
        return String.format("\tName: [%s], paths length: [%d]\n", name, paths.size());
    }
}
