package commands;

import engine.Engine;

/**
 * Created by mo on 14.10.16.
 */
public class DeleteCommand implements Command {
    public static int DEL_BACKWARDS = 0;
    public static int DEL_FORWARDS = 1;

    int delDirection;

    public DeleteCommand(int delDirection) {
        this.delDirection = delDirection;
    }

    public void execute(Engine engine) {
        engine.deleteInDirection(delDirection);
    }
}
