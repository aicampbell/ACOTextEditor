package commands;

import engine.Engine;

import java.io.File;

/**
 * This class represents the SaveCommand that is build and
 * run when user decides to save his text to a file.
 */
public class SaveCommand implements Command {
    File file;

    public SaveCommand(File file){
        this.file = file;
    }

    @Override
    public void execute(Engine engine) {
        engine.saveFile(file);
    }
}
