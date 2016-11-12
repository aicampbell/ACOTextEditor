package engine;

import java.util.List;

/**
 * Created by mo on 14.10.16.
 */
public interface IEngine {
    void insertChar(char character);

    void deleteInDirection(int delDirection);

    void updateCursor(int position);

    void updateSelection(int base, int end);

    void expandSelection(int newEnd);

    void cutSelection();

    void copySelection();

    void pasteClipboard();

    void undoCommand();

    void redoCommand();

    void startRecording();

    void stopRecording();

    void replayRecording();

    void openFile(List<Character> chars);
}