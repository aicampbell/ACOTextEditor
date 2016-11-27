package engine;

import commands.Command;
import commands.DeleteCommand;
import io.FileIO;
import org.assertj.core.util.VisibleForTesting;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents the backend (engine) of the text editor. It's main
 * purpose is to provide an API for the fronted that is specified in {@link IEngine}.
 * <p>
 * It maintains the current state and uses various modules to extend its
 * capabilities. It receives commands that are part of the Command design
 * pattern, does state transformations and notifies the UI about changed
 * states.
 */
public class Engine implements IEngine, Observable, MementoOriginator {
    /**
     * Observers that are notified when the state changed. In our case, the
     * list only contains one EngineObserver which is the ui.GUI class
     * (frontend).
     */
    private List<EngineObserver> observers;

    /**
     * This instance of RecordModule is used to realize Phase 2 of the project.
     * The facade pattern is used to hide implementation details at this top
     * level of the Engine.
     */
    private RecordModule recordModule;

    /**
     * This instance of UndoModule is used to realize Phase 3 of the project.
     * The facade pattern is used to hide implementation details at this top
     * level of the Engine.
     */
    private UndoModule undoModule;

    /**
     * This instance of SpellCheckModule is used implement spell checking.
     * It is an optional feature that we added to the feature set.
     * The facade pattern is used to hide implementation details at this top
     * level of the Engine.
     */
    private SpellCheckModule spellCheckModule;

    /**
     * Represents the current state of the text content.
     */
    private Buffer buffer;

    /**
     * Represents the current state of the clipboard. The clipboard is also
     * of type Buffer since it must be able to store a port or the whole of
     * the variable {@link Engine#buffer}.
     */
    private Buffer clipboard;

    /**
     * Indicates the current position of the cursor in the text.
     */
    private int cursorPosition = 0;

    /**
     * Indicates the current selection start and end index in the text.
     */
    private Selection selection;

    /**
     * Indicates if {@link Engine#selection} describe an active selection or
     * if these values can be ignored.
     */
    private boolean isTextSelected = false;

    List<Selection> misspelledWordSelections;

    /**
     * Constructor instantiates all instance objects.
     */
    public Engine() {
        buffer = new Buffer();
        clipboard = new Buffer();
        selection = new Selection();

        observers = new ArrayList<>();
        recordModule = new RecordModule();
        undoModule = new UndoModule();
        spellCheckModule = new SpellCheckModule();
    }

    /************************************************************
     * State transformations invoked by the different Commands. *
     ************************************************************/

    /**
     * Inserts passed character at the cursor position of the current Engine's state.
     *
     * @param c character to be inserted.
     */
    public void insertChar(char c) {
        deleteSelectionIfExists(selection);

        /** Insert typed character */
        buffer.insertAtPosition(c, cursorPosition);
        cursorPosition++;
        undoModule.save(createMemento());

        /**
         * Notify observers about the changed elements between old state and new state.
         * In this case, by typing a character the text and cursor position changes.
         */
        notifyTextChange();
        notifyCursorChange();
    }

    /**
     * Invokes a delete action on the text. If and what kind of delete operation
     * can be applied, depends on some factors:
     * <ul>
     * <li>Does a selection exist that needs to be deleted (independent of the fact if BACK_SPACE or DELETE is pressed (reflected in parameter {@code delDirection}).</li>
     * <li>Should a single character be deleted? If so, {@code delDirection} is important to give the direction of deletion relative to the current cursor position.</li>
     * <li>Is there a character to delete at the current position? E.g. pressing BACK_SPACE at {@link Engine#cursorPosition} 0 should neither modify the current state nor give an exception.</li>
     * </ul>
     *
     * @param delDirection an abstraction that determines if BACK_SPACE or DELETE was pressed by the user.
     */
    public void deleteInDirection(int delDirection) {
        /** If there is an active selection, only delete that. */
        if (deleteSelectionIfExists(selection)) {
            notifyTextChange();
            notifyCursorChange();
        } else if (delDirection == DeleteCommand.DEL_FORWARDS) {
            /**
             * DELETE was used, so the character at {@link Engine#cursorPosition} should be deleted.
             * cursorPosition doesn't change.
             */
            buffer.deleteAtPosition(cursorPosition);
            notifyTextChange();
            notifyCursorChange();
        } else if (delDirection == DeleteCommand.DEL_BACKWARDS) {
            /**
             * BACK_SPACE was used, so the previous character at
             * {@link Engine#cursorPosition}-1 should be deleted.
             * {@link Engine#cursorPosition} is decremented.
             */
            buffer.deleteAtPosition(cursorPosition - 1);

            /**
             * Only move cursor and notify UI about it when {@link Engine#cursorPosition}
             * is not 0. If it is 0, we are not allowed to move the cursor and therefore
             * we also don't need to notify the UI.
             */
            if (cursorPosition > 0) {
                cursorPosition--;
                notifyTextChange();
                notifyCursorChange();
            }
        }
        undoModule.save(createMemento());
    }

