package com.roman;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by user on 13.01.2017.
 */
public class BaseConfig implements Config {
    protected static final String CONFIG = "config";
    protected static final String PROJECT_NAME = "project-name";
    protected static final String ELEMENTS_PATH = "elements-path";
    protected static final String PACKAGE = "package";
    protected String dir;
    protected String packageName;

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDir() {
        return dir;
    }

    public String getPackageName() {
        return packageName;
    }
}
