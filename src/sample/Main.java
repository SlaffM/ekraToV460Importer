package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("import ekra variables to v460");
        primaryStage.setScene(new Scene(root, 450, 380));
        primaryStage.show();
        Controller controller = loader.getController();
        controller.setStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
