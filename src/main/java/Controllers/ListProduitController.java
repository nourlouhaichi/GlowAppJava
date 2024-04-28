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
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;



import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;



import java.io.File;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private Button generatePDFButton;
    @FXML
    private Button SearchButton;

    @FXML
    private Button updateButton;


    @FXML
    private Button CategorieButton;

    @FXML
    private TableView<Produit> ProduitTable;
    private List<Produit> produits;


    @FXML
    private Label usernameLabel;


    @FXML
    private TextField searchTextField;
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

        File logoFile = new File("images/logoglowapp.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);
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

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterproduits(newValue);
                });

        // Refresh table every 10 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> populateProduitTable()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    private void populateProduitTable() {
        // Clear existing items
       // ProduitTable.getItems().clear();

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
                    updateProduitController.initData(selectedProduit);

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
    void categorieButton(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListCateforieP.fxml"));
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
        void ProduitButtonOnAction (ActionEvent event){

        }



    private void filterproduits(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            populateProduitTable();
        } else {
            ObservableList<Produit> produitData = ProduitTable.getItems();
            FilteredList<Produit> filteredData = new FilteredList<>(produitData, produit -> produit.getName().toLowerCase().contains(searchText.toLowerCase()));
            ProduitTable.setItems(filteredData);
        }
    }
    private String sanitizeText(String input) {
        // Remove any characters that are not supported by the font's encoding
        return input.replaceAll("[^\\x20-\\x7E]", ""); // Keep only printable ASCII characters
    }

    private void generatePDF(List<Produit> produits) {
        try {
            // Create a new PDF document
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // Initialize the content stream of the page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Define table parameters
            float margin = 50;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yStart = page.getMediaBox().getHeight() - margin;
            float yPosition = yStart;
            float rowHeight = 20f;
            float tableMargin = 10;

            // Define column widths
            float[] columnWidths = {0.2f, 0.3f, 0.3f, 0.2f}; // Adjust these as needed
            float tableHeight = rowHeight * 2 + tableMargin * 3; // Assuming two rows in the header

            // Begin the Content stream
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(margin, yPosition);

            // Add header row
            addTableHeader(contentStream, margin, yPosition, tableWidth, rowHeight, columnWidths);
            yPosition -= rowHeight;

            // Add product details
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            for (Produit produit : produits) {
                yPosition -= rowHeight;
                if (yPosition <= margin) {
                    // New page needed if content exceeds current page
                    contentStream.endText();
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yStart);
                    yPosition = yStart;
                    addTableHeader(contentStream, margin, yPosition, tableWidth, rowHeight, columnWidths);
                    yPosition -= rowHeight;
                }
                addTableRow(contentStream, margin, yPosition, tableWidth, rowHeight, columnWidths, produit);
            }

            // End the content stream
            contentStream.endText();
            contentStream.close();

            // Save the PDF document
            File file = new File("products.pdf");
            document.save(file);
            document.close();

            // Show confirmation message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("PDF document saved successfully!");
            alert.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }

    private void addTableHeader(PDPageContentStream contentStream, float xStart, float yStart, float tableWidth, float rowHeight, float[] columnWidths) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.setLeading(rowHeight);
        float xPosition = xStart;
        contentStream.beginText();
        for (float width : columnWidths) {
            contentStream.newLineAtOffset(xPosition, yStart);
            contentStream.showText("Header"); // Replace with your column headers
            xPosition += width * tableWidth;
        }
        contentStream.endText(); // Move endText() here to properly pair with beginText()
    }

    private void addTableRow(PDPageContentStream contentStream, float xStart, float yStart, float tableWidth, float rowHeight, float[] columnWidths, Produit produit) throws IOException {
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.setLeading(rowHeight);
        float xPosition = xStart;
        contentStream.beginText();
        for (float width : columnWidths) {
            contentStream.newLineAtOffset(xPosition, yStart);
            // Add the product information to the table row
            // Adjust the logic based on your specific requirements
            // For example, you might have produit.getName(), produit.getDescription(), etc.
            contentStream.showText("Product Info");
            xPosition += width * tableWidth;
        }
        contentStream.endText();
    }
    private void populateProduits() {
        try {
            ProduitService produitCrud = new ProduitService();
            produits = produitCrud.show(); // Populate produits list
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }
    // Add an action method to handle the PDF generation button click

    public void generatePDFButton(ActionEvent event) {
        // Ensure produits list is populated
        if (produits == null) {
            // Populate produits list if not already populated
            populateProduits();
        }

        // Call generatePDF method with the produits list
        generatePDF(produits);
    }
}











