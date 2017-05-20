package context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 22.10.2016.
 */
public class HStack {
    private List<Integer> stack;

    public HStack(Integer endElement, Integer startElement) {
        stack = new ArrayList<>(2);
        stack.add(endElement);
        stack.add(startElement);
    }

    public HStack(List<Integer> stack) {
        this.stack = new ArrayList<>(stack.size());
        for (int i = 0; i < stack.size(); i++) {
            this.stack.add(stack.get(i));
        }
    }

    public void push(Integer[] codes) {
        stack.remove(stack.size() - 1);
        for (int i = codes.length - 1; i >= 0; i--) {
            stack.add(codes[i]);
        }
    }

    public void removeLast() {
        stack.remove(stack.size() - 1);
    }

    public HStack copy() {
        return new HStack(stack);
    }

    public Integer get() {
        return stack.get(stack.size()-1);
    }

    public Integer get(int i) {
        return stack.get(i);
    }

    public int size() {
        return stack.size();
    }
}
