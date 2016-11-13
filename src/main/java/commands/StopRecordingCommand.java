package commands;

import engine.Engine;

/**
 * This class represents the StopRecordingCommand that is build and run
 * when user decides to stop recording a macro.
 */
public class StopRecordingCommand implements Command {
    public StopRecordingCommand() {
    }

    public void execute(Engine engine) {
        engine.stopRecording();
    }
}
