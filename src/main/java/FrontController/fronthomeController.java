package FrontController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.io.IOException;

public class fronthomeController {


    @FXML
    private Label LocationNameLabel;

    @FXML
    private Label adresseLabel;

    @FXML
    private VBox chosenFruitCard;

    @FXML
    private Label descriptionLabel;

    @FXML
    private GridPane grid;

    @FXML
    private AnchorPane home;

    @FXML
    private ImageView locationimg;

    @FXML
    private ScrollPane scroll;

    @FXML
    void pubonclick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/ListPublications.fxml"));
        Parent newPage = loader.load();
        home.getChildren().add(newPage);


    }
    public void handleReserveButtonClick(ActionEvent actionEvent) {

    }
}
