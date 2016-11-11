package commands;

import commands.interfaces.Command;
import commands.interfaces.Replayable;
import engine.Engine;
import engine.RecordModule;

/**
 * Created by mo on 14.10.16.
 */
public class InsertCommand implements Command {
    char character;

    public InsertCommand(char character) {
        this.character = character;
    }

    public void execute(Engine engine) {
        engine.insertChar(character);
        engine.getRecordModule().record(this);
    }
}
