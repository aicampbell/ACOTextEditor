package commands;

import engine.Engine;

/**
 * Created by mo on 25.10.16.
 */
public class ReplayRecordingCommand implements Command {
    public ReplayRecordingCommand() {
    }

    public void execute(Engine engine) {
        engine.replayRecording();
    }
}
