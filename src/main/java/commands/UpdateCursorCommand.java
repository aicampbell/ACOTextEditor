package commands;

import engine.Engine;

/**
 * Created by mo on 26.10.16.
 */
public class UpdateCursorCommand implements Command {
    int position;

    public UpdateCursorCommand(int position) {
        this.position = position;
    }

    public void execute(Engine engine) {
        engine.updateCursor(position);
    }
}
