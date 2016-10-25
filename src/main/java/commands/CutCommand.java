package commands;

import engine.Engine;

/**
 * Created by mo on 14.10.16.
 */
public class CutCommand implements Command {
    public CutCommand() {
    }

    public void execute(Engine engine) {
        engine.cutSelection();
    }
}
