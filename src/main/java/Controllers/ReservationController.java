package Controllers;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import Services.ReservationServices;
import Entities.Reservation;
import javafx.stage.Stage;

public class ReservationController {

    @FXML
    private TableColumn<Reservation, String> create_at;

    @FXML
    private TableColumn<Reservation, String> event;

    @FXML
    private TableView<Reservation> eventTableView;

    @FXML
    private Button frontButton;

    @FXML
    private Button homeButton;

    @FXML
    private AnchorPane home_form;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Button logoutButton;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button profileButton;

    @FXML
    private TableColumn<Reservation, String> qrcode;

    @FXML
    private TableColumn<Reservation, String> user;

    @FXML
    private Button userButton;

    @FXML
    private Label usernameLabel;

    @FXML
    void deleteR(ActionEvent event) {
        // Get the selected reservation from the table view
        Reservation selectedReservation = eventTableView.getSelectionModel().getSelectedItem();

        if (selectedReservation != null) {
            try {
                // Call the delete method from ReservationServices to delete the reservation
                ReservationServices reservationServices = new ReservationServices();
                reservationServices.supprimer(selectedReservation);

                // Refresh the table view after deletion
                initialize();

                // Show a success message
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success");
                success.setHeaderText(null);
                success.setContentText("Reservation deleted successfully!");
                success.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
        } else {
            // If no reservation is selected, show an error message
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText(null);
            error.setContentText("Please select a reservation to delete.");
            error.showAndWait();
        }
    }

    @FXML
    void frontButtonOnAction(ActionEvent event) {
        // Implement action for the frontButton if needed
    }

    @FXML
    void homeButtonOnAction(ActionEvent event) {
        // Implement action for the homeButton if needed
    }

    @FXML
    void logoutButtonOnAction(ActionEvent event) {
        // Implement action for the logoutButton if needed
    }

    @FXML
    void profileButtonOnAction(ActionEvent event) {
        // Implement action for the profileButton if needed
    }

    @FXML
    void userButtonOnAction(ActionEvent event) {
        // Implement action for the userButton if needed
    }

    @FXML
    void initialize() {
        // Initialize columns
        create_at.setCellValueFactory(cellData -> {
            // Format LocalDateTime to String
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = formatter.format(cellData.getValue().getCreatedAt());
            return new SimpleStringProperty(formattedDateTime);
        });
        event.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent().getTitle()));

        user.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getFirstname()));

        // Populate table view with reservation data
        populateReservationTable();
    }

    private void populateReservationTable() {
        try {
            // Retrieve reservation data from the database
            ReservationServices reservationServices = new ReservationServices();
            List<Reservation> reservationList = reservationServices.afficher();

            // Convert the list to observable list
            ObservableList<Reservation> reservationData = FXCollections.observableArrayList(reservationList);

            // Set the data to the table view
            eventTableView.setItems(reservationData);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
    }

    public void Events(ActionEvent event) {
        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Events.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Close the current stage
            currentStage.close();

            // Show the new stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
