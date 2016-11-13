package commands;

import engine.Engine;

/**
 * This class represents the SelectCurrentWordCommand that is build
 * and run when user decides to double-click a word (series of letters)
 * or series of whitespaces.
 */
public class SelectCurrentWordCommand implements Command {
    int position;

    /**
     * The constructor takes the current cursor position as parameter.
     *
     * The Engine computes if at this position is something to select.
     * If so it computes the start and end of the selection.
     *
     * @param position at which the double-click happened.
     */
    public SelectCurrentWordCommand(int position) {
        this.position = position;
    }

    public void execute(Engine engine) {
        engine.selectCurrentWord(position);
    }
}
