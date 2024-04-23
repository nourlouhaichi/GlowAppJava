import Utils.MyDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        MyDatabase bd= MyDatabase.getInstance();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Front/fronthome.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("Front/style.css").toExternalForm());
        primaryStage.setTitle("GLOWAPP");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
