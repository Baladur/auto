package com.roman;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

/**
 * Created by user on 13.01.2017.
 */
public class JSONConfig extends BaseConfig {
    private final static String prefix = "configJson = ";
    public static JSONConfig fromFile(String filePath, String projectName) throws AutogenException, IOException, ParseException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new AutogenException("Config file can not be located! Check it is in the same folder with autogen.jar");
        } else {
            JSONConfig jsonConfig = new JSONConfig();
            JSONParser parser = new JSONParser();
            FileInputStream fis = new FileInputStream(file);
            fis.skip(prefix.length());
            JSONObject configJson = (JSONObject) parser.parse(new InputStreamReader(fis));
            JSONArray configs = (JSONArray)configJson.get("configs");

            configs.stream()
                    .filter(o -> {
                        JSONObject config = (JSONObject)o;
                        return config.get(PROJECT_NAME).equals(projectName);
                    })
                    .forEach(o -> {
                        JSONObject config = (JSONObject)o;
                        jsonConfig.setDir((String)config.get(ELEMENTS_PATH));
                        jsonConfig.setPackageName((String)config.get(PACKAGE));
                    });
            return jsonConfig;
        }
    }
}
