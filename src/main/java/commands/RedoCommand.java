package commands;

import commands.interfaces.Command;
import commands.interfaces.Replayable;
import engine.Engine;
import engine.RecordModule;

/**
 * Created by mo on 14.10.16.
 */
public class RedoCommand implements Command, Replayable {
    public RedoCommand() {
    }

    public void execute(Engine engine) {
        engine.redoCommand();
        engine.getRecordModule().record(this);
    }

    public void record(RecordModule recordModule) {
        recordModule.record(this);
    }
}
