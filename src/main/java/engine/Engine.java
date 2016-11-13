package engine;

import commands.Command;
import commands.DeleteCommand;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Aidan on 10/10/2016.
 */
public class Engine implements IEngine, Observable, MementoOriginator {
    // Observer pattern
    private List<EngineObserver> observers;

    // Facade pattern
    private RecordModule recordModule;
    private UndoModule undoModule;
    private SpellCheckModule spellCheckModule;

    // Facade pattern
    private Buffer buffer;
    private Buffer clipboard;

    private int cursorPosition = 0;

    private int selectionBase = 0;
    private int selectionEnd = 0;
    private boolean isTextSelected = false;

    public Engine() {
        buffer = new Buffer();
        clipboard = new Buffer();

        observers = new ArrayList<>();
        recordModule = new RecordModule();
        undoModule = new UndoModule();
        spellCheckModule = new SpellCheckModule();
    }

    public RecordModule getRecordModule() {
        return recordModule;
    }

    public void insertChar(char c) {
        deleteSelectionIfExists(selectionBase, selectionEnd);

        // Insert typed character
        buffer.insertAtPosition(new TextElement(c), cursorPosition);
        cursorPosition++;
        undoModule.save(createMemento());

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
        undoModule.save(createMemento());
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

    public void selectCurrentWord(int cursorPosition) {
        int selectionBase = buffer.getWordStart(cursorPosition);
        int selectionEnd = buffer.getWordEnd(cursorPosition);
        updateSelection(selectionBase, selectionEnd);
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

        undoModule.save(createMemento());
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

        undoModule.save(createMemento());
    }

    public void undoCommand() {
        Memento memento = undoModule.undo();
        recoverMemento(memento);

        notifyTextChange();
        notifyCursorChange();
        notifySelectionChange();
    }

    public void redoCommand() {
        Memento memento = undoModule.redo();
        recoverMemento(memento);

        notifyTextChange();
        notifyCursorChange();
        notifySelectionChange();
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
        List<TextElement> content = Buffer.convertCharsToTextElements(chars);
        buffer = new Buffer(content);
        cursorPosition = 0;
        isTextSelected = false;

        notifyTextChange();
        notifyCursorChange();
    }

    public void spellCheck(){
        Map<Selection, String> misspelledWords = spellCheckModule.getMisspelledWords(buffer);

        notifyMisspelledWordsChange(new ArrayList<>(misspelledWords.keySet()));
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

    public void recoverMemento(Memento memento) {
        this.buffer = memento.getBuffer();
        this.clipboard = memento.getClipboard();
        this.cursorPosition = memento.getCursorPosition();
        this.selectionBase = memento.getSelectionBase();
        this.selectionEnd = memento.getSelectionEnd();
    }

    public Memento createMemento() {
        return new Memento(
                buffer.getCopy(),
                clipboard.getCopy(),
                cursorPosition,
                selectionBase,
                selectionEnd
        );
    }

    public void registerObserver(EngineObserver engineObserver) {
        observers.add(engineObserver);
    }

    public void unregisterObserver(EngineObserver engineObserver) {
        observers.remove(engineObserver);
    }

    public void notifyTextChange() {
        observers.forEach(o -> o.updateText(buffer.toString()));
        spellCheck();
    }

    public void notifyCursorChange() {
        observers.forEach(o -> o.updateCursor(cursorPosition));
    }

    public void notifySelectionChange() {
        observers.forEach(o -> o.updateSelection(isTextSelected, selectionBase, selectionEnd));
    }

    public void notifyMisspelledWordsChange(List<Selection> selections) {
        observers.forEach(o -> o.updateMisspelledWords(selections));
    }
}
