package commands;

import engine.Engine;

/**
 * Created by mo on 12.11.16.
 */
public class SelectCurrentWordCommand implements Command {
    int position;

    public SelectCurrentWordCommand(int position) {
        this.position = position;
    }

    public void execute(Engine engine) {
        engine.selectCurrentWord(position);
    }
}
