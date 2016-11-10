package engine;

/**
 * Created by mo on 14.10.16.
 */
public interface EngineI {
    void insertChar(char character);

    void deleteInDirection(int delDirection);

    void updateCursor(int position);

    void updateSelection(int start, int end);

    void cutSelection();

    void copySelection();

    void pasteClipboard();

    void undoCommand();

    void redoCommand();

    void toggleRecording();

    void replay();
}
