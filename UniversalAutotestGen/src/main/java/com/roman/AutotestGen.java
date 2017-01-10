package com.roman;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by roman on 27.11.2016.
 */
public class AutotestGen {

    public static void main(String[] args) {
        try {
            String test =
                    "1. go https://www.kinopoisk.ru/" + "\n" +
                            "2. set #SEARCH \"Большой куш\"" + "\n" +
                            "click #SEARCH" + "\n" +
                            "3. click link #FIRST_RESULT" + "\n" +
                            "wait 2 sec" + "\n";
            AutotestGen.process(args[0], args[1], args[2]);
        } catch (AutogenException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void process(String projectName, String testName, String testCase) throws AutogenException, ParserConfigurationException, SAXException, IOException {
        File file = new File(AutotestGen.class.getProtectionDomain().getCodeSource().getLocation()
                .getFile());
        String path = file.getParent() + File.separator + "config.xml";
        XMLConfig config = XMLConfig.fromFile(path, projectName);
        String[] lines = testCase.split("\n");
        BaseInterpretator bi = new BaseInterpretator(1);
        Logger.printLine(String.format("testcase:\n%s", testCase));
        for (String line : lines) {
            Logger.printLine(String.format("next line = %s", line));
            bi.interpretLine(line);
        }
        List<Step> steps = bi.getResultSteps();
        CodeGenerator.process(config.getTestsPath(), testName, steps);
    }
}
