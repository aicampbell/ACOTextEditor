package listener;

import commands.*;
import commands.Command;
import engine.Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static commands.DeleteCommand.DEL_BACKWARDS;
import static commands.DeleteCommand.DEL_FORWARDS;

/**
 * Created by mo on 09.11.16.
 */
public class KeyActionListener implements KeyListener {
    private static int Y_UP = -15;
    private static int Y_DOWN = 15;

    // TODO: Do we need these here?
    private static char KEY_BACKSPACE = '\b';
    private static char KEY_DELETE = '\u007F';
    private static char KEY_ESCAPE = '\u001B';
    private static char KEY_END_OF_TEXT = '\u0003'; // produced artifact when pressing CTRL+C (since it is used to abort something / signalling end of stream / ^C)
    private static char KEY_SYNCHRONOUS_IDLE = '\u0016'; // produced artifact when pressing CTRL+V
    private static char KEY_CANCEL = '\u0018'; // produced artifact when pressing CTRL+X
    private static char KEY_SUBSTITUTE = '\u001A'; // produced artifact when pressing CTRL+Z
    private static char KEY_END_OF_MEDIUM = '\u0019'; // produced artifact when pressing CTRL+Y
    private static char KEY_START_OF_HEADING = '\u0001'; // produced artifact when pressing CTRL+A

    private JTextPane textPane;

    private Engine engine;

    private Command command;

    public KeyActionListener(JTextPane textPane, Engine engine) {
        this.textPane = textPane;
        this.engine = engine;
    }

    public void keyTyped(KeyEvent e) {
        e.consume();

        if (e.getKeyChar() == KEY_ESCAPE ||
                e.getKeyChar() == KEY_DELETE ||
                e.getKeyChar() == KEY_BACKSPACE ||
                e.getKeyChar() == KEY_END_OF_TEXT ||
                e.getKeyChar() == KEY_SYNCHRONOUS_IDLE ||
                e.getKeyChar() == KEY_CANCEL ||
                e.getKeyChar() == KEY_SUBSTITUTE ||
                e.getKeyChar() == KEY_END_OF_MEDIUM ||
                e.getKeyChar() == KEY_START_OF_HEADING) {
            return;
        }

        command = new InsertCommand(e.getKeyChar());
        command.execute(engine);
    }

    public void keyPressed(KeyEvent e) {
        e.consume();

        /** COPY, CUT, and PASTE */
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
            command = new CopyCommand();
        }
        else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_X) {
            command = new CutCommand();
        }
        else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
            command = new PasteCommand();
        }

        /** DELETE */
        else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            command = new DeleteCommand(DEL_FORWARDS);
        }
        else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            command = new DeleteCommand(DEL_BACKWARDS);
        }

        /** UNDO and REDO */
        else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            command = new UndoCommand();
        }
        else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Y) {
            command = new RedoCommand();
        }

        /** CTRL + A select everything */
        else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A) {
            int selectionStart = 0;
            int selectionEnd = textPane.getText().length();
            command = new UpdateSelectionCommand(selectionStart, selectionEnd);
            command.execute(engine);
        }

        /** SHIFT + ARROW_KEY selection */
        else if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_LEFT) {
            int currentPosition = textPane.getCaretPosition();
            int newSelectionEnd = Math.max(0, currentPosition - 1);
            command = new ExtendSelectionCommand(newSelectionEnd);
        }
        else if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_RIGHT) {
            int currentPosition = textPane.getCaretPosition();
            int newSelectionEnd = Math.max(0, currentPosition + 1);
            command = new ExtendSelectionCommand(newSelectionEnd);
        }
        else if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_DOWN) {
            int newSelectionEnd = getNewCursorPosition(Y_DOWN);
            command = new ExtendSelectionCommand(newSelectionEnd);
        }
        else if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_UP) {
            int newSelectionEnd = getNewCursorPosition(Y_UP);
            command = new ExtendSelectionCommand(newSelectionEnd);
        }

        /** ARROW_KEY navigation */
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            int newPosition = Math.max(0, textPane.getCaretPosition() - 1);
            command = new UpdateCursorCommand(newPosition);
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            int newPosition = Math.min(textPane.getText().length(), textPane.getCaretPosition() + 1);
            command = new UpdateCursorCommand(newPosition);
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            int newPosition = getNewCursorPosition(Y_DOWN);
            command = new UpdateCursorCommand(newPosition);
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            int newPosition = getNewCursorPosition(Y_UP);
            // Prevent cursor from jumping to position 0 when cursor is already in first line and ARROW_UP key is pressed.
            if(newPosition == 0 && textPane.getCaret().getMagicCaretPosition().getX() > 3) {
                return;
            }
            command = new UpdateCursorCommand(newPosition);
        }
        else {
            return;
        }

        command.execute(engine);
    }

    public void keyReleased(KeyEvent keyEvent) {

    }

    private int getNewCursorPosition(int yDiff) {
        Point p = textPane.getCaret().getMagicCaretPosition();
        Point newPoint = new Point((int)p.getX(), (int)(p.getY() + yDiff));
        return textPane.viewToModel(newPoint);
    }
}
