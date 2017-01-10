package com.roman;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by roman on 27.11.2016.
 */
public class Logger {
    private static File logFile;
    static {
        File file = new File(AutotestGen.class.getProtectionDomain().getCodeSource().getLocation()
                .getFile());
        String logPath = file.getParent() + File.separator + "log.txt";
        logFile = new File(logPath);
    }

    public static void printLine(String line) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(logFile, true));
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
