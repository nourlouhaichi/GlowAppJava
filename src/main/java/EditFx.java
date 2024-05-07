import Utils.MyDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EditFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        MyDatabase bd= MyDatabase.getInstance();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Front/front.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Edit Publication");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
