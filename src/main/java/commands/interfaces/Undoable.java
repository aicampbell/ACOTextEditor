package commands.interfaces;

import engine.UndoModule;

/**
 * Created by mo on 11.11.16.
 */
public interface Undoable {
    void undo(UndoModule undoModule);
}
