package Controllers;

import Entities.Objectif;
import Entities.Programme;
import Services.ServiceObjectif;
import Services.ServiceProgramme;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ObjFrontController {
    @FXML
    private TilePane cardContainer;

    private ServiceObjectif serviceObjectif = new ServiceObjectif();


    @FXML
    public void initialize() {
        loadObjCards();
    }

    private void loadObjCards() {
        try {
            for (Objectif obj : serviceObjectif.afficher()) {
                VBox card = new VBox(10);
                card.setPadding(new Insets(10));
                card.setStyle("-fx-border-color: black; -fx-border-width: 2;");

                Label objectifLabel = new Label("Objectif : " + obj.getObjectifO());
                Label descriptionLabel = new Label("Description: " + obj.getDescriptionO());
                Button detailsButton = new Button("View Details");
                detailsButton.setOnAction(e -> showObjDetails(obj));

                card.getChildren().addAll(objectifLabel, descriptionLabel, detailsButton);
                cardContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showObjDetails(Objectif objectif) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ObjDetail.fxml"));
            Parent root = loader.load();

            ObjDetailController controller = loader.getController();
            controller.setObj(objectif);

            Stage stage = new Stage();
            stage.setTitle("Objectif Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void backHome(javafx.event.ActionEvent event) {
        try {

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void ObjsFront(javafx.event.ActionEvent event) {
        try {

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ObjFront.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void ProgsFront(javafx.event.ActionEvent event) {
        try {

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProgFront.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}










