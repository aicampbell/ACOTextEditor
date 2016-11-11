package engine;

import commands.DeleteCommand;
<<<<<<< Updated upstream
=======
import util.EngineObserver;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.List;
>>>>>>> Stashed changes

import static java.lang.System.lineSeparator;

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
<<<<<<< Updated upstream
            // "backspace" key was used
            // ...
        }
    }

    public void cutCurrentSelection() {
=======
            // "Backspace" key was used, so the previous character at currentPosition-1 should be deleted.
            buffer.deleteAtPosition(cursorPosition - 1);

            // Only move cursor and notify UI about it when cursorPosition is not 0. If it is 0, we are not allowed to move the cursor and therefor we also don't need to notify the UI.
            if (cursorPosition > 0) {
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
        if (start != end) {
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
        if (isTextSelected) {
            updateSelection(selectionBase, newEnd);
        } else {
            updateSelection(cursorPosition, newEnd);
        }
    }
>>>>>>> Stashed changes

    }

    public void copyCurrentSelection() {

    }

    public void pasteCurrentSelection() {

    }

    public void undoCommand() {

    }

    public void redoCommand() {

    }
<<<<<<< Updated upstream
=======

    public void startRecording() {
        recordModule.clear().start();
    }

    public void stopRecording() {
        recordModule.stop();
    }

    public void replayRecording() {
        List<Command> commands = recordModule.getReplayList();
        for (Command command : commands) {
            command.execute(this);
        }
    }

    public void openFile() {

        JFileChooser fileChooser = new JFileChooser();
        File selectedFile = null;
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile= fileChooser.getSelectedFile();
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(selectedFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            char[] characterArray = sb.toString().toCharArray();

            //Need to import character array into the buffer
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes the currently selected text, if there is a selection.
     *
     * @param base index of selection.
     * @param end  index of selection.
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
>>>>>>> Stashed changes
}
