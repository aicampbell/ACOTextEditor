package commands;

import engine.Engine;
import engine.RecordModule;

/**
 * This class represents the UndoCommand that is build and run when user
 * decides to undo his previous actions. This doesn't imply that
 * there is a state to undo in the engine.
 */
public class UndoCommand implements Command {
    public UndoCommand() {
    }

    public void execute(Engine engine) {
        engine.undoCommand();
        engine.getRecordModule().record(this);
    }
}
