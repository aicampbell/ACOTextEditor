package engine;

import commands.DeleteCommand;
import util.EngineObserver;

/**
 * Created by Aidan on 10/10/2016.
 */
public class Engine implements EngineI {
    private EngineObserver engineObserver;

    private CommandHistory commandHistory;
    private Buffer buffer;
    private Buffer clipboard;

    private int cursorPosition = 0;

    private int selectionStart = 0;
    private int selectionEnd = 0;
    private boolean isTextSelected = false;

    private boolean isRecordingActive = false;

    public Engine() {
        commandHistory = new CommandHistory(this);
        buffer = new Buffer();
    }

    public void insertChar(String character) {
        deleteSelectionIfExists(selectionStart, selectionEnd);

        // Insert typed character
        buffer.insertAtPosition(character, cursorPosition);
        cursorPosition++;
        //System.out.println("cursorPos after insertChar's pos++: " + cursorPosition);
        notifyTextChange();
        notifyCursorChange();
    }

    public void deleteInDirection(int delDirection) {
        if (deleteSelectionIfExists(selectionStart, selectionEnd)) {
            return;
        } else if (delDirection == DeleteCommand.DEL_FORWARDS) {
            // "Delete" key was used, so the character at currentPosition should be deleted.
            // cursorPosition doesn't change.
            buffer.deleteAtPosition(cursorPosition);
        } else if (delDirection == DeleteCommand.DEL_BACKWARDS) {
            // "Backspace" key was used, so the previous character at currentPosition-1 should be deleted.
            // cursorPosition is decremented.
            buffer.deleteAtPosition(cursorPosition - 1);
            //cursorPosition--;
        }
    }

    /**
     * Is triggered for cursor updates that are not part of selecting text.
     *
     * @param position
     */
    public void updateCursor(int position) {
        cursorPosition = position;
        isTextSelected = false;
        //System.out.println("cursorPos after updateCursor: " + cursorPosition);
        notifyCursorChange();
    }

    /**
     * Is triggered only for real selections where start<end is satisfied.
     *
     * @param start
     * @param end
     */
    public void updateSelection(int start, int end) {
        selectionStart = start;
        selectionEnd = end;
        isTextSelected = true;
        cursorPosition = end;
    }

    public void cutSelection() {
        if (isTextSelected) {
            clipboard = buffer.getCopy(selectionStart, selectionEnd);
        }
        deleteSelectionIfExists(selectionStart, selectionEnd);
    }

    public void copySelection() {
        if (isTextSelected) {
            clipboard = buffer.getCopy(selectionStart, selectionEnd);
        }
    }

    public void pasteClipboard() {
        deleteSelectionIfExists(selectionStart, selectionEnd);

        if (clipboard != null && !clipboard.isEmpty()) {
            buffer.insertAtPosition(clipboard, cursorPosition);
            cursorPosition += clipboard.getSize();
        }
    }

    public void undoCommand() {

    }

    public void redoCommand() {

    }

    public void toggleRecording() {
        if (isRecordingActive) {
            // stop recording
        } else {
            // start recording
        }
    }

    public void replay() {

    }

    /**
     * Deletes the currently selected text, if there is a selection.
     *
     * @param start index of selection.
     * @param end index of selection.
     * @return true if a selection has been deleted, or false if no selection is active.
     */
    private boolean deleteSelectionIfExists(int start, int end) {
        if (isTextSelected) {
            buffer.deleteInterval(start, end);
            cursorPosition = selectionStart;
            isTextSelected = false;
            return true;
        }
        return false;
    }

    public void attachObserver(EngineObserver engineObserver) {
        this.engineObserver = engineObserver;
    }

    private void notifyTextChange() {
        engineObserver.updateText(buffer.getStringContent());
    }

    private void notifyCursorChange() {
        engineObserver.updateCursor(cursorPosition);
    }
}
