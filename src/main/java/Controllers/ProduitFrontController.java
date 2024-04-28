package Controllers;

import Entities.Produit;
import Services.ProduitService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;




import javafx.scene.input.MouseEvent;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProduitFrontController {

    @FXML
    private Label LocationNameLabel;

    @FXML
    private Label LocationPriceLabel;


    @FXML
    private Label adresseLabel;

    @FXML
    private VBox chosenFruitCard;

    @FXML
    private Label descriptionLabel;

    @FXML
    private GridPane grid;

    @FXML
    private ImageView locationimg;

    @FXML
    private ScrollPane scroll;

    private int selectedLocationId;


    @FXML
    void handleReserveButtonClick(ActionEvent event) {

    }


    private void handleItemClick(MouseEvent event, int locationId) {
        // Store the selected location ID
        selectedLocationId = locationId;

        // Fetch location details using locationId
        ProduitService locationService = new ProduitService();
        Produit clickedLocation = null;
        try {
            clickedLocation = locationService.getProduitById(locationId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Update chosen location
        updateChosenLocation(clickedLocation);
    }

    private void updateChosenLocation(Produit location) {
        Label locationNameLabel = (Label) chosenFruitCard.lookup("#LocationNameLabel");
        Label locationPriceLabel = (Label) chosenFruitCard.lookup("#LocationPriceLabel");
        ImageView locationImageView = (ImageView) chosenFruitCard.lookup("#locationimg");
        // Label adresseLabel = (Label) chosenFruitCard.lookup("#adresseLabel"); // Add this line
        Label descriptionLabel = (Label) chosenFruitCard.lookup("#descriptionLabel"); // Add this line

        // Update labels with location information
        locationNameLabel.setText(location.getName());
        locationPriceLabel.setText(String.valueOf(location.getPrice()));
        //adresseLabel.setText(location.getLocation()); // Set address label text
        descriptionLabel.setText(location.getDescription()); // Set description label text

        // Fetch the image for the selected location

        // Load the image using the URL
        Image fxImage = new Image(location.getImage());
        locationImageView.setImage(fxImage);
    }

    @FXML
    void initialize() throws SQLException {
        // Fetch locations from the database
        ProduitService locationService = new ProduitService();
        List<Produit> produits = locationService.show();


        // Initialize column and row counters
        int column = 0;
        int row = 1; // Start displaying images from the second row

        // Populate the grid with items representing each product
        for (Produit produit : produits) {
            // Fetch the associated image path for the product
            String imagePath = produit.getImage();

            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    // Construct the full file path for the image
                    String fullImagePath = "file:///C:/Users/torkh/OneDrive/Bureau/glowimage/" + imagePath;

                    // Load the image using the full file path
                    //Image fxImage = new Image(fullImagePath);
                    // Load the image using the URL
                    Image fxImage = new Image(produit.getImage());

                    // Create a new item controller for the product
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/item.fxml"));
                    AnchorPane itemPane = fxmlLoader.load();
                    ItemProduitController itemController = fxmlLoader.getController();

                    // Set the product information in the item controller
                    itemController.setLocation(produit.getName(), produit.getPrice(), fxImage);

                    // Set size constraints for the itemPane
                    itemPane.setPrefSize(300, 300); // Adjust size as needed

                    // Add event handler for item click
                    itemPane.setOnMouseClicked(event -> handleItemClick(event, produit.getRef())); // Pass product ID to handleItemClick

                    // Add the item to the grid
                    grid.add(itemPane, column++, row);
                    grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                    grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    grid.setMaxWidth(Region.USE_COMPUTED_SIZE);

                    grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                    grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    grid.setMaxHeight(Region.USE_COMPUTED_SIZE);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Handle the case where the image path is null or empty
                System.out.println("Image path is null or empty for product: " + produit.getName());
            }

            // Check if column exceeds the maximum allowed (3)
            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }
}