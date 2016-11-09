package listener;

import commands.Command;
import commands.UpdateCursorCommand;
import commands.UpdateSelectionCommand;
import engine.Engine;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * Created by mo on 09.11.16.
 */
public class CursorListener implements CaretListener {
    private Engine engine;
    private Command command;

    public CursorListener(Engine engine) {
        this.engine = engine;
    }

    public void caretUpdate(CaretEvent caretEvent) {
        int cursorPos = caretEvent.getDot();
        int otherEnd = caretEvent.getMark();

        if(cursorPos != otherEnd) {
            command = new UpdateSelectionCommand(cursorPos, otherEnd);
        } else {
            command = new UpdateCursorCommand(cursorPos);
        }

        command.execute(engine);
    }
}
