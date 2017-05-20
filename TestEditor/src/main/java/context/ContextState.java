package context;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 18.05.2017.
 */
@Setter
@Getter
public class ContextState {
    private BiMap<CodePosition, HStack> snapshots = HashBiMap.create();
    private HStack currentStack;
    private String currentInput = "";
    private CodePosition currentPosition;
    private Integer ptr = 1;
    private boolean isOk = true;

    public ContextState() {
        currentPosition = new CodePosition(1, 1);
    }

    public ContextState(CodePosition position) {
        currentPosition = position;
    }

    public void addSnapshot(CodePosition position, HStack stack) {
        snapshots.put(position, stack);
    }

    public boolean returnToPosition(CodePosition position) {
        currentPosition = position;
        HStack stack = snapshots.get(position);
        if (stack != null) {
            currentStack = stack;
            return true;
        } else {
            return false;
        }

    }

    public void moveInputPointer() {
        ptr++;
        currentPosition.column++;
    }

    public void newLine() {
        ptr = 0;
        currentPosition.line++;
        currentPosition.column = 1;
    }

    public Character getInputChar() {
        return ptr < currentInput.length() ? currentInput.charAt(ptr) : null;
    }

    public void appendInput(String appendix) {
        currentInput += appendix;
        currentPosition.column += appendix.length();
    }

    public void clearInput() {
        ptr = 0;
        currentPosition.column -= currentInput.length();
        currentInput = "";
    }

    public void deleteLastChar() {
        currentInput = currentInput.substring(0, currentInput.length() - 1);
        currentPosition.column--;
    }
}
