package engine;

/**
 * Created by mo on 13.11.16.
 */
public class Selection {
    int selectionBase;
    int selectionEnd;

    public Selection(int base, int end) {
        selectionBase = base;
        selectionEnd = end;
    }

    public int getSelectionBase() {
        return selectionBase;
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }
}
