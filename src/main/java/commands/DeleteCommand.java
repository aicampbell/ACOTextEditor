package commands;

import engine.Engine;
import engine.RecordModule;

/**
 * This class represents the DeleteCommand that is build and run when user
 * decides to delete with the DELETE- or BACK_SPACE-key.
 */
public class DeleteCommand implements Command {
    /**
     * The two possible delete directions that reflect the origin of this command:
     * DELETE- or BACK_SPACE-key.
     */
    public static int DEL_BACKWARDS = 0;
    public static int DEL_FORWARDS = 1;

    int delDirection;

    /**
     * Constructor that accepts the direction of deletion.
     *
     * @param delDirection as integer that is specified in DEL_BACKWARDS and DEL_FORWARDS.
     */
    public DeleteCommand(int delDirection) {
        this.delDirection = delDirection;
    }

    public void execute(Engine engine) {
        engine.deleteInDirection(delDirection);
        engine.getRecordModule().record(this);
    }
}
