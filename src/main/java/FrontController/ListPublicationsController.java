package FrontController;

import Entities.Publication;
import Services.ServicePublication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListPublicationsController {

    @FXML
    private GridPane pubgrid;
    @FXML
    private VBox publicationBox;

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
                controller.initialize(publication);
                row.getChildren().add(publicationNode);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

}
