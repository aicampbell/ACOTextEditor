package engine;

/**
 * Created by mo on 14.10.16.
 */
public interface EngineI {
    void updateCursorPosition(int position);

    void insertChar(String character);

    void deleteInDirection(int delDirection);

    void updateSelection(int start, int end);

    void cutSelection();

    void copySelection();

    void pasteClipboard();

    void undoCommand();

    void redoCommand();

    void toggleRecording();

    void replay();
}
