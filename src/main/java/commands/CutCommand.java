package commands;

import engine.Engine;
import engine.RecordModule;

/**
 * Created by mo on 14.10.16.
 */
public class CutCommand implements Command {
    public CutCommand() {
    }

    public void execute(Engine engine) {
        engine.cutSelection();
        engine.getRecordModule().record(this);
    }
}