    /**
     * Updates the cursor, reflected by variable cursorPosition.
     *
     * @param position new cursor position that leads to a state transformation.
     */
    public void updateCursor(int position) {
        cursorPosition = position;
        isTextSelected = false;
        selection.clear();

        notifyCursorChange();
    }

    /**
     * Updates the selection with two position indexes.
     *
     * @param base position at which user initiated the selection.
     * @param end  position at which user completed/ended the selection.
     */
    public void updateSelection(int base, int end) {
        /** Make sure it is a real selection where both provided indexes differ from each other. */
        if (base != end) {
            /** Assign selection properties. */
            selection.setSelectionBase(base);
            selection.setSelectionEnd(end);
            isTextSelected = true;

            /**
             * Since the user makes a selection by dragging the mouse or using the keyboard,
             * every selection is started and ended at two given positions where both
             * {@code start}<{@code end} and {@code start}>{@code end} can be true.
             * However {@code end} represents always to most recent position of interaction.
             * Therefore we set the {@link Engine#cursorPosition} to this value.
             */
            cursorPosition = end;

            notifySelectionChange();
        } else {
            /**
             * Need for selections created by SHIFT + ARROW_KEYS where selection is reduced to 0-length
             * after having a selection of at least size==1. Also see {@link Engine#expandSelection(int)}
             * method.
             */
            updateCursor(base);
        }
    }

    /**
     * Updates the end position of a selection or creates a new selection based on the value
     * of currentPosition.
     *
     * @param newEnd new end position of a given or new selection.
     */
    public void expandSelection(int newEnd) {
        /**
         * If there exists a selection, expand it. If not, create a new selection
         * where the selection base position will be {@link Engine#cursorPosition}.
         */
        if (isTextSelected) {
            updateSelection(selection.getSelectionBase(), newEnd);
        } else {
            updateSelection(cursorPosition, newEnd);
        }
    }

    /**
     * Gives a possible selection of a word or sequence of whitespace characters when
     * either one is double-clicked in the text editor.
     *
     * @param cursorPosition at which user performed the double-click.
     */
    public void selectCurrentWord(int cursorPosition) {
        int selectionBase = buffer.getWordStart(cursorPosition);
        int selectionEnd = buffer.getWordEnd(cursorPosition);
        updateSelection(selectionBase, selectionEnd);
    }

    /**
     * Copies selected text to the clipboard in case a selection exists.
     */
    public void copySelection() {
        if (isTextSelected) {
            clipboard = buffer.getCopy(selection);
        }
    }

    /**
     * Copies selected text in the clipboard and removes selected text from {@link Engine#buffer}.
     */
    public void cutSelection() {
        copySelection();
        deleteSelectionIfExists(selection);

        notifyTextChange();
        notifyCursorChange();

        undoModule.save(createMemento());
    }

    /**
     * Pastes content of the clipboard to {@link Engine#buffer} at {@link Engine#cursorPosition}.
     */
    public void pasteClipboard() {
        if (clipboard != null && !clipboard.isEmpty()) {
            /**
             * Existing selections are overwritten by pasting. Therefore we have to delete
             * the selection first.
             */
            deleteSelectionIfExists(selection);

            int clipboardSize = clipboard.getSize();
            buffer.insertAtPosition(clipboard, cursorPosition);

            /** Places the cursor at the end of the pasted text. */
            cursorPosition += clipboardSize;
        }

        notifyTextChange();
        notifyCursorChange();

        undoModule.save(createMemento());
    }

    /**
     * Recovers the most recent state (Memento) of the engine if applicable.
     */
    public void undoCommand() {
        Memento memento = undoModule.undo();
        recoverMemento(memento);

        notifyTextChange();
        notifyCursorChange();
        notifySelectionChange();
    }

    /**
     * Rolls the most recent recovered state (Memento) back if applicable.
     * Represents the inverse method of {@link Engine#undoCommand()} but works only if
     * {@link Engine#undoCommand()} has been previously invoked without creating new
     * state transformations on the Engine. See also {@link UndoModule}.
     */
    public void redoCommand() {
        Memento memento = undoModule.redo();
        recoverMemento(memento);

        notifyTextChange();
        notifyCursorChange();
        notifySelectionChange();
    }

    /**
     * Starts recording a macro by using a instance of {@link RecordModule}.
     */
    public void startRecording() {
        recordModule.clear().start();
    }

    /**
     * Stops recording a macro by using a instance of {@link RecordModule}.
     */
    public void stopRecording() {
        recordModule.stop();
    }

    /**
     * Replays previously recorded macro if there exists a recorded macro.
     */
    public void replayRecording() {
        List<Command> commands = recordModule.getReplayList();

        /**
         * A null-check for the list is not needed since we make sure in {@link RecordModule}
         * that null is never returned for this method.
         */
        for (Command command : commands) {
            command.execute(this);
        }
    }

