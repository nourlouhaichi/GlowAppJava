package Controllers;

import Entities.Cart;
import Entities.Produit;
import Services.ProduitService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;




import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProduitFrontController {

    @FXML
    private Label LocationNameLabel;

    @FXML
    private Label LocationPriceLabel;

    @FXML
    private Button buttonCart;
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
    private Button homeButton;



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

    @FXML
    void homeButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/homeGUI.fxml"));
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
    void eventButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventsClient.fxml"));
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
    void productButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Produitfront.fxml"));
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
    void handleAddToCartButtonClick(ActionEvent event) {
        // Récupérer les détails du produit sélectionné
        String name = LocationNameLabel.getText();
        double price = Double.parseDouble(LocationPriceLabel.getText());



        // Créer une instance de Produit avec les détails récupérés
        Produit selectedProduct = new Produit(name, price);

        // Ajouter le produit au panier
        Cart.addToCart(selectedProduct);

        // Afficher un message de confirmation
        // System.out.println("Product added to cart!");

        // Afficher un message de confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product Added");
        alert.setHeaderText(null);
        alert.setContentText("Product has been added to the cart!");
        alert.showAndWait();
    }


    @FXML
    private void openCartView(ActionEvent event) {
        try {
            // Charger la vue CartView.fxml dans un Parent
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CartView.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Scene currentScene = ((Node) event.getSource()).getScene();

            // Remplacer le contenu de la racine de la scène actuelle par le contenu du panier
            currentScene.setRoot(root);



        } catch (IOException e) {
            e.printStackTrace();
        }


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
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/itemProduct.fxml"));
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

    @FXML
    void publicationButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/ListPublications.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProgFront.fxml"));
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
    void objectiveButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ObjFront.fxml"));
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