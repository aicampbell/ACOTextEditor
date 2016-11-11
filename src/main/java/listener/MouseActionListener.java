package listener;

import commands.interfaces.Command;
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

    private int selectionStart;
    private int selectionEnd;

    public MouseActionListener(JTextPane textPane, Engine engine) {
        super();
        this.engine = engine;
        this.textPane = textPane;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        e.consume();

        // TODO: Agree on right-click behaviour. With this check-return, we basically disable rightclicks
        /*if (SwingUtilities.isRightMouseButton(e)) {
            return;
        }*/

        if (e.getClickCount() == 2) {
            // TODO: select the whole word in which the cursor is currently placed.
            // TODO: Probably command like SelectCurrentWordCommand.
            // TODO: Why the default double-click behaviour is not to disable? I mean we have e.consume() above...
            //selectionStart = 0;
            //selectionEnd = textPane.getText().length();
            //command = new UpdateSelectionCommand(selectionStart, selectionEnd);
        } else if (e.getClickCount() >= 3) {
            // TODO: or seperate SelectAllCommand?
            selectionStart = 0;
            selectionEnd = textPane.getText().length();
            command = new UpdateSelectionCommand(selectionStart, selectionEnd);
            command.execute(engine);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        e.consume();

        selectionStart = textPane.viewToModel(e.getPoint());
        command = new UpdateCursorCommand(selectionStart);
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

        selectionEnd = textPane.viewToModel(e.getPoint());
        command = new UpdateSelectionCommand(selectionStart, selectionEnd);
        command.execute(engine);
    }
}
