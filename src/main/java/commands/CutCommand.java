package commands;

import engine.Engine;
import engine.RecordModule;

/**
 * This class represents the CutCommand that is build and run when user
 * decides to cut a selection of text in the editor.
 */
public class CutCommand implements Command {
    public CutCommand() {
    }

    public void execute(Engine engine) {
        engine.cutSelection();
        engine.getRecordModule().record(this);
    }
}
