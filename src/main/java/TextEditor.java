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
import listener.SelectionListener;
import util.KeyCombinations;

import java.io.IOException;

public class TextEditor extends Application {
    private Stage stage;
    private Scene scene;
    private BorderPane borderPane;
    private TextArea textArea;
    private Engine engine;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        engine = new Engine();

        // Setup UI components: TextArea, BorderPane, Stage, Scene, etc.
        setupUI();

        // Register all required handlers and listeners for event-based user input processing.
        registerHandlerAndListeners(textArea, engine);

        stage.show();
    }

    private void setupUI() {
        textArea = getTextArea();
        borderPane = getPane(textArea);
        setupMenu(borderPane);
        scene = getScene(borderPane);
        stage = getStage(scene);
    }

    private void registerHandlerAndListeners(TextArea textArea, Engine engine) {
        // KEY_TYPED event is triggered when a valid Unicode-character got generated.
        // See https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html
        textArea.addEventHandler(KeyEvent.KEY_TYPED, getTypedKeyHandler());
        // KEY_PRESSED event is triggered when any key is pressed. Compared to KEY_TYPED,
        // this is a low-level mechanism
        textArea.addEventHandler(KeyEvent.KEY_PRESSED, getPressedKeyHandler());
        // TODO: Add MouseHandler that triggers UpdateCursorCommands. Within mouseHandler we need to make sure that text-selections done with mouse are not respected (is done in SelectionListener).
        // Takes care of selection and cursor updates
        textArea.selectionProperty().addListener(new SelectionListener(engine));

        // TODO: What about this context menu that opens when right-clicking in textArea? Disable that? Handle later?
        //textArea.contextMenuProperty()....
    }

    private BorderPane getPane(TextArea textArea) {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(textArea);
        borderPane.setBottom(new Text("Cursor position here"));

        // TODO: styles and shit

        return borderPane;
    }

    private TextArea getTextArea() {
        TextArea textArea = new TextArea();

        // TODO: styles and shit

        return textArea;
    }

    private Scene getScene(BorderPane borderPane) {
        Scene scene = new Scene(borderPane, 360, 360);

        // TODO: styles and shit

        return scene;
    }

    private Stage getStage(Scene scene) {
        Stage stage = new Stage();
        stage.setTitle("Text Editor");
        stage.setScene(scene);

        // TODO: styles and shit

        return stage;
    }

    private void setupMenu(BorderPane borderPane) {
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
                 * Other key combinations with other modifier keys than SHIFT, are ignored.
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
                Command command = new InsertCommand(keyEvent.getCharacter());
                command.execute(engine);
                // TODO: consume event later, also in getPressedKeyHandler.
                //keyEvent.consume();
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
                    // TODO: Make sure SHIFT is not pressed, so that this is not part of selecting text with SHIFT+ARROW KEYS. If SHIFT is pressed, the update will be handled by SelectionListener anyway.
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
                            //keyEvent.consume();
                            return;
                        }

                command.execute(engine);

                // TODO: consume event later. because we need to update the UI from the Engine (observer design pattern)
                //keyEvent.consume();
            }
        };
    }

    /**
     * This method is used from inside Engine. A part of Observer pattern.
     *
     * @param completeString
     */
    public void updateUI(String completeString) {
        // TODO: Implement proper Observer pattern...
        textArea.setText(completeString);
    }
}
