package commands;

import engine.Engine;
import engine.RecordModule;

/**
 * Created by mo on 14.10.16.
 */
public class PasteCommand implements Command {
    public PasteCommand() {
    }

    public void execute(Engine engine) {
        engine.pasteClipboard();
        engine.getRecordModule().record(this);
    }
}
