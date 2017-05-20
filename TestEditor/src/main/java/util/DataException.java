package util;

/**
 * Created by roman on 07.05.2017.
 */
public class DataException extends Exception {
    public DataException() {
        super();
    }

    public DataException(String message) {
        super(message);
    }

    public DataException(String message, Throwable t) {
        super(message, t);
    }
}
