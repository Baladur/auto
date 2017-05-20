package context;

import model.Hint;
import model.Type;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by roman on 16.05.2017.
 */
public class Grammar {
    private static final Character END_CHAR = '\n';

    //TODO: replace integer type of code with short
    private Rule[][] Mtable;
    private Coder coder;
    private Integer startElement;
    private List<Rule> allRules;
    private List<Set<Hint>> hintList;
    private List<Set<Integer>> firstList;
    private List<Set<Integer>> followList;
    private List<Set<Integer>> synch;

    public Integer getStartElement() {
        return startElement;
    }

    public List<Rule> getRules(Predicate<Rule> searchCriteria) {
        return allRules.stream().filter(searchCriteria).collect(Collectors.toList());
    }

    public Grammar(String grammaticPath, String startElement, List<Type> typeBindings) throws IOException {
        try (GrammarReader reader = new GrammarReader(new BufferedReader(new FileReader(grammaticPath)), typeBindings)) {
            reader.read();
            coder = reader.getCoder();
            allRules = reader.getRules();
        }
        this.startElement = coder.getNonterminalCode(startElement);
        calculateHints();
    }

    /**
     * Calculates hints and associates them with all nonterminals.
     */
    public void calculateHints() {
        hintList = new ArrayList<>();
        IntStream.range(0, coder.getNonterminals().size())
                .forEach(i -> hintList.add(new HashSet<>()));
        //at first take nonterminals that lead to terminals directly
        List<Rule> rules = getRules(rule -> isTerminal(rule.to[0]));

        //for each rule create hint and assign left part of rule as path to hint
        rules.stream().forEach(rule -> {
            //extract terminal sequence
            String terminalSequence = extractTerminalSequence(rule);

            //prepare label
            String label = terminalSequence; //terminalSequence.indexOf(" ") >= 0 ? terminalSequence.substring(0, terminalSequence.indexOf(" ")) : terminalSequence;

            //create hint
            Hint hint = new Hint(label, Arrays.asList(rule.from));

            //add hint to left production nonterminal
            getHintsOf(rule.from).add(hint);
        });
        Set<Integer> currentNonterminals = new HashSet<>();
        currentNonterminals.addAll(rules.stream().map(rule -> rule.from).collect(Collectors.toList()));
        Set<Integer> allParentNonterminals = null;
        int previousNonterminalCount = 0;
        while (previousNonterminalCount != currentNonterminals.size()) {
            allParentNonterminals = new HashSet<>();

            for (Integer nt : currentNonterminals) {
                //get nonterminals which are direct parents for current rule.from
                List<Rule> parentRules = getRules(parent -> parent.to[0].equals(nt));
                Set<Integer> parentNonterminals = new HashSet<>(parentRules.stream().map(rule -> rule.from).collect(Collectors.toList()));
                allParentNonterminals.addAll(parentNonterminals);

                //get hints of current nonterminal
                Set<Hint> childHints = getHintsOf(nt);

                parentNonterminals.stream().forEach(parent -> {
                    //add hints of current rule.from for each parent
                    Set<Hint> parentHints = new HashSet<>();
                    for (Hint childHint : childHints) {
                        List<Integer> path = new ArrayList<>();
                        path.add(parent);
                        path.addAll(childHint.getPath());
                        parentHints.add(new Hint(childHint.getLabel(), path));
                    }
                    getHintsOf(parent).addAll(parentHints);
                });
            }
            previousNonterminalCount = currentNonterminals.size();
            currentNonterminals = allParentNonterminals;
        }
        int a = 5;
    }

