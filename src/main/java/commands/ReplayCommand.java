package commands;

import engine.Engine;

/**
 * Created by mo on 25.10.16.
 */
public class ReplayCommand implements Command {
    public ReplayCommand() {
    }

    public void execute(Engine engine) {
        engine.replay();
    }
}
