package commands;

import engine.Engine;

/**
 * Created by mo on 14.10.16.
 */
public class UpdateCursorCommand implements Command {
    int newPosition;

    public UpdateCursorCommand(int newPosition) {
        this.newPosition = newPosition;
    }

    public void execute(Engine engine) {
        engine.updateCursorPosition(newPosition);
    }
}
