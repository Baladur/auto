package com.roman;

import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Created by roman on 12.11.2016.
 */
public class EnumGen {
    private static BufferedWriter bw;
    public static void main(String[] args) {
        try {
            String elementClass = args[0];
            String elementName = args[1];
            String elementPath = args[2];
            String projectName = args[3];
            JSONToEnumConverter converter = JSONToEnumConverter.getInstance(projectName);
            converter.process(elementClass, elementName, elementPath);
        } catch (AutogenException e) {

        } catch (ParseException e) {

        } catch (IOException e) {

        } catch (SAXException e) {

        } catch (ParserConfigurationException e) {

        } finally {

        }
    }
}
