package commands;

import commands.interfaces.Command;
import engine.Engine;

/**
 * Created by aidan on 12/11/16.
 */
public class SpellCheckCommand implements Command {
    @Override
    public void execute(Engine engine) {
        engine.spellCheck();
    }
}
