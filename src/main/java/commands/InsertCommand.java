package commands;

import engine.Engine;

/**
 * Created by mo on 14.10.16.
 */
public class InsertCommand implements Command {
    String character;

    public InsertCommand(String character) {
        this.character = character;
    }

    public void execute(Engine engine) {
        engine.insertChar(character);
    }
}
