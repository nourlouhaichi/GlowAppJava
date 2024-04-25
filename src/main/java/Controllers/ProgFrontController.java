package Controllers;

import Entities.Programme;
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

public class ProgFrontController {


    @FXML
    private TilePane cardContainer;

    private ServiceProgramme serviceProgramme = new ServiceProgramme();



    @FXML
    public void initialize() {
        loadProgCards();
    }

    private void loadProgCards() {
        try {
            for (Programme prog : serviceProgramme.afficher()) {
                VBox card = new VBox(10);
                card.setPadding(new Insets(10));
                card.setStyle("-fx-border-color: black; -fx-border-width: 2;");

                Label titleLabel = new Label("title: " + prog.getTitrepro());
                Label planLabel = new Label("plan: " + prog.getPlanpro());
                Button detailsButton = new Button("View Details");
                detailsButton.setOnAction(e -> showProgDetails(prog));

                card.getChildren().addAll(titleLabel, planLabel, detailsButton);
                cardContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showProgDetails(Programme programme) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProgDetail.fxml"));
            Parent root = loader.load();

            ProgDetailController controller = loader.getController();
            controller.setProg(programme);

            Stage stage = new Stage();
            stage.setTitle("Prog Details");
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
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            currentStage.close();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}








