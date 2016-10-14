package commands;

import engine.Engine;

/**
 * Created by mo on 14.10.16.
 */
public class RedoCommand implements Command {
    public RedoCommand() {
    }

    public void execute(Engine engine) {
        engine.redoCommand();
    }
}
