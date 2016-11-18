import engine.Engine;
import ui.GUI;

/**
 * This class brings frontend (GUI) and backend (Engine) together.
 * Its main method will start the text editor.
 */
public class TextEditor {
    public static void main(String[] args) {
        /** Initialize the Engine. */
        Engine engine = new Engine();

        /** Initialize the GUI, tell it about the Engine instance and start it. */
        GUI gui = new GUI().setEngine(engine);
        gui.start();
    }
}
