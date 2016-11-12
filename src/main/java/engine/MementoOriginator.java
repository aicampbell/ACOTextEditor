package engine;

/**
 * Created by mo on 12.11.16.
 */
public interface MementoOriginator {
    void recoverMemento(Memento memento);

    Memento createMemento();
}
