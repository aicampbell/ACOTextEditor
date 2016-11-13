package commands;

import engine.Engine;

/**
 * This class represents the UpdateSelectionCommand that is build and
 * run when user decides to select text in the text editor. The origin
 * of this command can be the key combination CTRL + A which selects
 * all text or by click-and-drag the mouse.
 */
public class UpdateSelectionCommand implements Command {
    int start;
    int end;

    /**
     * The constructor takes the start and end position of the
     * selection as parameters.
     *
     * @param start position of selection.
     * @param end positon of selection.
     */
    public UpdateSelectionCommand(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public void execute(Engine engine) {
        engine.updateSelection(start, end);
        engine.getRecordModule().record(this);
    }
}
