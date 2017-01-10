package com.roman.base;

import com.roman.AutogenException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

/**
 * Created by roman on 19.11.2016.
 */
public class PathReader {
    private static PathReader instance = null;
    private JSONObject elements;

    private PathReader(String packageName) throws UniFrameworkException, IOException, ParseException {
        File elementsFile = new File(getClass().getResource("/" + packageName.replace('.', '/') + "/elements.json").getFile());
        if (!elementsFile.exists()) {
            throw new UniFrameworkException("No element had been generated!");
        }
        readElementsFile(elementsFile);
    }

    public static PathReader getInstance(String packageName) throws ParseException, IOException, UniFrameworkException {
        if (instance == null) {
            instance = new PathReader(packageName);
        }

        return instance;
    }

    public String getPath(String elementClass, String elementName) throws UniFrameworkException {
        for (Object elementKey : elements.keySet()) {
            String elementKeyName = (String)elementKey;
            if (elementClass.equals(elementKeyName)) {
                JSONObject classObj = (JSONObject)elements.get(elementKeyName);
                for (Object eKey : classObj.keySet()) {
                    String strKey = (String)eKey;
                    if (elementName.equals(strKey)) {
                        return (String)classObj.get(strKey);
                    }
                }
            }
        }
        throw new UniFrameworkException(String.format("Element with class '%s' and name '%s' was not found.", elementClass, elementName));
    }

    private void readElementsFile(File elementsFile) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        elements = (JSONObject) parser.parse(new FileReader(elementsFile));
    }
}
