package commands;

import engine.Engine;

import java.io.File;
import java.util.List;

/**
 * Created by aidan on 18/11/16.
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
