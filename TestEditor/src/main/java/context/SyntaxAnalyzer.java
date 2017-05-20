package context;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import model.Hint;
import model.Type;
import util.Messages;
import util.SyntaxAnalyzeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Created by roman on 22.10.2016.
 */
@Slf4j
@Getter
public class SyntaxAnalyzer {
    private static final String GRAMMAR_PATH = "test-script.txt";
    private static final String STARTING_ELEMENT = "<MAIN>";
    private static final char END_CHAR = '\n';

    private Grammar grammar;
    private ContextState contextState;
    private List<HistoryRow> history = new ArrayList<>();
    private List<InputEventListener> inputEventListeners = new ArrayList<>();
    private List<ContextEndEventListener> contextEndEventListeners = new ArrayList<>();
    private List<MovePositionEventListener> movePositionEventListeners = new ArrayList<>();

    public void addInputEventListener(InputEventListener listener) {
        inputEventListeners.add(listener);
    }

    public void addContextEndEventListener(ContextEndEventListener listener) {
        contextEndEventListeners.add(listener);
    }
    public void addMovePositionEventListener(MovePositionEventListener listener) {
        movePositionEventListeners.add(listener);
    }

    public SyntaxAnalyzer() throws IOException {
        this(GRAMMAR_PATH, STARTING_ELEMENT, Arrays.asList());
    }

    public SyntaxAnalyzer(String grammarPath, String startElement, List<Type> types) throws IOException {
        grammar = new Grammar(grammarPath, startElement, types);
        contextState = new ContextState();
        initStack();
    }

    public void processInput(Hint hint) throws SyntaxAnalyzeException {
        passNonterminals(hint);
        readTerminals();
    }

    public Set<Hint> getCurrentHints() {
        return grammar.getHintsOf(contextState.getCurrentStack().get());
    }

    public void initStack() {
        contextState.setCurrentStack(new HStack(grammar.getCode(END_CHAR), grammar.getStartElement()));
    }

    private void passNonterminals(Hint hint) throws SyntaxAnalyzeException {
        HStack stack = contextState.getCurrentStack();
        List<Integer> path = hint.getPath();
        if (!path.get(0).equals(stack.get())) {
            throw new SyntaxAnalyzeException(Messages.SYNTAX_ANALYZE_ERROR);
        }
        //log.info("Stack before move: {}.", stackToString(stack));
        for (int i = 1; i < path.size(); i++) {
            Integer X = stack.get();

            if (grammar.isTerminal(X)) {
                throw new SyntaxAnalyzeException(Messages.SYNTAX_ANALYZE_ERROR);
            }
            Integer nt = path.get(i);
            Rule nextRule = grammar.getRules(rule -> rule.from == X && rule.to[0] == nt).get(0);
            stack.push(nextRule.to);
            //log.info("Pass nonterminals. Stack: {}.", stackToString(stack));
        }
        Rule nextRule = grammar.getRules(
                rule -> rule.from == stack.get() && rule.to[0].equals(grammar.getCode(hint.getLabel().charAt(0)))).get(0);
        stack.push(nextRule.to);
        //log.info("Final stack: {}.", stackToString(stack));
    }

    private void readTerminals() {
        HStack stack = contextState.getCurrentStack();
        Integer X;
        do {
            X = stack.get();
            if (!grammar.isTerminal(X)) {
                break;
            }
            stack.removeLast();
            if (!grammar.isEmptyCode(X)) {
                if (X == grammar.getCode(END_CHAR)) {
                    emitContextEndEvent();
                    initStack();
                    break;
                } else {
                    contextState.moveInputPointer();
                    emitChar(grammar.getTerminal(X));
                }
            }
            //log.info("Read terminals. Stack: {}.", stackToString(stack));
        } while (grammar.isTerminal(X));
        contextState.clearInput();
    }

    public void moveToCurrentPosition() {
        movePositionEventListeners.forEach(listener -> listener.moveToPosition(contextState.getCurrentPosition()));
    }

    private void emitChar(Character character) {
        //log.info("Emitting char {}.", character);
        inputEventListeners.forEach(listener -> listener.handle(character));
    }

