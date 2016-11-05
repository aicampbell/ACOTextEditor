package util;

import commands.*;
import engine.Engine;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.KeyEvent;

/**
 * Created by mo on 05.11.16.
 */
public class KeyHandlerFactory {
    private static String KEY_BACKSPACE = "\b";
    private static String KEY_DELETE = "\u007F";
    private static String KEY_ESCAPE = "\u001B";
    //private static String KEY_BACKSPACE = "\b";


    private Engine engine;

    public KeyHandlerFactory(Engine engine) {
        this.engine = engine;
    }

    public EventHandler<KeyEvent> get(EventType<KeyEvent> type) {
        if(type == KeyEvent.KEY_TYPED) {
            return getTypedKeyHandler();
        } else if (type == KeyEvent.KEY_PRESSED) {
            return getPressedKeyHandler();
        }
        throw new IllegalArgumentException("EventType " + type.toString() + " not known.");
    }

    /**
     * Blacklisting of all typedKeyEvents that aren't typed keys for us.
     *
     * @return
     */
    private EventHandler<KeyEvent> getTypedKeyHandler() {
        return new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                /*
                 * Right now, we only support regular characters and their SHIFT-variant.
                 * Other keys and key combinations with other modifier keys than SHIFT
                 * should be ignored.
                 */
                if (keyEvent.isAltDown() || keyEvent.isControlDown() ||
                        keyEvent.isMetaDown() || keyEvent.isShortcutDown() ||
                        keyEvent.getCharacter().equals(KEY_BACKSPACE) ||
                        keyEvent.getCharacter().equals(KEY_DELETE) ||
                        keyEvent.getCharacter().equals(KEY_ESCAPE)) {
                    keyEvent.consume();
                    return;
                }

                /*
                 * KeyCode can contain more than a char if the key produced a single Unicode
                 * character from outside of the Basic Multilingual Plane.
                 * For more info, see https://docs.oracle.com/javase/8/javafx/api/javafx/
                 * scene/input/KeyEvent.html#getCharacter--
                 *
                 * To simplify development, we are disrespecting these cases though and
                 * assume that getCharacter() always only returns one char at a time by using charAt(0).
                 */
                Command command = new InsertCommand(keyEvent.getCharacter());
                command.execute(engine);
                // TODO: consume event later, also in getPressedKeyHandler.
                keyEvent.consume();
            }
        };
    }

    /**
     * Whitelisting of all pressedKeyEvents that lead to a command
     *
     * @return
     */
    private EventHandler<KeyEvent> getPressedKeyHandler() {
        return new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                Command command;

                // Deleting
                if (KeyCombinations.DEL_BACKWARDS.match(keyEvent)) {
                    command = new DeleteCommand(DeleteCommand.DEL_BACKWARDS);
                } else if (KeyCombinations.DEL_FORWARD.match(keyEvent)) {
                    command = new DeleteCommand(DeleteCommand.DEL_FORWARDS);
                } else

                    // Moving cursor
                    // TODO: Make sure SHIFT is not pressed, so that this is not part of selecting text with SHIFT+ARROW KEYS. If SHIFT is pressed, the update will be handled by SelectionListener anyway.
                    /*if (keyEvent.getCode().isArrowKey()) {
                        command = new UpdateCursorCommand(textArea.getCaretPosition());
                    } else*/

                    // Copying, cutting, pasting
                    if (KeyCombinations.COPY.match(keyEvent)) {
                        command = new CopyCommand();
                    } else if (KeyCombinations.CUT.match(keyEvent)) {
                        command = new CutCommand();
                    } else if (KeyCombinations.PASTE.match(keyEvent)) {
                        command = new PasteCommand();
                    } else

                        // Undo, redo
                        if (KeyCombinations.UNDO.match(keyEvent)) {
                            command = new UndoCommand();
                        } else if (KeyCombinations.REDO.match(keyEvent)) {
                            command = new RedoCommand();
                        } else {
                            keyEvent.consume();
                            return;
                        }

                command.execute(engine);

                // TODO: consume event later. because we need to update the UI from the Engine (observer design pattern)
                keyEvent.consume();
            }
        };
    }
}
