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
 * This class is an implementation of a Swing KeyListener that handles input via a keyboard.
 */
public class KeyActionListener implements KeyListener {
    /**
     * Distance in points between two adjacent text lines in the text editor.
     * Is used to compute the new cursor position when using the ARROW keys for
     * in-text navigation (ARROW_UP and ARROW_DOWN in particular).
     */
    private static int Y_UP = -15;
    private static int Y_DOWN = 15;

    /** Generated character when pressing BACKSPACE */
    private static char KEY_BACKSPACE = '\b';
    /** Generated unicode character when pressing DELETE */
    private static char KEY_DELETE = '\u007F';
    /** Generated unicode character when pressing ESCAPE */
    private static char KEY_ESCAPE = '\u001B';
    /** Generated unicode character when pressing CTRL + C */
    private static char KEY_END_OF_TEXT = '\u0003';
    /** Generated unicode character when pressing CTRL + V */
    private static char KEY_SYNCHRONOUS_IDLE = '\u0016';
    /** Generated unicode character when pressing CTRL + X */
    private static char KEY_CANCEL = '\u0018';
    /** Generated unicode character when pressing CTRL + Z */
    private static char KEY_SUBSTITUTE = '\u001A';
    /** Generated unicode character when pressing CTRL + Y */
    private static char KEY_END_OF_MEDIUM = '\u0019';
    /** Generated unicode character when pressing CTRL + A */
    private static char KEY_START_OF_HEADING = '\u0001';

    private JTextPane textPane;
    private Engine engine;
    private Command command;

    public KeyActionListener(JTextPane textPane, Engine engine) {
        this.textPane = textPane;
        this.engine = engine;
    }

    /**
     * Is invoked when a valid unicode character is associated with the KeyEvent.
     * Also {@see https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html}
     *
     * @param e key event that occured
     */
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

    /**
     * Is invoked when any key on the keyboard is pressed (pushing key down).
     * Also {@see https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html}
     *
     * @param e key event that occured
     */
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

        /** CTRL + A to select everything */
        else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_A) {
            int selectionStart = 0;
            int selectionEnd = textPane.getText().length();
            command = new UpdateSelectionCommand(selectionStart, selectionEnd);
            command.execute(engine);
        }

        /**
         * SHIFT + ARROW_KEY to select adjacent characters (adjacent in row
         * and column).
         */
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
            /**
             * Prevent cursor from jumping to position 0 when cursor is already
             * in first line and ARROW_UP key is pressed.
             */
            if(newPosition == 0 && textPane.getCaret().getMagicCaretPosition().getX() > 3) {
                return;
            }
            command = new UpdateCursorCommand(newPosition);
        }
        else {
            /** If not key combination matches, we don't execute any command and do nothing. */
            return;
        }

        command.execute(engine);
    }

    /**
     * Is invoked when any key on the keyboard is released after pressing it.
     * Also {@see https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html}
     *
     * @param e key event that occured
     */
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Computes the the new cursor position when ARROW keys are used to navigate
     * within the text (both for normal navigation as for text selection with hold
     * SHIFT key).
     *
     * @param yDiff the vertical point offset from bottom to top. Is negative for jumping
     *              a line up and positive for jumping a line down.
     * @return the computed cursor position
     */
    private int getNewCursorPosition(int yDiff) {
        Point p = textPane.getCaret().getMagicCaretPosition();
        Point newPoint = new Point((int)p.getX(), (int)(p.getY() + yDiff));
        return textPane.viewToModel(newPoint);
    }
}
