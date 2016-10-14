package engine;

import commands.DeleteCommand;

/**
 * Created by Aidan on 10/10/2016.
 */
public class Engine implements EngineI {
    private CommandHistory commandHistory;
    private Buffer buffer;
    private Buffer clipboard;

    private int cursorPosition;
    private int selectionStart;
    private int selectionEnd;

    public Engine() {
        commandHistory = new CommandHistory(this);
        buffer = new Buffer();
    }

    public void updateCursorPosition(int position) {

    }

    public void insertChar(char character) {

    }

    public void deleteInDirection(int delDirection) {
        if (delDirection == DeleteCommand.DEL_FORWARDS) {
            // "delete" key was used
            // ...
        } else if (delDirection == DeleteCommand.DEL_BACKWARDS) {
            // "backspace" key was used
            // ...
        }
    }

    public void cutCurrentSelection() {

    }

    public void copyCurrentSelection() {

    }

    public void pasteCurrentSelection() {

    }

    public void undoCommand() {

    }

    public void redoCommand() {

    }
}
