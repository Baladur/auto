package com.roman.util;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.util.stream.IntStream;

/**
 * Created by roman on 28.04.2017.
 */
public abstract class BaseTestWriter implements OutputTestWriter, Closeable {
    protected BufferedWriter bw;
    protected int logicIndents = 0;
    private StringBuilder sb = new StringBuilder();

    public BaseTestWriter(BufferedWriter bw) {
        this.bw = bw;
    }

    public void write(String str) {
        sb.append(str);
    }

    public void writeToken(String token) {
        sb.append(token).append(" ");
    }

    public void newLine() throws IOException {
        bw.write(sb.toString());
        sb.delete(0, sb.length());
        bw.newLine();
        IntStream.range(0, logicIndents).forEach(i -> write("\t"));
    }

    public void writeEmptyLine() throws IOException {
        newLine();
    }

    public void endInstruction() {
        sb.append(';');
    }

    @Override
    public void close() throws IOException {
        bw.close();
    }
}
