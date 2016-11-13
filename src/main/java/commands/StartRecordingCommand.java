package commands;

import engine.Engine;

/**
 * This class represents the StartRecordingCommand that is build and run
 * when user decides to start recording a macro.
 */
public class StartRecordingCommand implements Command {
    public StartRecordingCommand() {
    }

    public void execute(Engine engine) {
        engine.startRecording();
    }
}
