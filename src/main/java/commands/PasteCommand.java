package commands;

import engine.Engine;

/**
 * Created by mo on 14.10.16.
 */
public class PasteCommand implements Command {
    public PasteCommand() {
    }

    public void execute(Engine engine) {
        engine.pasteClipboard();
    }
}
