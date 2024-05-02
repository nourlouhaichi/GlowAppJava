package Controllers;

import Entities.Objectif;
import Services.ServiceObjectif;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.w3c.dom.Text;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectifController {

    public Button generatePdfButton;
    @FXML private TextField idField;
    @FXML private TextField ObjectifField;
    @FXML private TextField DescriptionField;
    @FXML private TextField WeightField;
    @FXML private TextField HeightField;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;
    @FXML private TextField searchTextField;
    @FXML private TableView<Objectif> ObjectifTableView;
    @FXML private TableColumn<Objectif, Integer> columnId;
    @FXML private TableColumn<Objectif, String> columnObjectif;
    @FXML private TableColumn<Objectif, String> columnDescription;
    @FXML private TableColumn<Objectif, Float> columnWeight;
    @FXML private TableColumn<Objectif, Float> columnHeight;

    private ServiceObjectif serviceObjectif;

    private int selectedObjectifId;

    @FXML
    public void generatePDFReport() {
        ServiceObjectif serviceObjectif = new ServiceObjectif();
        try {
            List<Objectif> objectifsList = serviceObjectif.afficher();
            String htmlContent = convertObjectifListToHtml(objectifsList);
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




    private String convertObjectifListToHtml(List<Objectif> objectifList) {
        StringBuilder htmlBuilder = new StringBuilder();

        // Begin HTML
        htmlBuilder.append("<html><body>");
        htmlBuilder.append("<h1>Objectives Report</h1>");
        htmlBuilder.append("<table border='1'><tr><th>Objectif</th><th>Description</th><th>Weight</th><th>Height</th></tr>");

        // Add rows for each Programme
        for (Objectif obj : objectifList) {
            htmlBuilder.append("<tr>");
            htmlBuilder.append("<td>").append(obj.getObjectifO()).append("</td>");
            htmlBuilder.append("<td>").append(obj.getDescriptionO()).append("</td>");
            htmlBuilder.append("<td>").append(obj.getPoidO()).append("</td>");
            htmlBuilder.append("<td>").append(obj.getTailleO()).append("</td>");
            htmlBuilder.append("</tr>");
        }

        // End HTML
        htmlBuilder.append("</table></body></html>");

        return htmlBuilder.toString();
    }


    @FXML
    public void initialize() {
       serviceObjectif = new ServiceObjectif();

        columnObjectif.setCellValueFactory(new PropertyValueFactory<>("objectifO"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("descriptionO"));
        columnWeight.setCellValueFactory(new PropertyValueFactory<>("poidO"));
        columnHeight.setCellValueFactory(new PropertyValueFactory<>("tailleO"));

        loadObjectifData();
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                loadObjectifData();
            } else {
                searchObjectifData();
            }
        });
    }




    private void loadObjectifData() {
        try {
            ObservableList<Objectif> objectifData = FXCollections.observableArrayList(serviceObjectif.afficher());
            ObjectifTableView.setItems(objectifData);
        } catch (SQLException e) {
            showError("Error loading objectives: " + e.getMessage());
        }
    }

    private void searchObjectifData() {
        String searchText = searchTextField.getText().toLowerCase();
        try {
            ObservableList<Objectif> allObjectifs = FXCollections.observableArrayList(serviceObjectif.afficher());
            ObservableList<Objectif> filteredList = allObjectifs.stream()
                    .filter(p -> p.getObjectifO().toLowerCase().contains(searchText) ||
                            p.getDescriptionO().toLowerCase().contains(searchText))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            ObjectifTableView.setItems(filteredList);
        } catch (SQLException e) {
            showError("Error filtering objectives: " + e.getMessage());
        }
    }

    @FXML
    private void clearForm() {
        ObjectifField.clear();
        DescriptionField.clear();
        WeightField.clear();
        HeightField.clear();
        ObjectifTableView.getSelectionModel().clearSelection();
    }

//


    @FXML
    public void handleObjectifSelection() {
        Objectif selectedObjectif = ObjectifTableView.getSelectionModel().getSelectedItem();
        if (selectedObjectif != null) {
            selectedObjectifId = selectedObjectif.getId();
            ObjectifField.setText(selectedObjectif.getObjectifO());
            DescriptionField.setText(selectedObjectif.getDescriptionO());
            WeightField.setText(String.valueOf(selectedObjectif.getPoidO()));
            HeightField.setText(String.valueOf(selectedObjectif.getTailleO()));
        }
    }



    @FXML
    public void addObjectif() {
        if (!validateObjectif() || !validateDescription() || !validateWeight() || !validateHeight()) {
            return;
        }
        try {
            Objectif objectif = new Objectif(
                    0,
                    ObjectifField.getText(),
                    DescriptionField.getText(),
                    Float.parseFloat(WeightField.getText()),
                    Float.parseFloat(HeightField.getText())
            );
            serviceObjectif.ajouter(objectif);
            loadObjectifData();
            clearForm();
            showConfirmation("Objectif added successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void updateObjectif() {
        if (!validateObjectif() || !validateDescription() || !validateWeight() || !validateHeight()) {
            return;
        }
        try {
            Objectif objectif = new Objectif(
                    selectedObjectifId,
                    ObjectifField.getText(),
                    DescriptionField.getText(),
                    Float.parseFloat(WeightField.getText()),
                    Float.parseFloat(HeightField.getText())
            );
            serviceObjectif.modifier(objectif);
            loadObjectifData();
            clearForm();
            showConfirmation("Objectif updated successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }




    @FXML
    public void deleteObjectif() {
        try {
            serviceObjectif.supprimer(selectedObjectifId);
            loadObjectifData();
            clearForm();
            showConfirmation("Objectif deleted successfully.");

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



    private boolean validateObjectif() {
        String title = ObjectifField.getText().trim();
        if (title.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Objectif Title cannot be empty.");
            return false;
        }
        if (title.length() > 70) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Objectif Title is too long.");
            return false;
        }
        return true;
    }

    private boolean validateDescription() {
        String plan = DescriptionField.getText().trim();
        if (plan.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Description cannot be empty.");
            return false;
        }
        return true;
    }

    private boolean validateWeight() {
        String Weight = WeightField.getText().trim();
        try {
            Float weight = Float.parseFloat(Weight);
            if (weight <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Weight must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Weight must be a valid integer.");
            return false;
        }
        return true;
    }

    private boolean validateHeight() {
        String Height = HeightField.getText().trim();
        try {
            Float height = Float.parseFloat(Height);
            if (height <= 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Height must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Height must be a valid integer.");
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


