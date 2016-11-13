package commands;

import engine.Engine;

/**
 * This class represents the InsertCommand that is build and run when user
 * decides to type printable characters in the text editor. For every key
 * stroke that generates a character a new command of this type is executed.
 */
public class InsertCommand implements Command {
    char character;

    /**
     * The constructer takes the typed character as parameter.
     *
     * @param character that is typed.
     */
    public InsertCommand(char character) {
        this.character = character;
    }

    public void execute(Engine engine) {
        engine.insertChar(character);
        engine.getRecordModule().record(this);
    }
}
