package context;

/**
 * Created by roman on 30.10.2016.
 */
public class HistoryRow {
    public static final int MAX_STACK = 25;
    public static final int MAX_INPUT = 100;
    public static final int MAX_MSG = 60;

    public String stack;
    public String input;
    public String msg;

    public void setStack(String stack) {
        this.stack = stack;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStack() {
        return stack;
    }

    public String getInput() {
        return input;
    }

    public String getMsg() {
        return msg;
    }

    public HistoryRow(String pStack, final String pInput, String pMsg) {
        stack = pStack;
        input = pInput;
        msg = pMsg;
    }
}
