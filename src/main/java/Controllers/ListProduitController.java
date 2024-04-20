package Controllers;

import Entities.CategorieProd;
import Entities.Produit;
import Services.ProduitService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListProduitController {
    @FXML
    private TableColumn<Produit, Integer> RefColumn;
    @FXML
    private TableColumn<Produit, String> namePColumn;

    @FXML
    private TableColumn<Produit, String> DescriptionPColumn;

    @FXML
    private TableColumn<Produit, String> PhotoPColumn;

    @FXML
    private TableColumn<Produit, Integer> QuantityPColumn;

    @FXML
    private TableColumn<Produit, Double> PricePColumn;

    @FXML
    private TableColumn<Produit, String> CategoryPColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button frontButton;

    @FXML
    private Button homeButton;

    @FXML
    private AnchorPane home_form;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Button logoutButton;

    @FXML
    private AnchorPane main_form;


    @FXML
    private Button profileButton;


    @FXML
    private Button updateButton;

    @FXML
    private Button userButton;

    @FXML
    private TableView<Produit> ProduitTable;

    @FXML
    private Label usernameLabel;

    @FXML
    void addButtonOnAction(ActionEvent event) {

        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/produit.fxml"));
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
            ProduitService produitService = new ProduitService();
            List<Produit> produits = produitService.show();
            ObservableList<Produit> eventData = FXCollections.observableArrayList(produits);
            ProduitTable.setItems(eventData);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }





    @FXML

    void initialize() {
        // Set cell value factories

        RefColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRef()).asObject());
        namePColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        DescriptionPColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        PhotoPColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImage()));
        QuantityPColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());
        PricePColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());

        CategoryPColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie().getNom_ca()));


        // Populate table view
        populateProduitTable();

        // Refresh table every 10 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> populateProduitTable()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    private void populateProduitTable() {
        // Clear existing items
        ProduitTable.getItems().clear();

        try {

            ProduitService produitCrud = new ProduitService();
            ObservableList<Produit> produitData = FXCollections.observableArrayList();
            // Retrieve and add new items
            List<Produit> produits = produitCrud.show(); // Call show() on the instance
            produitData.addAll(produits);

            // Set items to TableView
            ProduitTable.setItems(produitData);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }


    @FXML
    void clearButtonOnAction(ActionEvent event) {

    }

    @FXML
    void deleteButtonOnAction(ActionEvent event) {

            // Get the selected produit from the table view
            Produit selectedProduit = ProduitTable.getSelectionModel().getSelectedItem();

            if (selectedProduit != null) {
                // Ask for confirmation before deleting the produit
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Delete Product");
                confirmation.setHeaderText(null);
                confirmation.setContentText("Are you sure you want to delete this product?");
                confirmation.showAndWait()
                        .filter(response -> response == ButtonType.OK)
                        .ifPresent(response -> {
                            try {
                                // Call the delete method from ProduitService to delete the produit
                                ProduitService produitService = new ProduitService();
                                produitService.delete(selectedProduit);

                                // Refresh the table view after deletion
                                refreshPage();

                                // Show a success message
                                Alert success = new Alert(Alert.AlertType.INFORMATION);
                                success.setTitle("Success");
                                success.setHeaderText(null);
                                success.setContentText("Product deleted successfully!");
                                success.showAndWait();
                            } catch (SQLException e) {
                                e.printStackTrace();
                                // Handle the exception appropriately
                            }
                        });
            } else {
                // If no produit is selected, show an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select a product to delete.");
                alert.showAndWait();
            }
        }




    @FXML
    void frontButtonOnAction(ActionEvent event) {

    }

    @FXML
    void getProduit(MouseEvent event) {

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

            // Get the selected produit from the table view
            Produit selectedProduit = ProduitTable.getSelectionModel().getSelectedItem();

            if (selectedProduit != null) {
                try {
                    // Load the new FXML file
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/update_produit.fxml"));
                    Parent root = loader.load();

                    // Get the controller of the update produit interface
                    UpdateProduitController updateProduitController = loader.getController();

                    // Initialize the update interface with the selected produit information
                    //updateProduitController.initData(selectedProduit);

                    // Create a new stage
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));

                    // Show the stage
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // If no produit is selected, show an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please select a produit to modify.");
                alert.showAndWait();
            }
        }

    @FXML
    void userButtonOnAction(ActionEvent event) {

    }

}


