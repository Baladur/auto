package context;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.List;

/**
 * This class performs convertion between (non)terminals and their codes.
 */
public class Coder {
    public static final int EMPTY_CODE = 0;

    private BiMap<String, Integer> nonterminals;
    private BiMap<Character, Integer> terminals;
    private int nonterminalIdCounter = 1;
    private int terminalIdCounter = -1;

    public BiMap<String, Integer> getNonterminals() {
        return nonterminals;
    }

    public BiMap<Character, Integer> getTerminals() {
        return terminals;
    }

    public Coder() {
        nonterminals = HashBiMap.create();
        terminals = HashBiMap.create();
    }

    /**
     * Gets code of nonterminal.
     * If no code is found, new code is generated and associated with given nonterminal.
     *
     * @param nonterminal
     * @return
     */
    public Integer getNonterminalCode(String nonterminal) {
        nonterminalIdCounter += nonterminals.putIfAbsent(nonterminal, nonterminalIdCounter) == null ? 1 : 0;
        return nonterminals.get(nonterminal);
    }

    /**
     * Gets code of terminal.
     * If no code is found, new code is generated and associated with given terminal.
     *
     * @param terminal
     * @return
     */
    public Integer getTerminalCode(Character terminal) {
        terminalIdCounter -= terminals.putIfAbsent(terminal, terminalIdCounter) == null ? 1 : 0;
        return terminals.get(terminal);
    }

    /**
     * Converts string to list of terminal codes.
     * All the unknown characters are saved.
     *
     * @param terminalSequence
     * @return
     */
    public List<Integer> getTerminalCodes(String terminalSequence) {
        List<Integer> codes = new ArrayList<>(terminalSequence.length());
        for (Character ch : terminalSequence.toCharArray()) {
            codes.add(getTerminalCode(ch));
        }
        return codes;
    }

    /**
     * Gets terminal by its code.
     *
     * @param code
     * @return
     */
    public Character getTerminal(Integer code) {
        return terminals.inverse().get(code);
    }

    /**
     * Gets nonterminal by its code.
     *
     * @param code
     * @return
     */
    public String getNonterminal(Integer code) {
        return nonterminals.inverse().get(code);
    }
}
