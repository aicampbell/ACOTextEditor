package commands;

import commands.interfaces.Command;
import commands.interfaces.Replayable;
import engine.Engine;
import engine.RecordModule;

/**
 * Created by mo on 14.10.16.
 */
public class DeleteCommand implements Command, Replayable {
    public static int DEL_BACKWARDS = 0;
    public static int DEL_FORWARDS = 1;

    int delDirection;

    public DeleteCommand(int delDirection) {
        this.delDirection = delDirection;
    }

    public void execute(Engine engine) {
        engine.deleteInDirection(delDirection);
        engine.getRecordModule().record(this);
    }

    public void record(RecordModule recordModule) {
        recordModule.record(this);
    }
}