    /**
     * Pastes the content of a file in the text editor and initializes the state accordingly.
     *
     * @param chars list of characters with which the engine needs to be filled.
     */
    public void openFile(List<Character> chars) {
        buffer = new Buffer(chars);
        cursorPosition = 0;
        isTextSelected = false;

        notifyTextChange();
        notifyCursorChange();
    }

    /**
     * Saves the current text state to a file.
     *
     * @param file object in which text state is written to
     */
    public void saveFile(File file) {
        try {
            FileIO.saveContentToFile(file, buffer.getContent());
        } catch (IOException e) {
            System.out.println("Error while saving buffer content to a file.");
            e.printStackTrace();
        }
    }

    /**
     * Performs a complete spell check on all words currently written in the text editor.
     */
    public void spellCheck() {
        /**
         * The {@link Engine#spellCheckModule} expects the current text as input and returns
         * a map of misspelled words. Each entry of the map contains the position (start
         * and end position) and value of a misspelled word.
         */
        misspelledWordSelections = spellCheckModule.getMisspelledWords(buffer);

        notifyMisspelledWordsChange(misspelledWordSelections);
    }

    /**
     * Helper method that deletes the currently selected text in case there is a selection.
     *
     * @param selection object to be deleted.
     * @return true if a selection has been deleted, or false if no selection is active.
     * The return value can be used in the caller's code to have a feedback about the
     * outcome of invoking this method.
     */
    private boolean deleteSelectionIfExists(Selection selection) {
        int base = selection.getSelectionBase();
        int end = selection.getSelectionEnd();

        if (isTextSelected) {
            buffer.deleteInterval(base, end);
            cursorPosition = base < end ? base : end;
            isTextSelected = false;
            return true;
        }
        return false;
    }

    /**
     * Recovers a given Memento object such that the new Engine state reflects
     * the given Memento.
     *
     * @param memento object to be restored.
     */
    public void recoverMemento(Memento memento) {
        this.buffer = memento.getBuffer();
        this.clipboard = memento.getClipboard();
        this.selection = memento.getSelection();
        this.cursorPosition = memento.getCursorPosition();
    }

    /**
     * Creates a Memento object out of the current state of the Engine that can be
     * stored for later use (e.g. recovery).
     *
     * @return created Memento object.
     */
    public Memento createMemento() {
        return new Memento(
                buffer.getCopy(),
                clipboard.getCopy(),
                selection.getCopy(),
                cursorPosition
        );
    }

    /**
     * Registers an observer so that he is updated in the future when relevant events happen.
     *
     * @param engineObserver to be added to the list of observers that want to be notified.
     */
    public void registerObserver(EngineObserver engineObserver) {
        observers.add(engineObserver);
    }

    /**
     * This method removes an observer from the list of registered observers.
     * It complements the Observer design pattern but is not used in our case.
     *
     * @param engineObserver to be removed from the list of observers.
     */
    public void unregisterObserver(EngineObserver engineObserver) {
        observers.remove(engineObserver);
    }

    /**
     * Notifies each registered observer that the state of the text content has been changed by
     * providing the new state of the text content.
     */
    public void notifyTextChange() {
        observers.forEach(o -> o.updateText(buffer.toString()));
        spellCheck();
    }

    /**
     * Notifies each registered observer that the state of the cursor has been changed by
     * providing the new state of the cursor.
     */
    public void notifyCursorChange() {
        observers.forEach(o -> o.updateCursor(cursorPosition));
    }

    /**
     * Notifies each registered observer that the state of the selection has been changed by
     * providing the new state of the selection.
     */
    public void notifySelectionChange() {
        observers.forEach(o -> o.updateSelection(isTextSelected, selection));
    }

    /**
     * Notifies each registered observer that the state of the list of misspelled words has been
     * changed by providing the new state of said list.
     */
    public void notifyMisspelledWordsChange(List<Selection> selections) {
        observers.forEach(o -> o.updateMisspelledWords(selections));
    }

    public RecordModule getRecordModule() {
        return recordModule;
    }

    @VisibleForTesting
    public int getCursorPosition() {
        return cursorPosition;
    }

    @VisibleForTesting
    public void setCursorPosition(int cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    @VisibleForTesting
    public Buffer getBuffer() {
        return buffer;
    }

    @VisibleForTesting
    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }

    @VisibleForTesting
    public Selection getSelection() {
        return selection;
    }

    @VisibleForTesting
    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    @VisibleForTesting
    public void setIsTextSelected(boolean newValue) {
        this.isTextSelected = newValue;
    }

    public List<Selection> getMisspelledWordSelections() {
        return misspelledWordSelections;
    }

    public Buffer getClipboard() {
        return clipboard;
    }
}
