package commands;

import engine.Engine;

/**
 * This class represents the ReplayRecordingCommand that is build and
 * run when user decides to replay his previously recorded macro.
 * However this doesn't imply that there is a macro available to be
 * played.
 */
public class ReplayRecordingCommand implements Command {
    public ReplayRecordingCommand() {
    }

    public void execute(Engine engine) {
        engine.replayRecording();
    }
}
