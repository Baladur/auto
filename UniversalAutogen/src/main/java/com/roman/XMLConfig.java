package com.roman;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by roman on 19.11.2016.
 */
public class XMLConfig extends BaseConfig {

    public static XMLConfig fromFile(String filePath, String projectName) throws AutogenException, ParserConfigurationException, IOException, SAXException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new AutogenException("Config file can not be located! Check it is in the same folder with autogen.jar");
        } else {
            XMLConfig xmlConfig = new XMLConfig();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getDocumentElement().getElementsByTagName(CONFIG);
            Node config = null;
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element)node;
                    if (e.getAttribute(PROJECT_NAME).equalsIgnoreCase(projectName)) {
                        NodeList children = node.getChildNodes();
                        boolean foundDir = false;
                        boolean foundPackage = false;
                        for (int j = 0; j < children.getLength(); j++) {
                            Node child = children.item(j);
                            if (child.getNodeName().equals(ELEMENTS_PATH)) {
                                xmlConfig.setDir(child.getChildNodes().item(0).getNodeValue());
                                foundDir = true;
                            }
                            if (child.getNodeName().equals(PACKAGE)) {
                                xmlConfig.setPackageName(child.getChildNodes().item(0).getNodeValue());
                                foundPackage = true;
                            }
                        }
                        if (!foundDir || !foundPackage) {
                            throw new AutogenException("Error parsing config.xml! Invalid file structure!");
                        }
                    }
                }
            }
            return xmlConfig;
        }
    }

}
