package commands;

import engine.Engine;

/**
 * Created by mo on 25.10.16.
 */
public class ToggleRecordCommand implements Command {
    public ToggleRecordCommand() {
    }

    public void execute(Engine engine) {
        engine.toggleRecording();
    }
}
