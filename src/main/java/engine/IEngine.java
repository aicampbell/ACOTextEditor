package engine;

import java.util.List;

/**
 * This interface specifies the API between the frontend (ui.GUI) and backend
 * (Engine) of the text editor. To maintain consistency, every listed
 * operation is invoked by a command created by the frontend.
 *
 * The different scenarios that are possible are documented in the implementing class.
 *
 * The methods are invoked each time the user uses expected key or mouse actions.
 * If the method results into a state transformation (or: changes something on the UI)
 * is determined in the implementation of each operation.
 *
 * The operations are of the following types:
 * <ul>
 *     <li>state transformations on the current Engine state (e.g. type a letter)</li>
 *     <li>toggle functionality (e.g. start recording a macro)</li>
 *     <li>providing information about the current Engine state (spell checking)</li>
 * </ul>
 */
public interface IEngine {
    /**
     * Is invoked when user typed a printable character on the keyboard.
     * A printable character can be a letter, digit, whitespace or special character.
     * The position of the insertion is derived from the current state of the
     * Engine and doesn't have to be provided here.
     *
     * @param character to be inserted
     */
    void insertChar(char character);

    /**
     * Is invoked when user pressed a delete key (either DELETE or BACK_SPACE).
     * The difference between both is provided as parameter and named as direction
     * of deletion.
     *
     * @param delDirection if DELETE or BACK_SPACE are pressed
     */
    void deleteInDirection(int delDirection);

    /**
     * Is invoked when user moves the cursor by either clicking with the mouse
     * somewhere in the text or by navigating through the next with ARROW keys.
     *
     * @param position new position of the cursor.
     */
    void updateCursor(int position);

    /**
     * Is invoked when user selects a text either with the mouse by pressing and dragging
     * the cursor or using CTRL + A to select the whole text.
     *
     * @param base starting position of the selection (where user clicks first)
     * @param end end position of the selection (where user releases mouse button
     *            after finishing the drag movement)
     */
    void updateSelection(int base, int end);

    /**
     * Is invoked when user selects text with the keyboard by holding SHIFT and
     * navigating with ARROW keys. By doing that the starting point of the selection
     * doesn't update - only the end position is updated.
     *
     * We decided to move this functionality into an own method since the selection
     * start is determined by the state of the Engine and not known
     * in the ui.GUI since the editor's state is managed by the Engine.
     *
     * @param newEnd new end position of the selection.
     */
    void expandSelection(int newEnd);

    /**
     * Is invoked when user double-clicks anywhere in the text editor. The Engine can
     * then determine based on the clicked position if there is a sequence of
     * whitespace characters or sequence of non-whitespace characters (e.g. a word)
     * selectable.
     *
     * @param position at which the user double-clicked.
     */
    void selectCurrentWord(int position);

    /**
     * Is invoked when user decides to copy a selection into the clipboard, for
     * example by using the combination CTRL + C. The content of the clipboard is
     * evaluated by the Engine's selection and text state.
     */
    void copySelection();

    /**
     * Is invoked when user decides to cut a selection, for example by using the
     * combination CTRL + X. The selection will then be moved to the clipboard
     * and thus deleted in the text editor. The content of the clipboard is
     * evaluated by the Engine's selection and text state.
     */
    void cutSelection();

    /**
     * Is invoked when user decides to paste the contents of the clipboard in the
     * text editor, for example by using CTRL + P. The new state of the Engine is
     * determined by the cursor state and the content of the clipboard.
     */
    void pasteClipboard();

    /**
     * Is invoked when user wants to undo a previous action (a previous state
     * transformation), for example by using CTRL + Z.
     */
    void undoCommand();

    /**
     * Is invoked when user wants to restore an action that was previously undone
     * by {@see undoCommand}, for example by using CTRL + Y.
     */
    void redoCommand();

    /**
     * Is invoked when user wants to start recording a macro. All following actions
     * are then recorded until {@see stopRecording} is invoked to save the macro to
     * replay it later.
     */
    void startRecording();

    /**
     * Is invoked when user wants to stop recording a macro.
     */
    void stopRecording();

    /**
     * Is invoked when user wants to replay a previously recorded macro.
     */
    void replayRecording();

    /**
     * Is invoked when user wants to open a text-base file in the text editor.
     * The newly loaded content is passed as a parameter.
     *
     * @param chars list of characters that overwrite the current state of the
     *              Engine.
     */
    void openFile(List<Character> chars);

    /**
     * Is invoked when user performs a spell check on the current state of the
     * text. The result is that every word that is considered misspelled will be
     * marked with an underline.
     */
    void spellCheck();
}
