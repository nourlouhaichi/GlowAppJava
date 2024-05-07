package Controllers;

import Entities.CategorieProd;

import Services.CategorieService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class UpdateCategorieController {

    @FXML
    private Button CancelButton;

    @FXML
    private TextArea DescripTextfield;

    @FXML
    private Button UpdateCatButton;

    @FXML
    private DatePicker createdateField;

    @FXML
    private TextField nomCatTextfield;

    private CategorieProd selectedCategorieProd  ;

    public void initData(CategorieProd categorieProd) {
        selectedCategorieProd = categorieProd;
        // Set the fields with the information of the selected product
        nomCatTextfield.setText(selectedCategorieProd.getNom_ca());
        createdateField.setValue(selectedCategorieProd.getCreate_date_ca().toLocalDate());
        DescripTextfield.setText(selectedCategorieProd.getDescription_cat());

    }

    @FXML
    void CancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    void UpdateCatButtonOnAction(ActionEvent event) {
        if (nomCatTextfield.getText().isEmpty() || DescripTextfield.getText().isEmpty() || createdateField.getValue() == null) {
            // Display an alert informing the user that all fields are required
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required!");
            return;
        }

        // Check if the date is in the past
        if (createdateField.getValue().isBefore(LocalDate.now())) {
            // Display an alert informing the user that the date cannot be in the past
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Date cannot be in the past!");
            alert.showAndWait();
            return; // Exit the method without updating the event
        }

        // Update the event information
        selectedCategorieProd.setNom_ca(nomCatTextfield.getText());
        selectedCategorieProd.setDescription_cat(DescripTextfield.getText());
        selectedCategorieProd.setCreate_date_ca(createdateField.getValue().atStartOfDay());

        try {
            // Call the modifier method to update the event
            CategorieService categorieService = new CategorieService();
            categorieService.update(selectedCategorieProd);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Event UPDATED successfully!");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
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

