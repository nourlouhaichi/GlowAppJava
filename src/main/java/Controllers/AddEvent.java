package Controllers;

import Entities.Category;
import Entities.Event;
import Entities.User;
import Services.CategoryServices;
import Services.EventServices;
import Services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.io.File;

public class AddEvent {

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField participantsTextField;

    @FXML
    private TextField titleTextField;


    private final UserService userService = new UserService();
    private final CategoryServices catus = new CategoryServices();

    @FXML
    void addEvent(ActionEvent event) {
        try {
            // Retrieve data from the UI controls
            String title = titleTextField.getText();
            String description = descriptionTextArea.getText();
            String location = locationTextField.getText();
            LocalDate selectedDate = datePicker.getValue();
            int participants = 0;
            // Validate fields
            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || selectedDate == null || participantsTextField.getText().isEmpty() || categoryComboBox.getValue() == null) {
                // Display error alert for empty fields
                showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
                return;
            }

            // Validate number of participants
            try {
                participants = Integer.parseInt(participantsTextField.getText());
            } catch (NumberFormatException e) {
                // Display error alert for invalid number of participants
                showAlert(Alert.AlertType.ERROR, "Error", "Number of participants must be a valid integer.");
                return;
            }

            // Check if date is in the past
            if (selectedDate.isBefore(LocalDate.now())) {
                // Display error alert for past date
                showAlert(Alert.AlertType.ERROR, "Error", "Date cannot be in the past.");
                return;
            }

            // Get selected user and category from ComboBoxes

            Category selectedCategory = catus.getCategoryByTitle(categoryComboBox.getValue());
            // Retrieve the path of the photo (assuming you have a method to get it)
            String photoPath = getPhotoPath(); // Implement this method to retrieve the photo path


            // Create an Event object with the retrieved data
            Event eventToAdd = new Event();
            eventToAdd.setTitle(title);
            eventToAdd.setDescription(description);
            eventToAdd.setLocation(location);
            eventToAdd.setDate(selectedDate.atTime(0, 0)); // Set time component to midnight
            eventToAdd.setNbP(participants);
            User us = new User();
            us.setCin("14326585");
            eventToAdd.setUserId(us);
            eventToAdd.setCategoryId(selectedCategory);
            eventToAdd.setImageUrl(photoPath); // Set the photo path

            // Call the ajouter method to add the event
            EventServices eventServices = new EventServices();
            eventServices.ajouter(eventToAdd);

            // Optionally, you can display a success message to the user
            showAlert(Alert.AlertType.INFORMATION, "Success", "Event added successfully!");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            // Handle SQL exceptions, e.g., display an error message to the user or log the exception
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the event.");
        }
    }

    // Helper method to show alert
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }




    private void populateCategoryComboBox() {
        try {
            List<Category> cat = catus.afficher();
            ObservableList<String> userNames = FXCollections.observableArrayList();

            // Extract first and last names and add them to the userNames list
            for (Category cate : cat) {
                String fullName = cate.getTitle();
                userNames.add(fullName);
            }

            categoryComboBox.setItems(userNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // You need to implement this method similarly to populate userComboBox
    }

    @FXML
    void initialize() {

        populateCategoryComboBox();
    }


    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

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
        }
    }


}
