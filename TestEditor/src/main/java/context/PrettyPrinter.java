package context;

import java.util.stream.IntStream;

/**
 * Created by roman on 16.05.2017.
 */
public class PrettyPrinter {
    private static final int MAX_STACK_SIZE = 100;
    private static final int MAX_INPUT_SIZE = 100;
    private static final int MAX_MESSAGE_SIZE = 50;

    public static void printHistoryHeader() {
        printHistoryRow("Стек", "Вход", "Примечание");
    }

    public static void printHistoryRow(HistoryRow row) {
        printHistoryRow(row.getStack(), row.getInput(), row.getMsg());
    }

    public static void printHistoryRow(String stack, String input, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("|").append(stack);
        IntStream.range(0, MAX_STACK_SIZE - stack.length()).forEach(i -> sb.append(" "));
        sb.append("|").append(input);
        IntStream.range(0, MAX_INPUT_SIZE - input.length()).forEach(i -> sb.append(" "));
        sb.append("|").append(message);
        IntStream.range(0, MAX_MESSAGE_SIZE - message.length()).forEach(i -> sb.append(" "));
        System.out.println(sb.toString());
    }
}
