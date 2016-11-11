package commands;

import commands.interfaces.Command;
import commands.interfaces.Replayable;
import engine.Engine;
import engine.RecordModule;

/**
 * Created by mo on 14.10.16.
 */
public class CutCommand implements Command, Replayable {
    public CutCommand() {
    }

    public void execute(Engine engine) {
        engine.cutSelection();
        engine.getRecordModule().record(this);
    }

    public void record(RecordModule recordModule) {
        recordModule.record(this);
    }
}
