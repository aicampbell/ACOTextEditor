package engine;

import java.util.List;

/**
 * This interface is part of the Observer design pattern. Implementing
 * classes will be notified (after registration) about various types
 * of state changes in the Engine (text, cursor, selection, misspelled
 * words).
 */
public interface EngineObserver {
    /**
     * Is invoked when text content in Engine changes.
     *
     * @param content new text content
     */
    void updateText(String content);

    /**
     * Is invoked when cursor position in Engine changes.
     *
     * @param position new cursor position
     */
    void updateCursor(int position);

    /**
     * Is invoked when the text selection in Engine changes.
     *
     * @param active boolean specifying if selection is active
     * @param selection object of the selection
     */
    void updateSelection(boolean active, Selection selection);

    /**
     * Is invoked when a spell check is performed. The misspelled
     * words are in our implementation not considered as a
     * state of the Engine but an information that is provided by
     * the Engine upon running a spell check. This is mainly
     * because this feature is independent of all other Engine
     * functionality.
     *
     * @param selections list of text selections in which words
     *                   are considered misspelled
     */
    void updateMisspelledWords(List<Selection> selections);
}
