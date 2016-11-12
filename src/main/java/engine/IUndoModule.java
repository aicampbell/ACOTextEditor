package engine;

/**
 * Created by mo on 12.11.16.
 */
public interface IUndoModule {
    void save(Memento memento);

    Memento undo();

    Memento redo();
}
