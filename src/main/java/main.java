/**
 * Created by Aidan on 10/10/2016.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Stage stage = new Stage();
        stage.setTitle("Stage Title");

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 1024, 800);

        stage.setScene(scene);
        stage.show();
    }
}
