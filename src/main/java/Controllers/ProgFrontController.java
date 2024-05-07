package Controllers;

import Entities.Programme;
import Services.ServiceProgramme;
import javafx.event.ActionEvent;
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
import java.util.List;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;




public class ProgFrontController {


    @FXML
    private TilePane cardContainer;
    @FXML
    private TextField searchField;

    private ServiceProgramme serviceProgramme = new ServiceProgramme();
    private ObservableList<Node> originalProgCards;

    @FXML
    public void initialize() {
        loadProgCards();
        originalProgCards = FXCollections.observableArrayList(cardContainer.getChildren());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                String query = newValue.trim();
                if (!query.isEmpty()) {
                    List<Programme> searchResults = serviceProgramme.search(query);
                    displaySearchResults(searchResults);
                } else {
                    // If the search field is empty, reset to display the original list of programs
                    resetProgCards();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    private void resetProgCards() {
        cardContainer.getChildren().clear();
        cardContainer.getChildren().addAll(originalProgCards);
    }
    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            try {
                cardContainer.getChildren().clear(); // Clear previous results

                for (Programme prog : serviceProgramme.search(query)) { // Implement search method in ServiceProgramme class
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
        } else {
            // Optionally handle empty search query
        }
    }
    private void displaySearchResults(List<Programme> searchResults) {
        cardContainer.getChildren().clear(); // Clear previous results
        for (Programme prog : searchResults) {
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
            System.out.println(programme.toString());

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

    @FXML
    void productButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Produitfront.fxml"));
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

    public void eventButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventsClient.fxml"));
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

    public void programButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProgFront.fxml"));
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

    @FXML
    void homeButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeGUI.fxml"));
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








