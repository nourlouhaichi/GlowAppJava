package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Entities.Event;
import Entities.Reservation;
import Entities.User;
import Services.EventServices;
import Services.ReservationServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


public class EventsClient {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label LocationNameLabel;

    @FXML
    private Label LocationPriceLabel;

    @FXML
    private Label adresseLabel;

    @FXML
    private VBox chosenFruitCard;

    @FXML
    private Label descriptionLabel;

    @FXML
    private GridPane grid;

    @FXML
    private ImageView locationimg;

    @FXML
    private ScrollPane scroll;

    private int selectedLocationId;
    @FXML
    void handleReserveButtonClick(ActionEvent event) {
        if (selectedLocationId != 0) { // Check if a location is selected
            // Create a new reservation in the database with the selected location ID
            ReservationServices reservationService = new ReservationServices();
            try {
                EventServices ev = new EventServices();
                Event x = ev.getEventById(selectedLocationId);
                Reservation y = new Reservation();
                y.setEvent(x);



                // Assuming user details are set elsewhere in your application
                User us = new User();
                us.setCin("14326585");
                y.setUser(us);

                // Show confirmation alert
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirm Reservation");
                confirmationAlert.setHeaderText(null);
                confirmationAlert.setContentText("Are you sure you want to make this reservation?");

                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // User confirmed reservation, add it to the database
                    reservationService.ajouter(y);
                    // Show success alert
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Reservation successfully added!");
                    successAlert.showAndWait();
                    // Optionally, provide feedback to the user that the reservation was successful
                    // For example: show an alert or update a status label
                } else {
                    // User canceled reservation, do nothing or provide feedback
                }
            } catch (SQLException e) {
                // Handle any exceptions that occur during reservation creation
                e.printStackTrace();
                // Optionally, provide feedback to the user about the error
            }
        } else {
            // If no location is selected, display an alert to the user
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Event Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an event before making a reservation.");
            alert.showAndWait();
        }
    }


    @FXML
    private void handleItemClick(MouseEvent event, int locationId) {
        // Store the selected location ID
        selectedLocationId = locationId;

        // Fetch location details using locationId
        EventServices locationService = new EventServices();
        Event clickedLocation = null;
        try {
            clickedLocation = locationService.getEventById(locationId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Update chosen location
        updateChosenLocation(clickedLocation);

    }
    private void updateChosenLocation(Event location) {
        Label locationNameLabel = (Label) chosenFruitCard.lookup("#LocationNameLabel");
        Label locationPriceLabel = (Label) chosenFruitCard.lookup("#LocationPriceLabel");
        ImageView locationImageView = (ImageView) chosenFruitCard.lookup("#locationimg");
        Label adresseLabel = (Label) chosenFruitCard.lookup("#adresseLabel"); // Add this line
        Label descriptionLabel = (Label) chosenFruitCard.lookup("#descriptionLabel"); // Add this line

        // Update labels with location information
        locationNameLabel.setText(location.getTitle());

        adresseLabel.setText(location.getLocation()); // Set address label text
        descriptionLabel.setText(location.getDescription()); // Set description label text

        // Fetch the image for the selected location

        // Load the image using the URL
        javafx.scene.image.Image fxImage = new javafx.scene.image.Image(location.getImageUrl());
        locationImageView.setImage(fxImage);

        // Handle case where no image is available

    }


    @FXML
    void initialize() throws SQLException {
        // Fetch locations from the database
        EventServices locationService = new EventServices();
        List<Event> locations = locationService.afficher();

        // Initialize column and row counters
        int column = 0;
        int row = 1; // Start displaying images from the second row

        // Populate the grid with items representing each location
        for (Event location : locations) {
            // Fetch the associated image for the location
            // Assuming location.getId() gives the location ID

            if (location != null) {
                String imageUrl = location.getTitle();
                if (imageUrl != null) {
                    imageUrl = location.getImageUrl();
                    if (imageUrl != null) {
                        try {

                            javafx.scene.image.Image fxImage = new javafx.scene.image.Image(imageUrl);

                            // Create a new item controller for the location
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/item.fxml"));
                            AnchorPane itemPane;
                            try {
                                itemPane = fxmlLoader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            ItemController itemController = fxmlLoader.getController();

                            // Set the location information in the item controller
                            itemController.setLocation(location.getTitle(), fxImage);

                            // Set size constraints for the itemPane
                            itemPane.setPrefSize(300, 300); // Adjust size as needed

                            // Add event handler for item click
                            // Add event handler for item click
                            itemPane.setOnMouseClicked(event -> handleItemClick(event, location.getId())); // Pass location ID to handleItemClick

                            // Add the item to the grid
                            grid.add(itemPane, column++, row);
                            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                            grid.setMaxWidth(Region.USE_COMPUTED_SIZE);

                            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                            grid.setMaxHeight(Region.USE_COMPUTED_SIZE);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid image URL: " + imageUrl);
                            // Handle the case where the image URL is invalid
                        }
                    } else {
                        System.out.println("Image URL is null");
                        // Handle the case where the image URL is null
                    }
                } else {
                    System.out.println("Image URL is null");
                    // Handle the case where the image URL is null
                }
            } else {
                // Print "not available" in the console
                System.out.println("Image not available for location: " + location.getTitle());
            }


            // Check if column exceeds the maximum allowed (3)
            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }
}


