package commands;

import commands.interfaces.Command;
import engine.Engine;

import java.util.List;

/**
 * Created by aidan on 11/11/16.
 */
public class OpenCommand implements Command {
    List<Character> chars;

    public OpenCommand(List<Character> chars) {
        this.chars = chars;
    }

    public void execute(Engine engine) {
        engine.openFile(chars);
    }
}
