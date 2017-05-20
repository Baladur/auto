package model;

import java.util.List;

/**
 * Created by roman on 18.05.2017.
 */
public class Hint {
    private String label;
    private List<Integer> path;

    public Hint(String label, List<Integer> path) {
        this.label = label;
        this.path = path;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Integer> getPath() {
        return path;
    }

    public void setPath(List<Integer> path) {
        this.path = path;
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Hint && o.hashCode() == hashCode();
    }
}
