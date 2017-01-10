package com.roman;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Created by roman on 12.11.2016.
 */
public class JSONToEnumConverter {
    private static JSONObject elements;
    private static JSONToEnumConverter converter = null;
    private String elementDir;
    private String elementPackage;
    private String prefix = "elementsJson = ";

    private JSONToEnumConverter(String projectName) throws IOException, ParseException, SAXException, ParserConfigurationException, AutogenException {
        File file = new File(JSONToEnumConverter.class.getProtectionDomain().getCodeSource().getLocation()
                .getFile());
        String path = file.getParent() + File.separator + "config.xml";
        XMLConfig config = XMLConfig.fromFile(path, projectName);
        elementDir = config.getDir() + File.separator;
        System.out.println(elementDir);
        elementPackage = config.getPackageName();
        System.out.println(elementPackage);
        File elementsFile = new File(elementDir + "elements.json");
        if (!elementsFile.exists()) {
            BufferedWriter bw = new BufferedWriter(new FileWriter(elementsFile));
            bw.write(prefix + "{\r\n\r\n}");
            bw.close();
        }
        readElementsFile(elementsFile);
    }

    public static JSONToEnumConverter getInstance(String projectName) throws IOException, ParseException, SAXException, ParserConfigurationException, AutogenException {
        if (converter == null) {
            converter = new JSONToEnumConverter(projectName);
        }

        return converter;
    }

    public void process(String elementClass, String elementName, String elementPath, String elementDir, String elementPackage) throws AutogenException, IOException {
        editElementsFile(elementClass, elementName, elementPath);
        writeToElementsFile(elementDir);
        generateCode(elementDir, elementPackage);
    }

    public void process(String elementClass, String elementName, String elementPath) throws AutogenException, IOException {
        process(elementClass, elementName, elementPath, elementDir, elementPackage);
    }

    public String getPath(String elementClass, String elementName) throws AutogenException {
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
        throw new AutogenException(String.format("Element with class '%s' and name '%s' was not found.", elementClass, elementName));
    }

    private void editElementsFile(String elementClass, String elementName, String elementPath) {
        boolean found = false;
        JSONObject foundElement = null;
        for (Object elementKey : elements.keySet()) {
            String elementKeyName = (String)elementKey;
            if (elementClass.equals(elementKeyName)) {
                found = true;
                foundElement = (JSONObject)elements.get(elementKeyName);
                break;
            }
        }
        if (foundElement == null) {
            foundElement = new JSONObject();
            elements.put(elementClass, foundElement);
        }
        foundElement.put(elementName, elementPath);
    }

    private void generateCode(String dir, String packageName) throws IOException, AutogenException {
        //generateBaseElementInterface(dir, packageName);
        for (Object elementKey : elements.keySet()) {
            String elementKeyName = (String)elementKey;
            JSONObject elementValue = (JSONObject)elements.get(elementKeyName);
            File enumFile = new File(String.format("%s%s.java", dir, elementKeyName));
            BufferedWriter bw = new BufferedWriter(new FileWriter(enumFile));
            bw.write(String.format("package %s;\r\n\r\n", packageName));
            bw.write("import com.roman.base.BaseElement;\r\n\r\n");
            bw.write(String.format("public enum %s implements BaseElement {\r\n", elementKeyName));
            for (Object key : elementValue.keySet()) {
                String keyStr = (String) key;
                bw.write(String.format("\t%s,\r\n", keyStr));
                if (elementValue.get(keyStr) instanceof JSONArray) {

                } else if (elementValue.get(keyStr) instanceof String) {
                    //bw.write(String.format("\"%s\"),\r\n", (String)elementValue.get(keyStr)));
                } else {
                    throw new AutogenException("Element value is nor array neither string");
                }
            }
            bw.write("\t;\r\n\r\n");
            bw.write(String.format("\tpublic String getName() { return name(); }\r\n"));
            bw.write("}");
            bw.close();
        }
    }

    private void readElementsFile(File elementsFile) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        FileInputStream fis = new FileInputStream(elementsFile);
        fis.skip(prefix.length());
        elements = (JSONObject) parser.parse(new InputStreamReader(fis));
        //fis.close();
    }

    private void writeToElementsFile(String dir) throws IOException {
        FileWriter fw = new FileWriter(dir + "elements.json");
        fw.write(prefix + elements.toJSONString());
        fw.flush();
        fw.close();
    }
    
    private void generateBaseElementInterface(String dir, String packageName) throws IOException {
        if (!new File(dir + "BaseElement.java").exists()) {
            File be = new File(dir + "BaseElement.java");
            BufferedWriter bw = new BufferedWriter(new FileWriter(be));
            bw.write(String.format("package %s;\r\n\r\n", packageName));
            bw.write("public interface BaseElement {\r\npublic String getPath();\r\n}");
            bw.close();
        }
    }


}
