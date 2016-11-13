package commands;

import engine.Engine;

import java.util.List;

/**
 * This class represents the OpenCommand that is build and run when user
 * decides to open a text-based file from the menu of the editor.
 */
public class OpenCommand implements Command {
    List<Character> chars;

    /**
     * The constructor takes a List<Character> that is extracted from the file.
     * The transformation of the file into the list is done before building this
     * command.
     *
     * @param chars the list of characters to be loaded into the text editor.
     */
    public OpenCommand(List<Character> chars) {
        this.chars = chars;
    }

    public void execute(Engine engine) {
        engine.openFile(chars);
    }
}
