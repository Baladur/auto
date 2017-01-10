package com.roman;

import sun.rmi.runtime.Log;

import java.io.*;
import java.util.List;

/**
 * Created by roman on 27.11.2016.
 */
public class CodeGenerator {
    public static void process(String dir, String testName, List<Step> steps) {
        File fileIn = new File(dir + testName + ".java");
        File fileOut = new File(dir + "tmp" + testName + ".java");
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(fileIn));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOut), "UTF-8"));
            String line = "";
            int stepIndex = 0;
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
                if (stepIndex < steps.size()) {
                    Logger.printLine(String.format("Current line = %s", line));
                    Logger.printLine(String.format("step %d:\n%s", steps.get(stepIndex).getNumber(), steps.get(stepIndex).evaluate()));
                    if (steps.get(stepIndex).equals(line)) {
                        Logger.printLine("step is equal to line");
                        Step step = steps.get(stepIndex);
                        String code = step.evaluate();
                        bw.write(code);
                        stepIndex++;
                    } else {
                        Logger.printLine("step is not equal to line");
                    }
                }

            }
            br.close();
            fileIn.delete();
            bw.close();
            fileOut.renameTo(new File(dir + testName + ".java"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
