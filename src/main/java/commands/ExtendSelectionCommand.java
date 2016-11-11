package commands;

import commands.interfaces.Command;
import engine.Engine;

/**
 * Created by mo on 11.11.16.
 */
public class ExtendSelectionCommand implements Command {
    int end;

    public ExtendSelectionCommand(int end) {
        this.end = end;
    }

    public void execute(Engine engine) {
        engine.expandSelection(end);
        engine.getRecordModule().record(this);
    }
}