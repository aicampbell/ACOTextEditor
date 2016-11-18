package engine;

/**
 * This interface specifies the API of an object responsible for undo-
 * and redo-actions.
 */
public interface IUndoModule {
    /**
     * Saves the provided Memento.
     *
     * @param memento to be saved
     */
    void save(Memento memento);

    /**
     * Returns the Memento which represents the most recent state in the past.
     *
     * @return the most recent Memento
     */
    Memento undo();

    /**
     * Returns the Memento which represents a state in the past that has been
     * previously undone.
     *
     * @return the previously undone Memento
     */
    Memento redo();
}
