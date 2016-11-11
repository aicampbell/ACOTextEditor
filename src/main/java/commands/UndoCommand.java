package commands;

import commands.interfaces.Command;
import commands.interfaces.Replayable;
import engine.Engine;
import engine.RecordModule;

/**
 * Created by mo on 14.10.16.
 */
public class UndoCommand implements Command, Replayable {
    public UndoCommand() {
    }

    public void execute(Engine engine) {
        engine.undoCommand();
        engine.getRecordModule().record(this);
    }

    public void record(RecordModule recordModule) {
        recordModule.record(this);
    }
}
