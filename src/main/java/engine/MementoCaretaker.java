package engine;

/**
 * Created by mo on 12.11.16.
 */
public interface MementoCaretaker {
    void save(Memento memento);

    Memento undo();

    Memento redo();
}
