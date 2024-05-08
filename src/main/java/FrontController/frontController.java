package FrontController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class frontController {

    @FXML
    private Button eventButton;

    @FXML
    private ImageView homeImageView;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Button logoutButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button shopButton;

    @FXML
    private Label userLabel;



    public void logoutButtonOnAction(javafx.event.ActionEvent actionEvent) {
    }

    public void profileButtonOnAction(ActionEvent actionEvent) {
    }

    public void shopButtonOnAction(ActionEvent actionEvent) {
    }

    public void eventButtonOnAction(ActionEvent actionEvent) {
    }

    @FXML
    void publicationButtonOnclick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Front/ListPublications.fxml"));
        Stage newStage = new Stage();
        Scene scene = new Scene(root, 690, 650);
        newStage.setScene(scene);
        newStage.show();

    }
}
