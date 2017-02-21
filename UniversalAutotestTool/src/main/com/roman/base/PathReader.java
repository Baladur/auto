package com.roman.base;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.roman.AutogenException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by roman on 19.11.2016.
 */
public class PathReader {
    private static PathReader instance = null;
    private Elements elements;

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

    public List<String> getPaths(String elementClass, String elementName) throws UniFrameworkException {
//        try {
//            JSONObject classObj = (JSONObject)elements.keySet()
//                    .stream()
//                    .filter(key -> key.equals(elementClass))
//                    .map(key -> elements.get(key))
//                    .findFirst()
//                    .orElseThrow(Throwable::new);
//            Object paths = classObj.keySet()
//                    .stream()
//                    .filter(name -> name.equals(elementName))
//                    .map(name -> classObj.get(name))
//                    .findFirst()
//                    .orElseThrow(Throwable::new);
//            if (paths instanceof JSONArray) {
//                return ((JSONArray)paths).iterator();
//            } else {
//                throw new Throwable(String.format("Paths for element '%s.%s' is not JSON array as expected", elementClass, elementName));
//            }
//        } catch (Throwable t) {
//            if (t.getMessage().startsWith("Paths")) {
//                throw new UniFrameworkException(t.getMessage());
//            }
//            throw new UniFrameworkException(String.format("Element with class '%s' and name '%s' was not found.", elementClass, elementName));
//        }
        try {
            List<Element> elementsByType = (List)Elements.class.getField(elementClass).get(elements);
            return elementsByType
                    .stream()
                    .filter(element -> element.name.equals(elementName))
                    .map(element -> element.paths)
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }

    private void readElementsFile(File elementsFile) throws UniFrameworkException {
//        JSONParser parser = new JSONParser();
//        elements = (JSONObject) parser.parse(new FileReader(elementsFile));
        try (BufferedReader br = new BufferedReader(new FileReader(elementsFile))) {
            elements = new Gson().fromJson(br, Elements.class);
        } catch (IOException ioe) {
            throw new UniFrameworkException(ioe.getMessage());
        }
    }

    private class Elements {
        private List<Element> Button;
        private List<Element> TextField;
        private List<Element> Select;
        private List<Element> Table;
        public String toString() {
            StringBuffer result = new StringBuffer();
            result.append("Elements:\n");
            result.append(Button.toString());
            result.append(TextField.toString());
            result.append(Select.toString());
            result.append(Table.toString());
            return result.toString();
        }
    }

    private class Element {
        private String name;
        private List<String> paths;
        public String toString() {
            return String.format("\tName: [%s], paths length: [%d]\n", name, paths.size());
        }
    }
}
