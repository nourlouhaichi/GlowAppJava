package Controllers;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import Entities.Category;
import Entities.Event;
import Services.CategoryServices;
import Services.EventServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class UpdateEvent {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField nbPTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField locationTextField;

    @FXML
    private Button submitButton;

    @FXML
    private TextField titleTextField;

    private Event selectedEvent;
    private final CategoryServices x = new CategoryServices();

    @FXML
    void initialize() {
        populateCategoryComboBox();

    }

    public void initData(Event event) {
        selectedEvent = event;
        // Set the fields with the information of the selected event
        titleTextField.setText(selectedEvent.getTitle());
        descriptionTextArea.setText(selectedEvent.getDescription());
        locationTextField.setText(selectedEvent.getLocation());
        datePicker.setValue(selectedEvent.getDate().toLocalDate());
        nbPTextField.setText(String.valueOf(selectedEvent.getNbP()));
        // Set the selected category in the combo box (if applicable)
        categoryComboBox.setValue(selectedEvent.getCategoryId().getTitle());
    }

    private void populateCategoryComboBox() {
        try {
            List<Category> cat = x.afficher();
            ObservableList<String> titles = FXCollections.observableArrayList();

            // Extract first and last names and add them to the userNames list
            for (Category cate : cat) {
                String title = cate.getTitle();
                titles.add(title);
            }

            categoryComboBox.setItems(titles);
        } catch (SQLException e) {
            e.printStackTrace();
        }}


        public void update (ActionEvent event) throws SQLException {

            // Check if any of the fields are empty
            if (titleTextField.getText().isEmpty() || descriptionTextArea.getText().isEmpty() || locationTextField.getText().isEmpty() || datePicker.getValue() == null || categoryComboBox.getValue() == null) {
                // Display an alert informing the user that all fields are required
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("All fields are required!");
                alert.showAndWait();
                return; // Exit the method without updating the event
            }

            // Check if the date is in the past
            if (datePicker.getValue().isBefore(LocalDate.now())) {
                // Display an alert informing the user that the date cannot be in the past
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Date cannot be in the past!");
                alert.showAndWait();
                return; // Exit the method without updating the event
            }
            // Check if the number of places is a valid integer
            try {
                int nbPlaces = Integer.parseInt(nbPTextField.getText());
                if (nbPlaces <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                // Display an alert informing the user that the number of places must be a positive integer
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Number of places must be a positive integer!");
                alert.showAndWait();
                return; // Exit the method without updating the event
            }
            // Update the event information
            selectedEvent.setTitle(titleTextField.getText());
            selectedEvent.setDescription(descriptionTextArea.getText());
            selectedEvent.setLocation(locationTextField.getText());
            selectedEvent.setNbP(Integer.parseInt(nbPTextField.getText()));

            selectedEvent.setDate(datePicker.getValue().atStartOfDay());
            // Update the category information (if applicable)
            // Assuming categoryComboBox contains category titles
            CategoryServices x = new CategoryServices();
            selectedEvent.setCategoryId(x.getCategoryByTitle(categoryComboBox.getValue()));



            try {
                // Call the modifier method to update the event
                EventServices eventServices = new EventServices();
                eventServices.modifier(selectedEvent);

                showAlert(Alert.AlertType.INFORMATION, "Success", "Event UPDATED successfully!");
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
        }
    private String getPhotoPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo");
        // Set the file chooser to only allow image files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        // Show the file chooser dialog and wait for user input
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        // Check if a file was selected
        if (selectedFile != null) {
            // Return the absolute path of the selected file
            return selectedFile.toURI().toString();
        } else {
            // Return null if no file was selected
            return null;
        }}

    public void updatePhoto(ActionEvent event) {
        String photoPath = getPhotoPath();
        selectedEvent.setImageUrl(photoPath);
        EventServices eventServices = new EventServices();
        try {
            eventServices.modifier(selectedEvent);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
