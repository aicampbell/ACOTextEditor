/**
 * Created by Aidan on 10/10/2016.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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
