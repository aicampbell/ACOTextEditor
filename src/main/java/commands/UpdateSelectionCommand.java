package commands;

import engine.Engine;

/**
 * Created by mo on 26.10.16.
 */
public class UpdateSelectionCommand implements Command {
    int start;
    int end;

    public UpdateSelectionCommand(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public void execute(Engine engine) {
        engine.updateSelection(start, end);
    }
}
