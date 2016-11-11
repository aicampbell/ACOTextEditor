package commands;

import engine.Engine;

/**
 * Created by aidan on 11/11/16.
 */
public class OpenCommand implements Command {

    public void execute(Engine engine) {
        engine.openFile();
    }
}
