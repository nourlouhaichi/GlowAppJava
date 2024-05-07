package Controllers;

import Services.ServiceProgramme;
import Entities.Programme;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.stream.Collectors;
import javafx.application.Platform;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Arrays;
import java.awt.Desktop;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import java.nio.charset.StandardCharsets;
import org.controlsfx.control.Notifications;




public class ProgrammeController {
    public Button generatePdfButton;
    @FXML private TextField idField;
    @FXML private TextField titreproField;
    @FXML private TextField planproField;
    @FXML private TextField placedispoField;
    @FXML private DatePicker dateproPicker;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;
    @FXML private TextField searchTextField;
    @FXML private TableView<Programme> programmeTableView;
    @FXML private TableColumn<Programme, Integer> columnId;
    @FXML private TableColumn<Programme, String> columnTitle;
    @FXML private TableColumn<Programme, String> columnPlan;
    @FXML private TableColumn<Programme, Integer> columnPlaces;
    @FXML private TableColumn<Programme, Date> columnDate;
    @FXML private Button uploadImageButton;
    @FXML private String tempImagePath;
    @FXML private ImageView imageView;
    @FXML
    public void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image for Programme");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            tempImagePath = selectedFile.getAbsolutePath();
            imageView.setImage(new Image(selectedFile.toURI().toString())); // Display the selected image in an ImageView
            showConfirmation("Image uploaded successfully.");
        } else {
            showError("No file selected.");
        }
    }
    public void handleProgrammeSelection() {
        Programme selectedProgramme = programmeTableView.getSelectionModel().getSelectedItem();
        if (selectedProgramme != null) {
            selectedProgramId = selectedProgramme.getId();
            titreproField.setText(selectedProgramme.getTitrepro());
            planproField.setText(selectedProgramme.getPlanpro());
            placedispoField.setText(String.valueOf(selectedProgramme.getPlacedispo()));
            dateproPicker.setValue(selectedProgramme.getDatepro().toLocalDate());
            updateImageView(selectedProgramme.getImagePath());
        }
    }

    private void updateImageView(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            }
        } else {
            imageView.setImage(null);
        }
    }

    private void updateProgrammeWithImage(Programme programme) {
        try {
            serviceProgramme.modifier(programme); // Assuming this method now also updates the image path
            loadProgrammeData();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
    private ServiceProgramme serviceProgramme;

    private int selectedProgramId;

    @FXML
    public void generatePDFReport() {
        ServiceProgramme serviceProgramme = new ServiceProgramme();
        try {
            List<Programme> programmeList = serviceProgramme.afficher();
            String htmlContent = convertProgrammeListToHtml(programmeList);
            String apiEndpoint = "https://pdf-api.co/pdf";
            String apiKey = "952DB3D1DA80ED588277A06827311D0A627F";
            String requestBody = String.format("{\"apiKey\": \"%s\", \"html\": \"%s\"}", apiKey, htmlContent);
            HttpResponse<byte[]> response = Unirest.post(apiEndpoint)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .asBytes();

            if (response.getStatus() == 200 && "application/pdf".equals(response.getHeaders().getFirst("Content-Type"))) {
                Path path = Paths.get("ProgramReport.pdf");
                Files.write(path, response.getBody());
                System.out.println("PDF Generated at: " + path.toAbsolutePath());

                if (Desktop.isDesktopSupported()) {
                    try {
                        File pdfFile = new File(path.toString());
                        Desktop.getDesktop().open(pdfFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        System.err.println("Unable to open the PDF file. Error: " + ex.getMessage());
                    }
                } else {
                    System.err.println("Desktop operations not supported on the current platform. Cannot open the PDF file automatically.");
                }
            } else {
                System.err.println("Failed to generate PDF: " + response.getStatusText());
                System.err.println("Status Code: " + response.getStatus());
                System.err.println("Response Headers: " + response.getHeaders());
                String responseBody = new String(response.getBody(), StandardCharsets.UTF_8);
                System.err.println("Response Body: " + responseBody);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }




    private String convertProgrammeListToHtml(List<Programme> programmeList) {
        StringBuilder htmlBuilder = new StringBuilder();

        // Begin HTML
        htmlBuilder.append("<html><body>");
        htmlBuilder.append("<h1>Programs Report</h1>");
        htmlBuilder.append("<table border='1'><tr><th>Title</th><th>Plan</th><th>Available Places</th><th>Date</th></tr>");

        // Add rows for each Programme
        for (Programme prog : programmeList) {
            htmlBuilder.append("<tr>");
            htmlBuilder.append("<td>").append(prog.getTitrepro()).append("</td>");
            htmlBuilder.append("<td>").append(prog.getPlanpro()).append("</td>");
            htmlBuilder.append("<td>").append(prog.getPlacedispo()).append("</td>");
            htmlBuilder.append("<td>").append(prog.getDatepro().toString()).append("</td>");
            htmlBuilder.append("</tr>");
        }

        // End HTML
        htmlBuilder.append("</table></body></html>");

        return htmlBuilder.toString();
    }


    @FXML
    public void initialize() {
        serviceProgramme = new ServiceProgramme();

        columnTitle.setCellValueFactory(new PropertyValueFactory<>("titrepro"));
        columnPlan.setCellValueFactory(new PropertyValueFactory<>("planpro"));
        columnPlaces.setCellValueFactory(new PropertyValueFactory<>("placedispo"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("datepro"));

        loadProgrammeData();
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                loadProgrammeData();
            } else {
                searchProgrammeData();
            }
        });
    }




    private void loadProgrammeData() {
        try {
            ObservableList<Programme> programmeData = FXCollections.observableArrayList(serviceProgramme.afficher());
            programmeTableView.setItems(programmeData);
        } catch (SQLException e) {
            showError("Error loading programmes: " + e.getMessage());
        }
    }

    private void searchProgrammeData() {
        String searchText = searchTextField.getText().toLowerCase();
        try {
            ObservableList<Programme> allProgrammes = FXCollections.observableArrayList(serviceProgramme.afficher());
            ObservableList<Programme> filteredList = allProgrammes.stream()
                    .filter(p -> p.getTitrepro().toLowerCase().contains(searchText) ||
                            p.getPlanpro().toLowerCase().contains(searchText))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            programmeTableView.setItems(filteredList);
        } catch (SQLException e) {
            showError("Error filtering programmes: " + e.getMessage());
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

//






    @FXML
    public void addProgramme() {
        if (!validateTitle() || !validatePlan() || !validateAvailablePlaces() || !validateDate()) {
            return;
        }
        try {
            String defaultImagePath = null;
            Programme programme = new Programme(
                    0,
                    titreproField.getText(),
                    planproField.getText(),
                    Integer.parseInt(placedispoField.getText()),
                    Date.valueOf(dateproPicker.getValue()),
                    defaultImagePath
                    );
            serviceProgramme.ajouter(programme);
            loadProgrammeData();
            clearForm();
            showConfirmation("Programme added successfully.");
            Notifications notifications = Notifications.create();
            notifications.text("Program added successfully !");
            notifications.title("Successful");
            notifications.hideAfter(Duration.seconds(6));
            notifications.show();
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
            Programme currentProgramme = programmeTableView.getSelectionModel().getSelectedItem();
            String currentImagePath = (currentProgramme != null) ? currentProgramme.getImagePath() : null;
            Programme programme = new Programme(
                    selectedProgramId,
                    titreproField.getText(),
                    planproField.getText(),
                    Integer.parseInt(placedispoField.getText()),
                    java.sql.Date.valueOf(dateproPicker.getValue()),
                    currentImagePath
            );
            serviceProgramme.modifier(programme);
            loadProgrammeData();
            clearForm();
            showConfirmation("Programme updated successfully.");
            Notifications notifications = Notifications.create();
            notifications.text("Program updated successfully !");
            notifications.title("Successful");
            notifications.hideAfter(Duration.seconds(6));
            notifications.show();
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
            Notifications notifications = Notifications.create();
            notifications.text("Program deleted successfully !");
            notifications.title("Successful");
            notifications.hideAfter(Duration.seconds(6));
            notifications.show();

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




    public void goFront(javafx.event.ActionEvent event) {
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

    public void Progsback(javafx.event.ActionEvent event) {
        try {

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Programme.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void Objback(javafx.event.ActionEvent event) {
        try {

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Objectif.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

