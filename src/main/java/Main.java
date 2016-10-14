/**
 * Created by Aidan on 10/10/2016.
 */

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    BorderPane borderPane = new BorderPane();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Stage stage = new Stage();
        stage.setTitle("Stage Title");

        initMenu();

        final TextArea textArea = new TextArea();

        //textArea.addEventFilter();


        // Define an event handler
        EventHandler eventHandler = new EventHandler() {
            public void handle(Event event) {
                System.out.println("Handling event " + event.getEventType());
                KeyEvent ke = (KeyEvent) event;
                KeyCode keyCode = ke.getCode();
                if(KeyCode.LEFT == keyCode) {
                    System.out.println("jo");
                    textArea.positionCaret(textArea.getCaretPosition() -1);
                }

                event.consume();
            }
        };

        textArea.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
        textArea.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) { t.consume(); }
        });

        borderPane.setCenter(textArea);
        borderPane.setBottom(new Text("cursor position here"));

        Scene scene = new Scene(borderPane, 1024, 800);
        stage.setScene(scene);
        stage.show();
    }

    public void initMenu(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/menu.fxml"));
        try {
            borderPane.setTop((Node) fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
