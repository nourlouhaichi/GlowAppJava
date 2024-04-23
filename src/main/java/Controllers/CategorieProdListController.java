package Controllers;

import Entities.CategorieProd;
import Entities.Produit;
import Services.CategorieService;
import Services.ProduitService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CategorieProdListController {

    @FXML
    private TableView<CategorieProd> CategoriePTable;

    @FXML
    private TableColumn<CategorieProd, String> DateCatColumn;

    @FXML
    private TableColumn<CategorieProd, String> DescriptionCatPColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button frontButton;

    @FXML
    private Button homeButton;

    @FXML
    private AnchorPane home_form;

    @FXML
    private TableColumn<CategorieProd, Integer> idColumn;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Button logoutButton;

    @FXML
    private AnchorPane main_form;

    @FXML
    private TableColumn<CategorieProd, String> namePColumn;

    @FXML
    private Button profileButton;

    @FXML
    private Button updateButton;


    @FXML
    private Button ProduitButton;
    @FXML
    private Label usernameLabel;

    @FXML
    void initialize() {
        // Initialize the table columns
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        namePColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom_ca()));
        DescriptionCatPColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription_cat()));
        DateCatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreate_date_ca().toString()));

        // Populate the table
        populateCategorieTable();

        //refresh table
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> populateCategorieTable()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void populateCategorieTable() {
        try {
            // Retrieve categories from the database using CategorieService
            CategorieService categorieService = new CategorieService();

            List<CategorieProd> categories = categorieService.show();

            // Display categories in the table
            CategoriePTable.getItems().setAll(categories);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void addButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCat.fxml"));
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



    @FXML
    void deleteButtonOnAction(ActionEvent event) {
        // Get the selected category from the table
        CategorieProd selectedCategory = CategoriePTable.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            // Ask for confirmation before deleting the category
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Category");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Are you sure you want to delete this category?");
            confirmation.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        try {
                            // Call the delete method from CategorieService to delete the category
                            CategorieService categorieService = new CategorieService();
                            categorieService.delete(selectedCategory);

                            // Refresh the table view after deletion
                            refreshTable();

                            // Show a success message
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Category deleted successfully!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            // Handle the exception appropriately
                        }
                    });
        } else {
            // If no category is selected, show an error message
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a category to delete.");
        }
    }
    private void refreshTable() {
        try {
            // Retrieve categories from the database using CategorieService
            CategorieService categorieService = new CategorieService();
            List<CategorieProd> categories = categorieService.show();

            // Display categories in the table
            CategoriePTable.getItems().setAll(categories);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }


    @FXML
    void frontButtonOnAction(ActionEvent event) {

    }

    @FXML
    void getCategorieProd(MouseEvent event) {

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
    void updateButtonOnAction(ActionEvent event) {

    }

    @FXML
    void ProductButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListProduit.fxml"));
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
}
