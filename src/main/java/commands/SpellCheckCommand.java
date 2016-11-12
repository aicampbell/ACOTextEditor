package commands;

import engine.Engine;

/**
 * Created by aidan on 12/11/16.
 */
public class SpellCheckCommand implements Command {
    public void execute(Engine engine) {
        engine.spellCheck();
    }
}
