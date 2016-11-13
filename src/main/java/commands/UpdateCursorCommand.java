package commands;

import engine.Engine;

/**
 * This class represents the UpdateCursorCommand that is build and
 * run when user decides to move the position of the cursor. The origin
 * can be navigation from ARROW_KEYS or simply clicking with the mouse
 * in the text editor.
 */
public class UpdateCursorCommand implements Command {
    int position;

    /**
     * The constructor takes the new position of the cursor as parameter.
     *
     * @param position of the updated cursor.
     */
    public UpdateCursorCommand(int position) {
        this.position = position;
    }

    public void execute(Engine engine) {
        engine.updateCursor(position);
        engine.getRecordModule().record(this);
    }
}
