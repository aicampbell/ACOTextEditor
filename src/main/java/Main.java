/**
 * Created by Aidan on 10/10/2016.
 */

import commands.*;
import engine.Engine;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.KeyCombinations;

import java.io.IOException;

public class Main extends Application {
    BorderPane borderPane;
    TextArea textArea;
    private Engine engine;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Stage stage = new Stage();
        stage.setTitle("Text Editor");

        setupTextArea();
        setupPane();
        setupMenu();

        // KEY_TYPED event is triggered when a valid Unicode-character got generated.
        // See https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html
        textArea.addEventHandler(KeyEvent.KEY_TYPED, getTypedKeyHandler());
        // KEY_PRESSED event is triggered when any key is pressed. Compared to KEY_TYPED,
        // this is a low-level mechanism
        textArea.addEventHandler(KeyEvent.KEY_PRESSED, getPressedKeyHandler());

        Scene scene = new Scene(borderPane, 1024, 800);
        stage.setScene(scene);
        stage.show();
    }

    private void setupPane() {
        borderPane = new BorderPane();
        borderPane.setCenter(textArea);
        borderPane.setBottom(new Text("Cursor position here"));
        // ... styles and shit
    }

    private void setupTextArea() {
        textArea = new TextArea();
        // ... styles and shit
    }

    private void setupMenu() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/menu.fxml"));
        try {
            borderPane.setTop((Node) fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private EventHandler<KeyEvent> getTypedKeyHandler() {
        return new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                /*
                 * Right now, we only support regular characters and their SHIFT-variant.
                 * Other key combinatations with other modifier keys than SHIFT, are ignored.
                 */
                if (keyEvent.isAltDown() || keyEvent.isControlDown() ||
                        keyEvent.isMetaDown() || keyEvent.isShortcutDown()) {
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
                Command command = new InsertCommand(keyEvent.getCharacter().charAt(0));
                command.execute(engine);
            }
        };
    }

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
                    if (keyEvent.getCode().isArrowKey()) {
                        command = new UpdateCursorCommand(textArea.getCaretPosition());
                    } else

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
                keyEvent.consume();
            }
        };
    }

    /**
     * This method is used from inside Engine. A part of Observer pattern.
     *
     * @param completeString
     */
    public void updateUI(String completeString) {
        textArea.setText(completeString);
    }
}
