package commands;

import engine.Engine;

/**
 * This class represents the ExtendSelectionCommand that is build and run when user
 * decides to select or change the selection of text by using the key combination
 * SHIFT + ARROW_KEYS. By using this command only one value of the pair
 * selectionStart/selectionEnd changes while the other remains the same.
 */
public class ExtendSelectionCommand implements Command {
    int end;

    /**
     * The constructor takes the new end of the selection as parameter.
     *
     * @param end new end index of the selection
     */
    public ExtendSelectionCommand(int end) {
        this.end = end;
    }

    public void execute(Engine engine) {
        engine.expandSelection(end);
        engine.getRecordModule().record(this);
    }
}
