package util;

/**
 * Created by roman on 19.05.2017.
 */
public class SyntaxAnalyzeException extends Exception {
    public SyntaxAnalyzeException() {
        super();
    }

    public SyntaxAnalyzeException(String message) {
        super(message);
    }

    public SyntaxAnalyzeException(String message, Throwable t) {
        super(message, t);
    }
}
