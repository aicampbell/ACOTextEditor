package engine.interfaces;

import engine.Memento;

/**
 * This interface specifies the Caretaker component of the Memento design pattern.
 *
 * It is responsible for managing Mementos and it provides an API for undo- and
 * redo-actions.
 */
public interface MementoCaretaker {
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
