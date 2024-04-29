package Tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeBack.fxml"));


        Parent root =loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("produit Management");

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}