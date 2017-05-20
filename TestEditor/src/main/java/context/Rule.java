package context;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by roman on 22.10.2016.
 */
public class Rule {
    public Integer from;
    public Integer[] to;

    public Rule(Integer pFrom, Integer... pTo) {
        from = pFrom;
        to = pTo;
    }

    public Rule(Integer pFrom, List<Integer> pTo) {
        from = pFrom;
        to = pTo.toArray(new Integer[pTo.size()]);
    }

    public Rule(RuleSpecial rs) {
        if (rs.equals(RuleSpecial.Synch)) {
            to = new Integer[1];
            to[0] = 0;
            from = 0;
        }
    }



    public String toString() {
//        if (isSynch()) {
//            return "synch";
//        }
        StringBuilder sb = new StringBuilder(from.toString());
        sb.append(" -> ");
        Stream.of(to).forEach(code -> sb.append(code.toString() + ", "));
        return sb.toString();
    }

    public enum RuleSpecial {
        Synch
    }
}