    private String extractTerminalSequence(Rule rule) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rule.to.length; i++) {
            if (isTerminal(rule.to[i])) {
                sb.append(getTerminal(rule.to[i]));
            } else {
                break;
            }
        }
        return sb.toString();
    }

    public void calculatePredictionTable() {
        System.out.println("RULES:");
        final StringBuilder sb = new StringBuilder();
        for (Rule rule : allRules) {
            sb.append(coder.getNonterminal(rule.from))
                    .append(" -> ");
            for (int i = 0; i < rule.to.length; i++) {
                if (isTerminal(rule.to[i])) {
                    sb.append(isEmptyCode(rule.to[i]) ? "\\n" : coder.getTerminal(rule.to[i]));
                } else {
                    sb.append(coder.getNonterminal(rule.to[i]));
                }
            }
            System.out.println(sb.toString());
            sb.delete(0, sb.length());
        }
        //allRules.forEach(rule -> System.out.println(rule.toString()));
        firstList = new ArrayList<>();
        followList = new ArrayList<>();
        IntStream.range(0, coder.getNonterminals().size())
                .forEach(i -> {
                    firstList.add(new HashSet<>());
                    followList.add(new HashSet<>());
                });
        first();

        //print first sets
        System.out.println("FIRST SETS:");
        for (Map.Entry<String, Integer> entry : coder.getNonterminals().entrySet()) {
            System.out.print(entry.getKey() + " = { ");
            //TODO: remove in production
            firstList.get(index(entry.getValue())).stream()
                    .map(code -> isEmptyCode(code) ? "\\n" : coder.getTerminal(code))
                    .forEach(element -> System.out.print(element + ", "));
            System.out.println(" }");
        }
        follow();

        //print follow sets
        System.out.println("FOLLOW SETS:");
        for (Map.Entry<String, Integer> entry : coder.getNonterminals().entrySet()) {
            System.out.print(entry.getKey() + " = { ");
            //TODO: remove in production
            followList.get(index(entry.getValue())).stream()
                    .map(code -> isEmptyCode(code) ? "\\n" : coder.getTerminal(code))
                    .forEach(element -> System.out.print(element + ", "));
            System.out.println(" }");
        }
        //make up synchronization sets
        initMtable();
        synch();
        //addSynchToMtable();
        assignMtable();
    }

    /**
     * Is the code of terminal or nonterminal.
     *
     * @param code code of character
     * @return
     */
    public boolean isTerminal(Integer code) {
        return code <= 0;
    }

    /**
     * Is the code of empty character.
     *
     * @param code code of character
     * @return
     */
    public boolean isEmptyCode(Integer code) {
        return code == Coder.EMPTY_CODE;
    }

    /**
     * Is production error synchronization.
     *
     * @param rule production
     * @return
     */
    public boolean isSynch(Rule rule) {
        return rule.from.equals(0) && isLeadingToEmpty(rule);
    }

    /**
     * Is right part of production an empty character.
     *
     * @param rule production
     * @return
     */
    public boolean isLeadingToEmpty(Rule rule) {
        return rule.to.length == 0 && rule.to[0] == Coder.EMPTY_CODE;
    }

    /**
     * Get grammar rule.
     *
     * @param nonterminal left part of production
     * @param terminal right part of production
     * @return
     */
    public Rule getRule(Integer nonterminal, Integer terminal) {
        if (index(nonterminal) < 0 || index(terminal) < 0) {
            int a = 5;
        }
        return Mtable[index(nonterminal)][index(terminal)];
    }

    /**
     * Get index for Mtable by terminal or nonterminal integer code.
     *
     * @param code terminal or nonterminal code
     * @return index in Mtable
     */
    public int index(Integer code) {
        return Math.abs(code) - 1;
    }

    /**
     * Get terminal value by its integer code.
     * Note: for debug use only.
     *
     * @param code integer code of terminal
     * @return terminal
     */
    public Character getTerminal(Integer code) {
        return coder.getTerminal(code);
    }

    /**
     * Get nonterminal name (as in grammar) by its integer code.
     * Note: for debug use only.
     *
     * @param code integer code of nonterminal
     * @return terminal
     */
    public String getNonterminal(Integer code) {
        return coder.getNonterminal(code);
    }

    /**
     * Get terminal or nonterminal code by its value.
     *
     * @param key character (for terminals) or string (for nonterminals)
     * @return
     */
    public Integer getCode(Object key) {
        if (key instanceof Character) {
            return coder.getTerminalCode((Character)key);
        } else {
            return coder.getNonterminalCode((String)key);
        }
    }

    /**
     * Get FIRST set for nonterminal.
     *
     * @param code nonterminal code
     * @return
     */
    public Set<Integer> getFirstOf(Integer code) {
        return firstList.get(index(code));
    }

    /**
     * Get FOLLOW set for nonterminal.
     *
     * @param code nonterminal code
     * @return
     */
    public Set<Integer> getFollowOf(Integer code) {
        return followList.get(index(code));
    }

    /**
     * Get synchronization set for nonterminal.
     *
     * @param code nonterminal code
     * @return
     */
    public Set<Integer> getSynchOf(Integer code) { return synch.get(index(code)); }

    /**
     * Gets list of hints for nonterminal.
     *
     * @param code nonterminal code
     * @return
     */
    public Set<Hint> getHintsOf(Integer code) {
        return hintList.get(index(code));
    }

    /**
     * Adds rule to grammar.
     *If rule already exists, nothing is added.
     *
     * @param from left part of production
     * @param to right part of production
     */
    public void addRule(String from, String to) {
        Integer fromCode = getCode(from);
        Integer[] toCodes = coder.getTerminalCodes(to).toArray(new Integer[0]);
        if (!allRules.stream().anyMatch(rule ->
                fromCode == rule.from && commonPrefixLength(toCodes, rule.to) == toCodes.length && toCodes.length == rule.to.length)) {
            allRules.add(new Rule(fromCode, toCodes));
        }
    }

    /**
     * Left factorization.
     */
    public void leftFactorize() {
        //TODO: implement algorithm
        coder.getNonterminals().values().stream()
                .forEach(nt -> {
                    List<Rule> ntRules = allRules.stream().filter(rule -> rule.from.equals(nt)).collect(Collectors.toList());
                    ntRules.stream().forEach(rule -> {

                    });
                });
    }

    /**
     * Find common prefix of two terminal sequences. It is used in {@code leftFactorize()} procedure.
     * If there is no common prefix, 0 is returned.
     *
     * @param val1
     * @param val2
     * @return common prefix length
     */
    private int commonPrefixLength(Integer[] val1, Integer[] val2) {
        int prefixLength = 0;
        for (int i = 0; i < Math.min(val1.length, val2.length); i++) {
            if (val1[i] == val2[i]) {
                prefixLength++;
            } else {
                break;
            }
        }
        return prefixLength;
    }

    private void initMtable() {
        int nonterminalCount = coder.getNonterminals().size();
        int terminalCount = coder.getTerminals().size();
        Mtable = new Rule[nonterminalCount][terminalCount];
        for (int i = 0; i < nonterminalCount; i++) {
            for (int j = 0; j < terminalCount; j++) {
                Mtable[i][j] = new Rule(Rule.RuleSpecial.Synch);
            }
        }
    }

    private void assignMtable() {
        for (Rule rule : allRules) {
            int contained$ = 0;
            for (Integer code : rule.to) {
                if (isTerminal(code)) {
                    if (isEmptyCode(code)) {
                        contained$++;
                    } else {
                        Mtable[index(rule.from)][index(code)] = rule;
                        break;
                    }
                } else if (!isTerminal(code)) {
                    Set<Integer> toSet = getFirstOf(code);
                    for (Integer toCode : toSet) {
                        if (!isEmptyCode(code)) {
                            Mtable[index(rule.from)][index(toCode)] = rule;
                        }
                    }
                    if (!toSet.contains(coder.getTerminalCode(END_CHAR))) {
                        break;
                    } else {
                        contained$++;
                    }
                }
            }
            if (contained$ == rule.to.length) {
                Set<Integer> followSet = getFollowOf(rule.from);
                for (Integer code : followSet) {
                    Mtable[index(rule.from)][index(code)] = rule;
                }
                if (followSet.contains(coder.getTerminalCode(END_CHAR))) {
                    Mtable[index(rule.from)][index(coder.getTerminalCode(END_CHAR))] = rule;
                }
            }
        }
    }

    private void synch() {
        synch = new ArrayList<>();
        System.out.println("SYNCH SETS:");
        IntStream.range(0, coder.getNonterminals().size())
                .forEach(i -> synch.add(new HashSet<>()));
        for (Map.Entry<String, Integer> entry : coder.getNonterminals().entrySet()) {
            System.out.println(entry.getKey() + " = { ");
            for (Integer followCode : getFollowOf(entry.getValue())) {
                getSynchOf(entry.getValue()).add(followCode);
            }
            for (Integer firstCode : firstList.get(index(entry.getValue()))) {
                getSynchOf(entry.getValue()).add(firstCode);
            }
            //TODO: remove in production
            getSynchOf(entry.getValue()).stream()
                    .map(code -> coder.getTerminal(code))
                    .forEach(terminal -> System.out.print(terminal + ", "));
            System.out.println(" }");

        }
    }

    private void first() {
        boolean change = true;
        first_2rule();
        while (change) {
            change = first_3rule();
        }
    }

    private void follow() {
        boolean change = true;
        follow_1rule();
        follow_2rule();
        while (change) {
            change = follow_3rule();
        }
    }

    private boolean first_1rule(Integer code) {
        return isTerminal(code);
    }

    private boolean first_2rule() {
        for (Rule rule : allRules) {
            if (isLeadingToEmpty(rule)) {
                getFirstOf(rule.from).add(Coder.EMPTY_CODE);
            }
        }

        return false;
    }

    private boolean first_3rule() {
        boolean change = false;
        for (Rule rule : allRules) {
            Set<Integer> fromSet = getFirstOf(rule.from);
            int contained$ = 0;
            for (Integer code : rule.to) {
                if (isTerminal(code)) {
                    if (isEmptyCode(code)) {
                        //contained$++;
                    } else {
                        if (!fromSet.contains(code)) {
                            fromSet.add(code);
                            change = true;

                        }
                        break;
                    }

                } else {
                    //is nonterminal
                    Set<Integer> toSet = getFirstOf(code);
                    for (Integer toCode : toSet) {
                        if (!isEmptyCode(toCode)) {
                            if (!fromSet.contains(toCode)) {
                                fromSet.add(toCode);
                                change = true;
                            }
                        }

                    }
                    if (!toSet.contains(0)) {
                        break;
                    } else {
                        contained$++;
                    }
                }
            }
            if (contained$ == rule.to.length) {
                if (!fromSet.contains(Coder.EMPTY_CODE)) {
                    fromSet.add(Coder.EMPTY_CODE);
                    change = true;
                }
            }
        }

        return change;
    }

    private void follow_1rule() {
        getFollowOf(startElement).add(coder.getTerminalCode(END_CHAR));
    }

    private void follow_2rule() {
        for (Rule rule : allRules) {
            if (rule.to.length > 1) {
                for (int i = rule.to.length - 1; i >= 1; i--) {
                    if (!isTerminal(rule.to[i - 1])) {
                        if (first_1rule(rule.to[i]) && rule.to[i] != Coder.EMPTY_CODE) {
                            getFollowOf(rule.to[i-1]).add(rule.to[i]);
                        } else {
                            Set<Integer> firstSet = getFirstOf(rule.to[i]);
                            for (Integer code : firstSet) {
                                if (!isEmptyCode(code)) {
                                    getFollowOf(rule.to[i-1]).add(code);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean follow_3rule() {
        //TODO: add empty character to logic
        boolean change = false;
        for (Rule rule : allRules) {
            for (int i = rule.to.length-1; i >= 0; i--) {
                if (!isTerminal(rule.to[i + (i == rule.to.length - 1 ? 0 : 1)]) && !isTerminal(rule.to[i])) {
                    Set<Integer> fromSet = getFollowOf(rule.from);
                    Set<Integer> toSet = getFollowOf(rule.to[i]);
                    for (Integer code : fromSet) {
                        if (!toSet.contains(code)) {
                            toSet.add(code);
                            change = true;
                        }
                    }
                }
            }
        }

        return change;
    }




}
