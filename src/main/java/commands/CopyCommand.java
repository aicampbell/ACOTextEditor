package commands;

import engine.Engine;

/**
 * This class represents the CopyCommand that is build and run when user
 * decides to copy a selection of text in the editor.
 */
public class CopyCommand implements Command {
    public CopyCommand() {
    }

    public void execute(Engine engine) {
        engine.copySelection();
        engine.getRecordModule().record(this);
    }
}
