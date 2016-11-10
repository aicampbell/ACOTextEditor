package listener;

import commands.*;
import engine.Engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static commands.DeleteCommand.DEL_BACKWARDS;
import static commands.DeleteCommand.DEL_FORWARDS;

/**
 * Created by mo on 09.11.16.
 */
public class KeyActionListener implements KeyListener {
    // TODO: Do we need these here?
    private static char KEY_BACKSPACE = '\b';
    private static char KEY_DELETE = '\u007F';
    private static char KEY_ESCAPE = '\u001B';

    private Engine engine;
    private Command command;

    public KeyActionListener(Engine engine) {
        this.engine = engine;
    }

    public void keyTyped(KeyEvent e) {
        e.consume();

        if (e.getKeyCode() == KEY_ESCAPE ||
                e.getKeyCode() == KEY_DELETE ||
                e.getKeyCode() == KEY_BACKSPACE) {
            return;
        }

        command = new InsertCommand(e.getKeyChar());
        command.execute(engine);
    }

    public void keyPressed(KeyEvent e) {
        e.consume();

        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
            command = new CopyCommand();
        }
        else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_X) {
            command = new CutCommand();
        }
        else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
            command = new PasteCommand();
        }
        else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            command = new DeleteCommand(DEL_FORWARDS);
        }
        else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            command = new DeleteCommand(DEL_BACKWARDS);
        }
        else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            command = new UndoCommand();
        }
        else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Y) {
            command = new RedoCommand();
        }
        else {
            // TODO: arrow key navigation. Only easily possibly with LEFT and RIGHT arrow key. UP and DOWN i don't see a way on how to solve that...
            return;
        }
            // make sure this kind of selection events are not consumed, so that caretListener is invoked correctly. First make sure if caretListener also works when we consume the event here (?)

        command.execute(engine);

    }

    public void keyReleased(KeyEvent keyEvent) {

    }
}
