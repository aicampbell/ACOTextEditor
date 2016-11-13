package commands;

import engine.Engine;
import engine.RecordModule;

/**
 * This class represents the PasteCommand that is build and run when user
 * decides to paste the current clipboard into the editor. The current state
 * of the Engine decides where the text is pasted.
 */
public class PasteCommand implements Command {
    public PasteCommand() {
    }

    public void execute(Engine engine) {
        engine.pasteClipboard();
        engine.getRecordModule().record(this);
    }
}
