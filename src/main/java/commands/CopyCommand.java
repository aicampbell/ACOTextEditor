package commands;

import engine.Engine;

/**
 * Created by mo on 14.10.16.
 */
public class CopyCommand implements Command {
    public CopyCommand() {
    }

    public void execute(Engine engine) {
        engine.copySelection();
    }
}
