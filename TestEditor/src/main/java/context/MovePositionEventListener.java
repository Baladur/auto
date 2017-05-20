package context;

/**
 * Created by roman on 20.05.2017.
 */
public @FunctionalInterface interface MovePositionEventListener {
    void moveToPosition(CodePosition position);
}
