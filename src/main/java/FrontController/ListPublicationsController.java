package FrontController;

import Entities.Publication;
import Services.ServicePublication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListPublicationsController {

    @FXML
    private GridPane pubgrid;
    @FXML
    private VBox publicationBox;

    @FXML
    private Button detailbutton;
    @FXML
    private TextField searchField;

    private final ServicePublication servicePublication = new ServicePublication();

    public void initialize() {
        try {
            List<Publication> publications = servicePublication.afficher();
            int itemsPerRow = 3;
            HBox row = null;
            for (Publication publication : publications) {
                if (row == null || row.getChildren().size() == itemsPerRow) {
                    row = new HBox();
                    row.setSpacing(10);
                    publicationBox.getChildren().add(row);
                }
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/ShowPublication.fxml"));
                Node publicationNode = loader.load();
                ShowPublicationController controller = loader.getController();
                controller.setSelectedPublication(publication);
                controller.initialize(publication);
                row.getChildren().add(publicationNode);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleSearch(ActionEvent actionEvent) {
        loadPublications();
    }
    public void loadPublications() {
        publicationBox.getChildren().clear();
        String searchKeyword = searchField.getText();
        try {
            List<Publication> publications = servicePublication.searchPublications(searchKeyword);
            int itemsPerRow = 3;
            HBox row = null;
            for (Publication publication : publications) {
                if (row == null || row.getChildren().size() == itemsPerRow) {
                    row = new HBox(10);
                    publicationBox.getChildren().add(row);
                }
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/ShowPublication.fxml"));
                Node publicationNode = loader.load();
                ShowPublicationController controller = loader.getController();
                controller.setSelectedPublication(publication);
                controller.initialize(publication);
                row.getChildren().add(publicationNode);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void addpub(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Back/Publication.fxml"));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root, 400, 400));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



