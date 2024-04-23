package Controllers;



import Services.ServiceProgramme;
import Entities.Programme;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class ProgrammeController {
    @FXML private TextField idField;
    @FXML private TextField titreproField;
    @FXML private TextField planproField;
    @FXML private TextField placedispoField;
    @FXML private DatePicker dateproPicker;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;

    @FXML private TableView<Programme> programmeTableView;
    @FXML private TableColumn<Programme, Integer> columnId;
    @FXML private TableColumn<Programme, String> columnTitle;
    @FXML private TableColumn<Programme, String> columnPlan;
    @FXML private TableColumn<Programme, Integer> columnPlaces;
    @FXML private TableColumn<Programme, Date> columnDate;
    private ServiceProgramme serviceProgramme;


    private Programme currentSelectedProgramme;
    private int selectedProgramId;


    @FXML
    public void initialize() {
        serviceProgramme = new ServiceProgramme();
//        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnPlan.setCellValueFactory(new PropertyValueFactory<>("planpro"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("datepro"));
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("titrepro"));
        columnPlaces.setCellValueFactory(new PropertyValueFactory<>("placedispo"));

        loadProgrammeData();
    }




    private void loadProgrammeData() {
        try {
            ObservableList<Programme> programmeData = FXCollections.observableArrayList(serviceProgramme.afficher());
            programmeTableView.setItems(programmeData);
        } catch (SQLException e) {
            showError("Error loading programmes: " + e.getMessage());
        }
    }


    @FXML
    private void clearForm() {
        titreproField.clear();
        planproField.clear();
        placedispoField.clear();
        dateproPicker.setValue(null);
        programmeTableView.getSelectionModel().clearSelection();
    }

//    @FXML
//    public void handleProgrammeSelection() {
//        Programme selectedProgramme = programmeTableView.getSelectionModel().getSelectedItem();
//        if (selectedProgramme != null) {
//            idField.setText(String.valueOf(selectedProgramme.getId()));
//            titreproField.setText(selectedProgramme.getTitrepro());
//            planproField.setText(selectedProgramme.getPlanpro());
//            placedispoField.setText(String.valueOf(selectedProgramme.getPlacedispo()));
//            dateproPicker.setValue(selectedProgramme.getDatepro().toLocalDate());
//        }
//    }


    @FXML
    public void handleProgrammeSelection() {
        Programme selectedProgramme = programmeTableView.getSelectionModel().getSelectedItem();
        if (selectedProgramme != null) {
            selectedProgramId = selectedProgramme.getId();
            titreproField.setText(selectedProgramme.getTitrepro());
            planproField.setText(selectedProgramme.getPlanpro());
            placedispoField.setText(String.valueOf(selectedProgramme.getPlacedispo()));
            dateproPicker.setValue(selectedProgramme.getDatepro().toLocalDate());
        }
    }



    @FXML
    public void addProgramme() {
        if (!validateTitle() || !validatePlan() || !validateAvailablePlaces() || !validateDate()) {
            return;
        }
        try {
            Programme programme = new Programme(
                    0,
                    titreproField.getText(),
                    planproField.getText(),
                    Integer.parseInt(placedispoField.getText()),
                    Date.valueOf(dateproPicker.getValue())
                    );
            serviceProgramme.ajouter(programme);
            loadProgrammeData();
            clearForm();
            showConfirmation("Programme added successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void updateProgramme() {
        if (!validateTitle() || !validatePlan() || !validateAvailablePlaces() || !validateDate()) {
            return;
        }
        try {
            Programme programme = new Programme(
                    selectedProgramId,
                    titreproField.getText(),
                    planproField.getText(),
                    Integer.parseInt(placedispoField.getText()),
                    java.sql.Date.valueOf(dateproPicker.getValue())
                );
            serviceProgramme.modifier(programme);
            loadProgrammeData();
            clearForm();
            showConfirmation("Programme updated successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }




    @FXML
    public void deleteProgramme() {
        try {
            serviceProgramme.supprimer(selectedProgramId);
            loadProgrammeData();
            clearForm();
            showConfirmation("Programme deleted successfully.");

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operation Successful");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operation Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }



    private boolean validateTitle() {
        String title = titreproField.getText().trim();
        if (title.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Title cannot be empty.");
            return false;
        }
        if (title.length() > 15) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Title is too long.");
            return false;
        }
        return true;
    }

    private boolean validatePlan() {
        String plan = planproField.getText().trim();
        if (plan.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Plan cannot be empty.");
            return false;
        }
        return true;
    }

    private boolean validateAvailablePlaces() {
        String availablePlaces = placedispoField.getText().trim();
        try {
            int places = Integer.parseInt(availablePlaces);
            if (places <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Available places must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Available places must be a valid integer.");
            return false;
        }
        return true;
    }

    private boolean validateDate() {
        if (dateproPicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a date.");
            return false;
        }
        if (dateproPicker.getValue().isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "The date cannot be in the past.");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
