/**
 * Created by Aidan on 10/10/2016.
 */

import engine.Engine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import listener.SelectionListener;
import util.EngineObserver;
import util.KeyHandlerFactory;

import java.io.IOException;

public class TextEditor extends Application implements EngineObserver {
    private Stage stage;
    private Scene scene;
    private BorderPane borderPane;
    private HBox hBox;
    private Label cursorPosition;
    private Label selActive;
    private Label selStart;
    private Label selEnd;

    private TextArea textArea;

    private Engine engine;
    private KeyHandlerFactory keyHandlerFactory;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        engine = new Engine();
        engine.attachObserver(this);
        keyHandlerFactory = new KeyHandlerFactory(engine);

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
        // Takes care of selection and cursor updates. Only for such updates that are not triggered by insertCommand and deleteCommand (which are all updates caused by mouse or keyboard SHIFT+ARROW
        textArea.selectionProperty().addListener(new SelectionListener(engine));

        // KEY_TYPED event is triggered when a valid Unicode-character got generated.
        // See https://docs.oracle.com/javase/7/docs/api/java/awt/event/KeyEvent.html
        textArea.addEventHandler(KeyEvent.KEY_TYPED, keyHandlerFactory.get(KeyEvent.KEY_TYPED));

        // KEY_PRESSED event is triggered when any key is pressed. Compared to KEY_TYPED,
        // this is a low-level mechanism
        textArea.addEventHandler(KeyEvent.KEY_PRESSED, keyHandlerFactory.get(KeyEvent.KEY_PRESSED));


        // TODO: What about this context menu that opens when right-clicking in textArea? Disable that? Handle later?
        //textArea.contextMenuProperty()....
    }

    private BorderPane getPane(TextArea textArea) {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(textArea);

        hBox = new HBox();

        cursorPosition = new Label("0");
        selActive = new Label("false");
        selStart = new Label("0");
        selEnd = new Label("0");

        hBox.getChildren().add(new Label("Cursor: "));
        hBox.getChildren().add(cursorPosition);
        hBox.getChildren().add(new Label(", Selection: "));
        hBox.getChildren().add(selStart);
        hBox.getChildren().add(new Label("->"));
        hBox.getChildren().add(selEnd);
        hBox.getChildren().add(new Label(" ("));
        hBox.getChildren().add(selActive);
        hBox.getChildren().add(new Label(")"));

        borderPane.setBottom(hBox);

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

    private void setupBottomBar(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/LowerMenu.fxml"));
        try {
            hBox.getChildren().add((Node) fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void updateText(String text) {
        //textArea.setText(text);
    }

    public void updateCursor(int position) {
        //textArea.positionCaret(position);
        cursorPosition.setText(String.valueOf(position));
    }

    public void updateSelection(boolean active, int start, int end) {
        selActive.setText(String.valueOf(active));
        selStart.setText(String.valueOf(start));
        selEnd.setText(String.valueOf(end));
    }
}
