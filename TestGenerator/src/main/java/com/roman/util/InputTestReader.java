package com.roman.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by roman on 29.03.2017.
 */
@Slf4j
@Getter
public class InputTestReader implements Closeable {
    private BufferedReader reader;
    private StreamTokenizer st;
    private InputTestReaderListener listener;
    private int lineCount = 0;
    private int columnCount = 0;
    private String buffer = "";
    private String nextLine;
    private int logicIndents = 0;

    private static final Pattern ID_PATTERN = Pattern.compile("[^\\d][^\\s]*");

    public InputTestReader(BufferedReader reader) {
        this.reader = reader;
        this.st = new StreamTokenizer(reader);
    }

    public void addListener(InputTestReaderListener listener) {
        this.listener = listener;
    }

    public String readLine() throws IOException, ProcessException {
        buffer = nextLine();
        columnCount = 0;
        lineCount++;
        listener.onNewLine();
        listener.onColumnCountChanged(columnCount);
        if (logicIndents > 0) {
            assertIndents(logicIndents);
        }
        nextLine = null;
        st = new StreamTokenizer(new StringReader(buffer));
        st.ordinaryChar(':');
        st.ordinaryChar('_');
        st.wordChars('_', '_');
        return buffer;
    }

    public int getColumn(String text) {
        return buffer.indexOf(text) + 1;
    }

    public int getLineCount() {
        return lineCount;
    }

    public boolean isEOF() throws IOException {
        return nextLine() == null;
    }

    public boolean isEndOfBlock() throws IOException, ProcessException {
        if (isEOF()) {
            throw new ProcessException(this, "Script file is ended before end of block.");
        }
        int nextLineIndents = LineHelper.countOfIndents(nextLine());
        if (logicIndents > nextLineIndents) {
            endBlock();
            return true;
        }
        return false;
    }

    public boolean isEndOfLine() throws ProcessException, IOException {
        if (isEOF()) {
            return true;
        }
        return columnCount == buffer.length();
    }

    public void assertEndOfLine() throws IOException, ProcessException {
        if (!isEndOfLine()) {
            listener.onError("Expected end of line.");
        }
    }

    public String nextLine() throws IOException {
        nextLine = nextLine == null ? reader.readLine() : nextLine;
        return nextLine;
    }

    private String nextToken() throws IOException {
        int tokenType = st.nextToken();
        String token = null;
        switch (tokenType) {
            case StreamTokenizer.TT_NUMBER:
                double d = st.nval;
                if (d - (int)d == 0) {
                    token = Integer.valueOf((int)d).toString();
                } else {
                    token = Double.valueOf(d).toString();
                }
                break;
            case (int)'"': token = '"' + st.sval + '"'; break;
            case StreamTokenizer.TT_WORD: token = st.sval; break;
            case StreamTokenizer.TT_EOF: token = ""; break;
            default: token = Character.valueOf((char)st.ttype).toString();
        }
        return token;
    }

    public String readToken() throws IOException, ProcessException {
        String nextToken = nextToken();
        columnCount += nextToken.length();
        columnCount += isEndOfLine() ? 0 : 1; //skip space after token if it is not last token in line
        listener.onColumnCountChanged(columnCount);
        log.info("Read token: {}.", nextToken);
        return nextToken;
    }

    public String readToken(String... expectedTokens) throws IOException, ProcessException {
        String token = readToken();
        if (!Stream.of(expectedTokens).anyMatch(expected -> token.equals(expected))) {
            listener.onError(String.format("Expected '%s'.", expectedTokens.toString()));
        }
        return token;
    }

    public String readPrefix(String... expectedPrefixes) throws IOException, ProcessException {
        String prefix = nextToken();
        columnCount += prefix.length(); //don't count space after token
        listener.onColumnCountChanged(columnCount);
        if (!Stream.of(expectedPrefixes).anyMatch(expected -> prefix.equals(expected))) {
            listener.onError(String.format("Expected '%s'.", expectedPrefixes.toString()));
        }
        return prefix;
    }

    public String readAllTokensInLine() throws IOException, ProcessException {
        String result = buffer.substring(columnCount, buffer.length());
        columnCount = buffer.length();
        listener.onColumnCountChanged(columnCount);
        return result;
    }

    public String readUntilOneOf(String... variants) throws IOException, ProcessException {
        int index = -1;
        int columnShift = 0;
        for (String var : variants) {
            index = buffer.indexOf(var);
            if (index > columnCount) {
                do {
                    columnShift += nextToken().length() + 1;
                } while (columnCount + columnShift < index);
                String result = buffer.substring(columnCount, index - 1);
                columnCount = index;
                listener.onColumnCountChanged(columnCount);
                return result;
            }
        }
        return readAllTokensInLine();
    }

    public String readID() throws IOException, ProcessException {
        String id = readToken();
        if (!ID_PATTERN.matcher(id).matches()) {
            listener.onError(String.format("Wrong id format of token '%s'.", id));
        }
        return id;
    }

    public String readStringConstant() throws IOException, ProcessException {
        String constant = readToken();
        if (!constant.startsWith("\"")) {
            listener.onError("Name of step must be string constant only.");
        }
        return constant;
    }

    public int readInt() throws IOException, ProcessException {
        String number = readToken();
        int result = 0;
        try {
            result = Integer.parseInt(number);
        } catch (NumberFormatException nfe) {
            listener.onError(String.format("Expected integer value, got '%s' instead.", number));
        }
        return result;
    }

    public void readEmptyLine() throws IOException, ProcessException {
        if (readLine().length() > 0) {
            listener.onError("Expected empty line.");
        }
    }

    public int startBlock() {
        log.info("Start of block.");
        return ++logicIndents;
    }


    public int endBlock() {
        log.info("End of block.");
        if (logicIndents > 0) {
            logicIndents--;
        }
        return logicIndents;
    }

    public void assertIndents(int indents) throws IOException, ProcessException {
        int actualIndents = LineHelper.countOfIndents(Optional.ofNullable(buffer).orElse(nextLine()));
        if (actualIndents != indents) {
            throw new ProcessException(String.format("Indent error: expected %d indents, read %d indents.", logicIndents, actualIndents));
        }
        columnCount += actualIndents;
        listener.onColumnCountChanged(columnCount);
    }


    @Override
    public void close() throws IOException {
        reader.close();
    }


}
