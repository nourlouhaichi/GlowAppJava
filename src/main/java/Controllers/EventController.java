package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import Entities.Category;
import Entities.Event;
import Entities.User;
import Services.CategoryServices;
import Services.UserService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.collections.ObservableList;
import Services.EventServices;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EventController {

    @FXML
    private TextField textCategory;

    @FXML
    private TableView<Event> eventTableView;

    @FXML
    private TableColumn<Event, String> titleColumn;

    @FXML
    private TableColumn<Event, String> descriptionColumn;

    @FXML
    private TableColumn<Event, String> locationColumn;

    @FXML
    private TableColumn<Event, String> dateColumn;

    @FXML
    private TableColumn<Event, Integer> nbPColumn;

    @FXML
    private TableColumn<Event, String> userIdColumn;

    @FXML
    private TableColumn<Event, String> categoryIdColumn;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<?> CategoryE;

    @FXML
    private DatePicker DateE;

    @FXML
    private TextArea descriptionE;

    @FXML
    private Button frontButton;

    @FXML
    private Button homeButton;

    @FXML
    private AnchorPane home_form;

    @FXML
    private TextField localE;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Button logoutButton;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button profileButton;

    @FXML
    private TextField titreE;

    @FXML
    private Button userButton;

    @FXML
    private Label usernameLabel;

    @FXML
    void addEvent(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_event.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void refreshPage() {
        try {
            EventServices eventDAO = new EventServices();
            List<Event> eventList = eventDAO.afficher();
            ObservableList<Event> eventData = FXCollections.observableArrayList(eventList);
            eventTableView.setItems(eventData);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }





    @FXML
    void homeButtonOnAction(ActionEvent event) {

    }

    @FXML
    void logoutButtonOnAction(ActionEvent event) {

    }



    @FXML
    void profileButtonOnAction(ActionEvent event) {

    }

    @FXML
    void userButtonOnAction(ActionEvent event) {

    }

    private UserService userService = new UserService();

    @FXML
    void initialize() {
        // Initialize columns
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        nbPColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNbP()).asObject());
        userIdColumn.setCellValueFactory(cellData -> {
            String firstName = cellData.getValue().getUserId().getFirstname();
            String lastName = cellData.getValue().getUserId().getLastname();
            String fullName = firstName + " " + lastName;
            return new SimpleStringProperty(fullName);
        });

        categoryIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoryId().getTitle()));


        // Populate table view
        populateEventTable();
        //refresh table
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> populateEventTable()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    void deleteE(ActionEvent event) {
        // Get the selected event from the table view
        Event selectedEvent = eventTableView.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            // Ask for confirmation before deleting the event
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Event");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Are you sure you want to delete this event?");
            confirmation.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        try {
                            // Call the delete method from EventServices to delete the event
                            EventServices eventServices = new EventServices();
                            eventServices.supprimer(selectedEvent);

                            // Refresh the table view after deletion
                            refreshPage();

                            // Show a success message
                            Alert success = new Alert(Alert.AlertType.INFORMATION);
                            success.setTitle("Success");
                            success.setHeaderText(null);
                            success.setContentText("Event deleted successfully!");
                            success.showAndWait();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            // Handle the exception appropriately
                        }
                    });
        } else {
            // If no event is selected, show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an event to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    void modifyEvent(ActionEvent event) {
        // Get the selected event from the table view
        Event selectedEvent = eventTableView.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            try {
                // Load the new FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/update_event.fxml"));
                Parent root = loader.load();

                // Get the controller of the update event interface
                UpdateEvent updateEventController = loader.getController();

                // Initialize the update interface with the selected event information
                updateEventController.initData(selectedEvent);

                // Create a new stage
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                // Show the stage
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // If no event is selected, show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an event to Modify.");
            alert.showAndWait();
            // Show an error message or handle the case when no event is selected
        }}

    private void populateEventTable() {
        try {
            EventServices eventDAO = new EventServices();
            List<Event> eventList = eventDAO.afficher();
            ObservableList<Event> eventData = FXCollections.observableArrayList(eventList);
            eventTableView.setItems(eventData);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    @FXML
    void addCategory(ActionEvent event) {
        // Retrieve the name of the new category from the textCategory TextField
        String categoryName = textCategory.getText().trim();

        if (!categoryName.isEmpty()) {
            try {
                // Call the method from CategoryServices to add the new category to the database
                Category newCategory = new Category(categoryName);
                CategoryServices categoryServices = new CategoryServices();
                categoryServices.ajouter(newCategory);

                // Optionally, display a success message
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success");
                success.setHeaderText(null);
                success.setContentText("Category added successfully!");
                success.showAndWait();

                // Clear the text field after adding the category
                textCategory.clear();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
        } else {
            // Display an error message if the category name is empty
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText(null);
            error.setContentText("Please enter a category name.");
            error.showAndWait();
        }

    }

    public void Reservation(ActionEvent event) {
        try {
            // Get the current stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReservartionBack.fxml"));
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

