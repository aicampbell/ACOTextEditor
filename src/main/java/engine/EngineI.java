package engine;

/**
 * Created by mo on 14.10.16.
 */
public interface EngineI {
    void updateCursorPosition(int position);

    void insertChar(char character);

    void deleteInDirection(int delDirection);

    void cutCurrentSelection();

    void copyCurrentSelection();

    void pasteCurrentSelection();

    void undoCommand();

    void redoCommand();

    void startRecording();

    void stopRecording();

    void replayRecording();

    void openFile();

}
