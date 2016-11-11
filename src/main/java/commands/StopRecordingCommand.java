package commands;

import commands.interfaces.Command;
import engine.Engine;

/**
 * Created by mo on 11.11.16.
 */
public class StopRecordingCommand implements Command {
    public StopRecordingCommand() {
    }

    public void execute(Engine engine) {
        engine.stopRecording();
    }
}
