package commands;

import engine.Engine;
import engine.RecordModule;

/**
 * Created by mo on 14.10.16.
 */
public class RedoCommand implements Command {
    public RedoCommand() {
    }

    public void execute(Engine engine) {
        engine.redoCommand();
        engine.getRecordModule().record(this);
    }
}
