package Controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import Entities.Category;
import Entities.Event;
import Entities.User;
import Services.CategoryServices;
import Services.Session;
import Services.UserService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.collections.ObservableList;
import Services.EventServices;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.control.Alert;



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
    private TextField Search;

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

    private UserService userService = new UserService();

    @FXML
    void initialize() {
        Session session = Session.getInstance();
        Map<String, Object> userSession = session.getUserSession();
        usernameLabel.setText(userSession.get("email").toString());

        File logoFile = new File("images/logoglowapp.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);

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
        Search.textProperty().addListener((observable, oldValue, newValue) -> {
            filterEvents(newValue);
        });


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
    private void filterEvents(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            populateEventTable();
        } else {
            ObservableList<Event> eventList = eventTableView.getItems();
            FilteredList<Event> filteredData = new FilteredList<>(eventList, event -> event.getTitle().toLowerCase().contains(searchText.toLowerCase()));
            eventTableView.setItems(filteredData);
        }
    }

    @FXML
    void eventButtonOnAction(ActionEvent event) {

        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Events.fxml"));
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
    void categorieButton(ActionEvent event) {

        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListCateforieP.fxml"));
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
    void ProduitButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListProduit.fxml"));
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

    public void homeButtonOnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/backHomeGUI.fxml"));
            Stage homeStage = new Stage();
            //homeStage.initStyle(StageStyle.UNDECORATED);
            homeStage.setScene(new Scene(root,1100,600));
            homeStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) homeButton.getScene().getWindow();
        stage.close();
    }

    public void profileButtonOnAction(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/profileGUI.fxml"));
            Stage profileStage = new Stage();
            profileStage.initStyle(StageStyle.UNDECORATED);
            profileStage.setScene(new Scene(root,800,600));
            profileStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void userButtonOnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/backUserGUI.fxml"));
            Stage userStage = new Stage();
            //userStage.initStyle(StageStyle.UNDECORATED);
            userStage.setScene(new Scene(root,1100,600));
            userStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) userButton.getScene().getWindow();
        stage.close();
    }

    public void frontButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) frontButton.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/homeGUI.fxml"));
            Stage homeStage = new Stage();
            homeStage.initStyle(StageStyle.UNDECORATED);
            homeStage.setScene(new Scene(root,1024,576));
            homeStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logoutButtonOnAction(ActionEvent event) {
        Session.getInstance().logout();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/loginGUI.fxml"));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(new Scene(root,520,400));
            loginStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void publicationButtonOnAction(ActionEvent event) {

        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/backhome.fxml"));
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
    void programButtonOnAction(ActionEvent event) {

        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Programme.fxml"));
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

