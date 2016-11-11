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

    private int selectionBase = 0;
    private int selectionEnd = 0;
    private boolean isTextSelected = false;

    private boolean isRecordingActive = false;

    public Engine() {
        commandHistory = new CommandHistory(this);
        buffer = new Buffer();
    }

    public void insertChar(char character) {
        deleteSelectionIfExists(selectionBase, selectionEnd);

        // Insert typed character
        buffer.insertAtPosition(character, cursorPosition);
        cursorPosition++;

        notifyTextChange();
        notifyCursorChange();
    }

    public void deleteInDirection(int delDirection) {
        if (deleteSelectionIfExists(selectionBase, selectionEnd)) {
            notifyTextChange();
            notifyCursorChange();
        } else if (delDirection == DeleteCommand.DEL_FORWARDS) {
            // "Delete" key was used, so the character at currentPosition should be deleted.
            // cursorPosition doesn't change.
            buffer.deleteAtPosition(cursorPosition);
            // After text is updated on UI we need to make sure that the caret is restored as well.
            notifyTextChange();
            notifyCursorChange();
        } else if (delDirection == DeleteCommand.DEL_BACKWARDS) {
            // "Backspace" key was used, so the previous character at currentPosition-1 should be deleted.
            buffer.deleteAtPosition(cursorPosition - 1);

            // Only move cursor and notify UI about it when cursorPosition is not 0. If it is 0, we are not allowed to move the cursor and therefor we also don't need to notify the UI.
            if(cursorPosition > 0) {
                cursorPosition--;
                // After text is updated on UI we need to make sure that the caret is restored as well.
                notifyTextChange();
                notifyCursorChange();
            }
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
        selectionBase = 0;
        selectionEnd = 0;

        notifyCursorChange();
    }

    /**
     * Is triggered only for real selections where start<end is satisfied.
     *
     * @param start
     * @param end
     */
    public void updateSelection(int start, int end) {
        if(start != end) {
            selectionBase = start;
            selectionEnd = end;
            isTextSelected = true;
            cursorPosition = end;

            notifySelectionChange();
        } else {
            // Need for selections created by SHIFT + ARROW_KEYS where selection is reduced to 0-length after having a selection of at least size==1.
            updateCursor(start);
        }
    }

    public void extendSelection(int newEnd) {
        if(isTextSelected) {
            updateSelection(selectionBase, newEnd);
        } else {
            updateSelection(cursorPosition, newEnd);
        }
    }

    public void cutSelection() {
        if (isTextSelected) {
            clipboard = buffer.getCopy(selectionBase, selectionEnd);
        }
        deleteSelectionIfExists(selectionBase, selectionEnd);

        notifyTextChange();
        notifyCursorChange();
    }

    public void copySelection() {
        if (isTextSelected) {
            clipboard = buffer.getCopy(selectionBase, selectionEnd);
        }
    }

    public void pasteClipboard() {
        deleteSelectionIfExists(selectionBase, selectionEnd);

        if (clipboard != null && !clipboard.isEmpty()) {
            buffer.insertAtPosition(clipboard, cursorPosition);
            cursorPosition += clipboard.getSize();
        }

        notifyTextChange();
        notifyCursorChange();
        //notifySelectionChange();
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
     * @param base index of selection.
     * @param end index of selection.
     * @return true if a selection has been deleted, or false if no selection is active.
     */
    private boolean deleteSelectionIfExists(int base, int end) {
        if (isTextSelected) {
            buffer.deleteInterval(base, end);
            cursorPosition = base < end ? base : end;
            isTextSelected = false;

            return true;
        }
        return false;
    }

    public void registerObserver(EngineObserver engineObserver) {
        this.engineObserver = engineObserver;
    }

    private void notifyTextChange() {
        engineObserver.updateText(buffer.getStringContent());
    }

    private void notifyCursorChange() {
        engineObserver.updateCursor(cursorPosition);
    }

    private void notifySelectionChange() {
        engineObserver.updateSelection(isTextSelected, selectionBase, selectionEnd);
    }
}
