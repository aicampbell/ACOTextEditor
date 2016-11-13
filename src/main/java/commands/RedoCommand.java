package commands;

import engine.Engine;
import engine.RecordModule;

/**
 * This class represents the RedoCommand that is build and run when user
 * decides to redo his previously undone actions. This doesn't imply that
 * there is a state to redo in the engine.
 */
public class RedoCommand implements Command {
    public RedoCommand() {
    }

    public void execute(Engine engine) {
        engine.redoCommand();
        engine.getRecordModule().record(this);
    }
}
