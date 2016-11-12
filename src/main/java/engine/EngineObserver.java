package engine;

/**
 * Created by mo on 05.11.16.
 */
public interface EngineObserver {
    void updateText(String content);

    void updateCursor(int position);

    void updateSelection(boolean active, int start, int end);
}