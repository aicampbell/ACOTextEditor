package engine;

/**
 * This interface specifies the Originator component of the Memento design pattern.
 *
 * It is responsible for saving a state into a Memento object and to retrieve a state from
 * a Memento object.
 */
public interface MementoOriginator {
    /**
     * Recovers a state by extracting a given Memento object.
     *
     * @param memento object to be restored
     */
    void recoverMemento(Memento memento);

    /**
     * Creates a Memento object out of the state of the implementing class.
     *
     * @return created Memento object
     */
    Memento createMemento();
}
