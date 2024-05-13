package FrontController;

import Entities.Publication;
import Entities.User;
import Services.ServicePublication;
import Services.Session;
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
import java.util.Map;

import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class ListPublicationsController {

    @FXML
    private GridPane pubgrid;
    @FXML
    private VBox publicationBox;

    @FXML
    private Button detailbutton;
    @FXML
    private TextField searchField;

    @FXML
    private Button  addpubButton;

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
                Session session = Session.getInstance();
                Map<String, Object> userSession = session.getUserSession(); // Assuming you have a method to get the current logged-in user
                String roles = userSession.get("roles").toString();
                roles = roles.replace("[", "").replace("]", "").replace("\"", "");
                if ("ROLE_USER".equals(roles)) {
                    addpubButton.setVisible(false);
                }
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
            newStage.setOnCloseRequest(event -> loadPublications());
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void homeonaction(ActionEvent actionEvent) {
        try {

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeGUI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void eventonaction(ActionEvent actionEvent) {
        try {

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventsClient.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void productonaction(ActionEvent actionEvent) {
        try {

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProgFront.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void programonclick(ActionEvent actionEvent) {
        try {

            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProgFront.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void publicationButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/ListPublications.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage1.close();

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void objectiveButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ObjFront.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage1.close();

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



