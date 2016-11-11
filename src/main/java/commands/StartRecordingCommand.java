package commands;

import commands.interfaces.Command;
import engine.Engine;

/**
 * Created by mo on 25.10.16.
 */
public class StartRecordingCommand implements Command {
    public StartRecordingCommand() {
    }

    public void execute(Engine engine) {
        engine.startRecording();
    }
}