    private void emitContextEndEvent() {
        //log.info("Emitting context end event.");
        contextEndEventListeners.forEach(listener -> listener.handle());
    }

    /**
     * One iteration of predictive syntax analyze.
     *
     * @return
     */
//    private boolean step(Integer nonterminal, ContextState state) {
//        HStack stack = state.getCurrentStack();
//        String input = state.getCurrentInput();
//        Integer X = stack.get();
//        Integer a = grammar.getCode(state.getInputChar());
//        //HistoryRow historyRow = new HistoryRow(stackToString(stack), input.substring(ptr, input.length()).replace("\n", "\\n"), "");
//        //PrettyPrinter.printHistoryRow(historyRow);
//        if (grammar.isTerminal(X) || X == grammar.getCode(END_CHAR)) {
//            if (X == a) {
//                if (X == grammar.getCode(END_CHAR)) {
//                    return true;
//                }
//                stack.removeLast();
//                state.moveInputPointer();
//                state.emitChar(grammar.getTerminal(a));
//            } else {
//                if (grammar.isEmptyCode(X)) {
//                    stack.removeLast();
//                } else {
//                    //process error
//                    String errorMsg = "Ошибка синтаксиса в позиции " + state.getPtr() + ". Символ '" + a + "' внезапен.";
//                    processError(errorMsg);
//                    historyRow.msg = errorMsg;
//                    boolean found = false;
//                    for (int i = stack.size()-1; i >= 0; i--) {
//                        if (stack.get(i) == a) {
//                            found = true;
//                            break;
//                        }
//                    }
//                    if (found && stack.size() > 0) {
//                        do {
//                            stack.removeLast();
//                        } while (stack.get() != a);
//                        stack.removeLast();
//                        state.moveInputPointer();
//                    } else {
//                        state.moveInputPointer();
//                    }
//                }
//
//            }
//        } else {
//            Rule nextRule = grammar.getRules(rule -> rule.from == X && rule.to[0] == nonterminal).get(0);
//            stack.push(nextRule.to);
////            Rule production = grammar.getRule(X, a);
////            if (production != null) {
////                if (grammar.isSynch(production)) {
////                    String errorMsg = "Ошибка синтаксиса в позиции " + ptr + ". Символ '" + a + "' внезапен.";
////                    processError(errorMsg);
////                    historyRow.setMsg(errorMsg);
////                    ptr[0]++;
////                    if (!grammar.getFirstOf(X).contains(input.charAt(ptr))) {
////                        stack.removeLast();
////                    } else {
////                        errorMsg = a + " есть в FIRST(" + X + ")";
////                    }
////
////                } else {
////                    if (grammar.isLeadingToEmpty(production)) {
////                        stack.removeLast();
////                    } else {
////                        stack.push(production.to);
////                    }
////                    System.out.println(production.toString());
////                }
////
////            } else {
////                processError("ops");
////            }
//        }
//        history.add(historyRow);
//        return false;
//    }
//
//    private void printHistory() {
//        PrettyPrinter.printHistoryHeader();
//        for (HistoryRow row : history) {
//            PrettyPrinter.printHistoryRow(row);
//        }
//    }

    private void processError(String s) {
        System.out.println(s);
    }

    private String stackToString(HStack stack) {
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, stack.size())
                .forEach(i -> {
                    String stackElement = "";
                    Integer stackElementCode = stack.get(i);
                    if (grammar.isTerminal(stackElementCode)) {
                        if (grammar.isEmptyCode(stackElementCode)) {
                            stackElement = "_e_";
                        } else {
                            stackElement = grammar.getTerminal(stackElementCode) == '\n' ? "\\n" : grammar.getTerminal(stackElementCode).toString();
                        }
                    } else {
                        stackElement = grammar.getNonterminal(stackElementCode);
                    }
                    sb.append(stackElement);
                });
        return sb.toString();
    }

    public void addAssignedVariableRule(String variableName, String type) {
        grammar.addRule("<" + type.toUpperCase() + "_VARIABLE", variableName);
    }

    public void recalculatePredictions() {
        grammar.calculatePredictionTable();
    }

}
