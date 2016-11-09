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
public class KeyboardListener implements KeyListener {
    // TODO: Do we need these here?
    /*private static String KEY_BACKSPACE = "\b";
    private static String KEY_DELETE = "\u007F";
    private static String KEY_ESCAPE = "\u001B";*/

    private Engine engine;
    private Command command;

    public KeyboardListener(Engine engine) {
        this.engine = engine;
    }

    public void keyTyped(KeyEvent keyEvent) {

    }

    public void keyPressed(KeyEvent e) {
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
        /*else if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_UP) {
            // make sure this kind of selection events are not consumed, so that caretListener is invoked correctly. First make sure if caretListener also works when we consume the event here (?)
        } */
        else {
            return;
        }
        command.execute(engine);
    }

    public void keyReleased(KeyEvent keyEvent) {

    }
}
