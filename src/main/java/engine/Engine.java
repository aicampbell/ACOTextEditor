package engine;

import commands.interfaces.Command;
import commands.DeleteCommand;
import util.EngineObserver;

import javax.swing.*;
import java.io.*;
import java.util.List;

/**
 * Created by Aidan on 10/10/2016.
 */
public class Engine implements EngineI {
    private EngineObserver engineObserver;

    //private UndoModule moduleUndoRedo;
    private RecordModule recordModule;

    private Buffer buffer;
    private Buffer clipboard;

    private int cursorPosition = 0;

    private int selectionBase = 0;
    private int selectionEnd = 0;
    private boolean isTextSelected = false;

    public Engine() {
        recordModule = new RecordModule();
        buffer = new Buffer();
    }

    public RecordModule getRecordModule() {
        return recordModule;
    }

    public void insertChar(char character) {
        deleteSelectionIfExists(selectionBase, selectionEnd);

        // Insert typed character
        buffer.insertAtPosition(character, cursorPosition);
        cursorPosition++;
        System.out.println("cursor position: "+cursorPosition);
        System.out.print(character);

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
            /** Need for selections created by SHIFT + ARROW_KEYS where selection is reduced to 0-length after having a selection of at least size==1. */
            updateCursor(start);
        }
    }

    public void expandSelection(int newEnd) {
        if(isTextSelected) {
            updateSelection(selectionBase, newEnd);
        } else {
            updateSelection(cursorPosition, newEnd);
        }
    }

    public void copySelection() {
        if (isTextSelected) {
            clipboard = buffer.getCopy(selectionBase, selectionEnd);
        }
        System.out.println(clipboard);
    }

    public void cutSelection() {
        if (isTextSelected) {
            clipboard = buffer.getCopy(selectionBase, selectionEnd);
        }
        deleteSelectionIfExists(selectionBase, selectionEnd);

        notifyTextChange();
        notifyCursorChange();
    }

    public void pasteClipboard() {
        deleteSelectionIfExists(selectionBase, selectionEnd);

        if (clipboard != null && !clipboard.isEmpty()) {
            int clipboardSize = clipboard.getSize();
            buffer.insertAtPosition(clipboard, cursorPosition);
            cursorPosition += clipboardSize;
        }

        notifyTextChange();
        notifyCursorChange();
    }

    public void undoCommand() {

    }

    public void redoCommand() {

    }

    public void startRecording() {
        recordModule.clear().start();
    }

    public void stopRecording() {
        recordModule.stop();
    }

    public void replayRecording() {
        List<Command> commands = recordModule.getReplayList();
        for(Command command : commands) {
            command.execute(this);
        }
    }

    public void openFile(List<Character> chars) {
        buffer = new Buffer(chars);
        cursorPosition = 0;
        isTextSelected = false;

        notifyTextChange();
        notifyCursorChange();
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
        engineObserver.updateText(buffer.toString());
    }

    private void notifyCursorChange() {
        engineObserver.updateCursor(cursorPosition);
    }

    private void notifySelectionChange() {
        engineObserver.updateSelection(isTextSelected, selectionBase, selectionEnd);
    }
}
