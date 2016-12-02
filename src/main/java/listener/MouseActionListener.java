package listener;

import commands.Command;
import commands.SelectCurrentWordCommand;
import commands.UpdateCursorCommand;
import commands.UpdateSelectionCommand;
import engine.Engine;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by mo on 09.11.16.
 */
public class MouseActionListener extends MouseAdapter {
    private JTextPane textPane;

    private Engine engine;

    private Command command;

    private int selectionEnd;

    public MouseActionListener(JTextPane textPane, Engine engine) {
        super();
        this.engine = engine;
        this.textPane = textPane;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        e.consume();

        if(e.getClickCount() < 2) {
            return;
        }
        if (e.getClickCount() == 2) {
            command = new SelectCurrentWordCommand(getPositionInTextOfMouseEvent(e));
        }
        if (e.getClickCount() >= 3) {
            command = new UpdateSelectionCommand(
                    0,
                    textPane.getText().length()
            );
        }
        command.execute(engine);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        e.consume();

        /**
         * selectionEnd is an instance variable because it is needed
         * in mouseDragged(MouseEvent) to build a Command.
         */
        selectionEnd = getPositionInTextOfMouseEvent(e);
        command = new UpdateCursorCommand(selectionEnd);
        command.execute(engine);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        e.consume();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        e.consume();

        /**
         * Reuse selectionEnd that got assigned in mousePressed(MouseEvent).
         */
        command = new UpdateSelectionCommand(
                getPositionInTextOfMouseEvent(e),
                selectionEnd
        );
        command.execute(engine);
    }

    private int getPositionInTextOfMouseEvent(MouseEvent e) {
        return textPane.viewToModel(e.getPoint());
    }
}
