package Controllers;

import Entities.CategorieProd;
import Entities.Produit;
import Services.ProduitService;
import Services.Session;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private Button userButton;


    @FXML
    private Button StatGenerateButton;
    @FXML
    private Button CategorieButton;

    @FXML
    private TableView<Produit> ProduitTable;
    private List<Produit> produits;


    @FXML
    private Label usernameLabel;


    @FXML
    private TextField searchTextField;

    private ObservableList<Produit> ProdObservableList = FXCollections.observableArrayList();

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

        Session session = Session.getInstance();
        Map<String, Object> userSession = session.getUserSession();
        usernameLabel.setText(userSession.get("email").toString());

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
        ProduitService produitService = new ProduitService(); // Instantiate the service
        try {
            // Fetch products from the database
            List<Produit> produits = produitService.getAllProduits();

            // Clear the table to avoid duplicate entries
            ProdObservableList.clear(); // Clear the observable list

            // Add products to the table
            ProdObservableList.addAll(produits); // Add new products

            // Set the table items
            ProduitTable.setItems(ProdObservableList);

            // Check low stock and display alerts
            for (Produit produit : produits) {
                if (produit.getQuantity() <= 10) {
                    showAlert(Alert.AlertType.WARNING, "Low Stock Alert", "Product '" + produit.getName() + "' is low in stock. Remaining quantity: " + produit.getQuantity());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to populate produit table.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.show();
        });
    }

    @FXML
    void StatGenerateButtonOnAction(ActionEvent event) {

        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Stat.fxml"));
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

    @FXML
    void getProduit(MouseEvent event) {

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
            Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage1.close();

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void generatePDFButton(ActionEvent event) {

        try {
            // Load the logo image
            InputStream logoStream = getClass().getResourceAsStream("/image/logoApp.png");
            if (logoStream == null) {
                System.out.println("Logo image not found!");
                return;
            }
            BufferedImage logoImage = ImageIO.read(logoStream);

            // Convert BufferedImage to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(logoImage, "png", baos);
            byte[] logoBytes = baos.toByteArray();

            // Create a new PDF document
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("Produits.pdf"));
            document.open();

            // Add the logo image to the document
            com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(logoBytes);
            logo.scaleToFit(125, 125); // Resize the logo as needed
            logo.setAlignment(Element.ALIGN_LEFT);
            document.add(logo);

            // Add spacing between logo and title
            document.add(new Paragraph("\n"));

            // Title of the document
            com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("Listes des produits");
            title.setAlignment(com.itextpdf.text.Paragraph.ALIGN_CENTER);
            title.setSpacingAfter(10f);
            document.add(title);

            // Create a table to display the data
            PdfPTable table = new PdfPTable(4); // 4 columns for the attributes of the products

            // Add column headers to the table
            PdfPCell[] headers = new PdfPCell[]{
                    new PdfPCell(new com.itextpdf.text.Paragraph("Name")),
                    new PdfPCell(new com.itextpdf.text.Paragraph("Price")),
                    new PdfPCell(new com.itextpdf.text.Paragraph("Quantity")),
                    new PdfPCell(new com.itextpdf.text.Paragraph("Category"))
            };

            // Color the column headers in green


            // Color the column headers
            BaseColor greenColor = new BaseColor(0, 128, 0); // Green color
            for (PdfPCell cell : headers) {
                cell.setBackgroundColor(greenColor);
                table.addCell(cell);
            }

            // Get the list of products from the service
            ProduitService produitService = new ProduitService();
            List<Produit> produits = produitService.getAllProduits();

            // Add product data to the table
            for (Produit produit : produits) {
                table.addCell(new PdfPCell(new com.itextpdf.text.Paragraph(produit.getName())));
                table.addCell(new PdfPCell(new com.itextpdf.text.Paragraph(String.valueOf(produit.getPrice()))));
                table.addCell(new PdfPCell(new com.itextpdf.text.Paragraph(String.valueOf(produit.getQuantity()))));
                table.addCell(new PdfPCell(new com.itextpdf.text.Paragraph(produit.getCategorie().getNom_ca())));
            }

            // Add the table of products to the document
            document.add(table);

            // Show a success alert
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("The list of products has been generated successfully!");
            successAlert.show();

            document.close(); // Close the PDF document
            // Ouvrir le document PDF avec l'application par défaut

            File file = new File("Produits.pdf");

            Desktop.getDesktop().open(file);

        } catch (com.itextpdf.text.DocumentException | IOException | SQLException e) {
            e.printStackTrace();
            // Show an error alert in case of an error
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("An error occurred while generating the products report!");
            errorAlert.show();
        }
    }

//    private void generatePDF(List<Produit> produits) {
//        try {
//            // Create a new PDF document
//            PDDocument document = new PDDocument();
//            PDPage page = new PDPage();
//            document.addPage(page);
//
//            // Initialize the content stream of the page
//            PDPageContentStream contentStream = new PDPageContentStream(document, page);
//
//            // Add table of products
//            addTableOfProducts(document, contentStream, produits);
//
//
//
//            // Close the content stream
//            contentStream.close();
//
//            // Save the PDF document
//            File file = new File("Products.pdf");
//            document.save(file);
//            document.close();
//
//            // Open the PDF document with the default application
//            Desktop.getDesktop().open(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void addTableOfProducts(PDDocument document, PDPageContentStream contentStream, List<Produit> produits) throws IOException {
//        // Define table parameters
//        float margin = 50;
//        float yStart = 700; // Adjust as needed
//        float yPosition = yStart;
//        float rowHeight = 20f;
//        float tableMargin = 10;
//
//        // Define column widths
//        float[] columnWidths = {0.2f, 0.3f, 0.3f, 0.2f}; // Adjust these as needed
//        float tableWidth = 500; // Adjust as needed
//
//        // Begin the Content stream
//        contentStream.beginText();
//        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
//        contentStream.newLineAtOffset(margin, yPosition);
//
//        // Add header row
//        addTableHeader(contentStream, margin, yPosition, tableWidth, rowHeight, columnWidths);
//        yPosition -= rowHeight;
//
//        // Add product details
//        contentStream.setFont(PDType1Font.HELVETICA, 12);
//        for (Produit produit : produits) {
//            yPosition -= rowHeight;
//            if (yPosition <= margin) {
//                // New page needed if content exceeds current page
//                contentStream.endText();
//                contentStream.close();
//                PDPage page = new PDPage();
//                document.addPage(page);
//                contentStream = new PDPageContentStream(document, page);
//                contentStream.beginText();
//                contentStream.newLineAtOffset(margin, yStart);
//                yPosition = yStart;
//                addTableHeader(contentStream, margin, yPosition, tableWidth, rowHeight, columnWidths);
//                yPosition -= rowHeight;
//            }
//            addTableRow(contentStream, margin, yPosition, tableWidth, rowHeight, columnWidths, produit);
//        }
//        // End the content stream
//        contentStream.endText();
//    }

    private void addTableHeader(PDPageContentStream contentStream, float xStart, float yStart, float tableWidth, float rowHeight, float[] columnWidths) throws IOException {
        float xPosition = xStart;
        for (float width : columnWidths) {
            contentStream.newLineAtOffset(xPosition, yStart);
            contentStream.showText("Header"); // Replace with your column headers
            xPosition += width * tableWidth;
        }
    }

    private void addTableRow(PDPageContentStream contentStream, float xStart, float yStart, float tableWidth, float rowHeight, float[] columnWidths, Produit produit) throws IOException {
        float xPosition = xStart;
        for (float width : columnWidths) {
            contentStream.newLineAtOffset(xPosition, yStart);
            // Add the product information to the table row
            // Adjust the logic based on your specific requirements
            // For example, you might have produit.getName(), produit.getDescription(), etc.
            contentStream.showText("Product Info");
            xPosition += width * tableWidth;
        }
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

//    public void generatePDFButton(ActionEvent event) {
//        // Ensure produits list is populated
//        if (produits == null) {
//            // Populate produits list if not already populated
//            populateProduits();
//        }
//
//        // Call generatePDF method with the produits list
//        generatePDF(produits);
//    }

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











