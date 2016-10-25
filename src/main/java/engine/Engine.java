package engine;

import commands.DeleteCommand;

/**
 * Created by Aidan on 10/10/2016.
 */
public class Engine implements EngineI {
    private CommandHistory commandHistory;
    private Buffer buffer;
    private Buffer clipboard;

    private int cursorPosition = 0;
    private int selectionStart = 0;
    private int selectionEnd = 0;
    private boolean isRecordingActive = false;

    public Engine() {
        commandHistory = new CommandHistory(this);
        buffer = new Buffer();
    }

    /**
     * Gets the new position of cursor and updates the cursor position. It takes the LineBreak as one character
     * and increment position in each line break by 1
     * so first character in the second line of the text has the position of: (number of character in first line + 1)
     * position 0 is the place before first character
     *
     * @param position is the new position that this function will put the cursor on
     *                 the position should be less than buffer length
     */
    public void updateCursorPosition(int position) {
        if (position >= 0 && position <= buffer.getSize()) {
            cursorPosition = position;
        }
    }

    public void insertChar(String character) {
        //TODO: implementation needed
            /*
            if(already selected text)
            {
                first delete selection

            }*/
        buffer.insertAtPosition(character, cursorPosition);
        cursorPosition++;
    }

    public void deleteInDirection(int delDirection) {
        //TODO: implementation needed
            /*
            if(already selected text)
            {
                 delete selection

            }
            else: */
        if (delDirection == DeleteCommand.DEL_FORWARDS) {
            // "backspace" key was used, cursorPosition will not change
            if (cursorPosition < buffer.getSize()) {
                buffer.deleteAtPosition(cursorPosition);
            }
        } else if (delDirection == DeleteCommand.DEL_BACKWARDS) {
            // "backspace" key was used, cursorPosition is decremented
            if (cursorPosition > 0) {
                buffer.deleteAtPosition(cursorPosition - 1);
                cursorPosition--;
            }
        }
    }

    public void updateSelection(int start, int end) {

    }

    public void cutSelection() {
        clipboard = buffer.getCopy(selectionStart, selectionEnd);
        buffer.deleteInterval(selectionStart, selectionEnd);
        cursorPosition = selectionStart;

        // reset selection
        selectionStart = 0;
        selectionEnd = 0;
        // TODO: Or is 'isTextSelected = false;' better?
    }

    public void copySelection() {
        clipboard = buffer.getCopy(selectionStart, selectionEnd);
        cursorPosition = selectionStart;
    }

    public void pasteClipboard() {
        //TODO: implementation needed
            /*
            if(already selected text)
            {
                first delete selection

            }*/

        if (clipboard != null && clipboard.getSize() > 0) {
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
}
