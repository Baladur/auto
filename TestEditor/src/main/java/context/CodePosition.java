package context;

/**
 * Created by roman on 18.05.2017.
 */
public class CodePosition {
    public int line = 1;
    public int column = 1;

    public CodePosition(int line, int column) {
        this.line = line;
        this.column = column;
    }

    @Override
    public int hashCode() {
        return line * 100000 + column;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CodePosition && o.hashCode() == hashCode();
    }
}
