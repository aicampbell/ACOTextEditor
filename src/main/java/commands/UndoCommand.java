package commands;

import engine.Engine;

/**
 * Created by mo on 14.10.16.
 */
public class UndoCommand implements Command {
    public UndoCommand() {
    }

    public void execute(Engine engine) {
        engine.undoCommand();
    }
}
