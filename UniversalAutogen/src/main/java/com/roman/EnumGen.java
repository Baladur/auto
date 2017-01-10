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
//            if (args.length != 5) {
//                throw new AutogenException(String.format("Number of params is %d! 5 params expected.", args.length));
//            }
            bw.write("Started\n");
            String elementClass = args[0];
            String elementName = args[1];
            String elementPath = args[2];
            String projectName = args[3];
            for (String arg : args) {
                bw.write("arg " + arg + "\n");
            }
            JSONToEnumConverter converter = JSONToEnumConverter.getInstance(projectName);
            bw.write("Converter is created\n");
            converter.process(elementClass, elementName, elementPath);
            bw.write("Converter processed successfully");
        } catch (AutogenException e) {
            try {
                bw.write(e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (ParseException e) {
            try {
                bw.write(e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            try {
                bw.write(e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (SAXException e) {
            try {
                bw.write(e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            try {
                bw.write(e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static {
        try {
            bw = new BufferedWriter(new FileWriter(new File("C:\\Users\\roman\\Documents\\auto\\log.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
