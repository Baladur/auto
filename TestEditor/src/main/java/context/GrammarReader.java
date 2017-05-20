package context;

import model.Method;
import model.MethodParameter;
import model.Type;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 15.05.2017.
 */
public class GrammarReader implements Closeable {
    private static final String END_SYMBOL = "<END>";

    private BufferedReader br;
    private List<Type> typeBindings;
    private Coder coder;
    private List<Rule> rules;

    public Coder getCoder() { return coder; }

    public List<Rule> getRules() {
        return rules;
    }

    public GrammarReader(BufferedReader br) {
        this.br = br;
        coder = new Coder();
        rules = new ArrayList<>();
    }

    public GrammarReader(BufferedReader br, List<Type> bindings) {
        this(br);
        this.typeBindings = bindings;
    }

    public void read() throws IOException {
        String line = "";

        //read grammar file line by line
        while ((line = br.readLine()) != null) {
            if (line.length() == 0) {
                break;
            }
            String[] fromTo = line.split(" - ");
            int fromId = coder.getNonterminalCode(fromTo[0]);

            //possible branches
            String[] tos = fromTo[1].split("\\|");

            String lastNonterminal;

            //iterate over branches
            for (String to : tos) {
                List<Integer> toIds = new ArrayList<>();
                if (to.length() == 0) {
                    //empty character is a possible branch
                    toIds.add(Coder.EMPTY_CODE);
                }
                for (int i = 0; i < to.length(); i++) {
                    char currentChar = to.charAt(i);
                    int closingBracket = to.indexOf('>', i);
                    if (currentChar == '<' && closingBracket > 0) {
                        //found nonterminal
                        lastNonterminal = to.substring(i, closingBracket + 1);

                        //<END> nonterminal is just newline character, put it to terminals list
                        if (lastNonterminal.equals(END_SYMBOL)) {
                            toIds.add(coder.getTerminalCode('\n'));
                            i += END_SYMBOL.length() - 1;
                            continue;
                        }
                        toIds.add(coder.getNonterminalCode(lastNonterminal));
                        //skip nonterminal
                        i += lastNonterminal.length() - 1;
                    } else {
                        //found terminal
                        toIds.add(coder.getTerminalCode(currentChar));
                    }
                }
                rules.add(new Rule(fromId, toIds));
            }
        }

        //add binding types
        for (Type type : typeBindings) {
            String typeName = type.scriptName.toUpperCase();
            String typeValue = "<" + typeName + "_VALUE>";
            String typeValueEnd = "<" + typeName + "_VALUE_END>";
            String typeVariable = "<" + typeName + "_VARIABLE>";
            String typeFunction = "<" + typeName + "_FUNCTION>";
//            addNonterminal(typeValue);
//            addNonterminal(typeValueEnd);
//            addNonterminal(typeVariable);
//            addNonterminal(typeFunction);

            //TODO: check it
            //rules.add(new Rule(nonterminals.get(typeVariable), EMPTY_CODE));

            //add rule: <VALUE> - <TYPE_VALUE><TYPE_VALUE_END>
            rules.add(new Rule(coder.getNonterminalCode("<VALUE>"),
                    coder.getNonterminalCode(typeValue), coder.getNonterminalCode(typeValueEnd)));

            //add rule: <ASSIGNED_VARIABLE> - <TYPE_VARIABLE>
            rules.add(new Rule(coder.getNonterminalCode("<ASSIGNED_VARIABLE>"), coder.getNonterminalCode(typeVariable)));

            for (Method method : type.methods) {
                List<Integer> methodCodes = new ArrayList<>();
                List<Integer> objectMethodCodes = new ArrayList<>();
                List<Integer> functionCodes = new ArrayList<>();
                methodCodes.addAll(coder.getTerminalCodes(method.scriptName));
                if (method.params.size() > 0) {
                    methodCodes.add(coder.getTerminalCode('('));
                    for (MethodParameter parameter : method.params) {
                        methodCodes.add(coder.getNonterminalCode("<" + parameter.type.toUpperCase() + ">"));
                        if (method.params.indexOf(parameter) < method.params.size() - 1) {
                            methodCodes.addAll(coder.getTerminalCodes(", "));
                        }
                    }
                    methodCodes.add(coder.getTerminalCode(')'));
                }
                objectMethodCodes.add(coder.getNonterminalCode(typeValue));
                objectMethodCodes.addAll(coder.getTerminalCodes(" -> "));
                objectMethodCodes.addAll(methodCodes);
                methodCodes.add(coder.getNonterminalCode("<" + method.returnType.toUpperCase() + "_VALUE_END>"));
                functionCodes.addAll(coder.getTerminalCodes(" -> "));
                functionCodes.add(coder.getNonterminalCode(typeFunction));

                //add rule: <TYPE_FUNCTION> - method(<TYPE1>,...)<RETURN_TYPE_VALUE_END>
                rules.add(new Rule(coder.getNonterminalCode(typeFunction), methodCodes));

                //add rule: <RETURN_TYPE_VALUE> - <TYPE> -> method(<TYPE1>,...)
                rules.add(new Rule(coder.getNonterminalCode("<" + method.returnType.toUpperCase() + "_VALUE>"), objectMethodCodes));

                //add rule: <TYPE_VALUE_END - empty character
                rules.add(new Rule(coder.getNonterminalCode(typeValueEnd), Coder.EMPTY_CODE));

                //add rule: <TYPE_VALUE_END> -  -> <TYPE_FUNCTION>
                rules.add(new Rule(coder.getNonterminalCode(typeValueEnd), functionCodes));
            }
        }
    }

    @Override
    public void close() throws IOException {
        br.close();
    }
}
