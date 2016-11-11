package commands.interfaces;

import engine.Engine;

/**
 * Created by mo on 14.10.16.
 */
public interface Command {
    void execute(Engine engine);

    /** for later versions */
    //void undo();
}
