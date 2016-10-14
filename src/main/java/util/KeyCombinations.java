package util;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * Created by mo on 14.10.16.
 */
public class KeyCombinations {
    // delete
    public static KeyCodeCombination DEL_BACKWARDS = new KeyCodeCombination(KeyCode.BACK_SPACE);
    public static KeyCodeCombination DEL_FORWARD = new KeyCodeCombination(KeyCode.DELETE);

    // copy, cut, paste
    public static KeyCodeCombination COPY = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
    public static KeyCodeCombination CUT = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);
    public static KeyCodeCombination PASTE = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);

    // undo, redo
    public static KeyCodeCombination UNDO = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
    public static KeyCodeCombination REDO = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
}
