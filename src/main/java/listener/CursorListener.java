package listener;

import commands.Command;
import commands.UpdateSelectionCommand;
import engine.Engine;

import javax.swing.*;
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

    public void caretUpdate(CaretEvent e) {
        final int cursorPos = e.getDot();
        final int otherEnd = e.getMark();

        if(cursorPos != otherEnd) {

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    command = new UpdateSelectionCommand(cursorPos, otherEnd);
                    command.execute(engine);
                }
            });
        }
    }
}
