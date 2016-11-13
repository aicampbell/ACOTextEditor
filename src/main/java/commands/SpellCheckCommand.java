package commands;

import engine.Engine;

/**
 * This class represents the SpellCheckCommand that is build and run
 * when user decides to run a spell check on the current text in the
 * editor.
 */
public class SpellCheckCommand implements Command {
    public SpellCheckCommand() {
    }

    public void execute(Engine engine) {
        engine.spellCheck();
    }
}
