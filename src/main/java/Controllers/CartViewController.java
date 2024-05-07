package Controllers;

import Entities.Cart;
import Entities.Produit;
import Services.ProduitService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import javafx.scene.layout.VBox;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CartViewController {
    @FXML
    private VBox cartItems;

    @FXML
    private AnchorPane paymentForm; // référence à l'élément racine du formulaire de paiement

    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField expMonthField;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Button removeButton;


    @FXML
    private TextField expYearField;

    @FXML
    private TextField cvcField;

   /* @FXML
    private ListView<String> cartListView;*/

    @FXML
    private TableView<Produit> cartTableView;

    @FXML
    private TableColumn<Produit, String> productNameColumn;

    @FXML
    private TableColumn<Produit, Double> priceColumn;



    private ObservableList<Produit> items = FXCollections.observableArrayList();


    @FXML
    private ImageView logoImageView;

    public void initialize() {

        File logoFile = new File("images/logoglowapp.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);

        // Initialize table columns
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));



        // Populate table with data
        List<Produit> cartItemsList = Cart.getCartItems();
        items.addAll(cartItemsList);
        cartTableView.setItems(items);

        // Update total price label
        updateTotalPriceLabel();
    }



    private void updateTotalPriceLabel() {
        // Calculate total amount
        double totalAmount = calculateTotalAmount();

        // Update the text of the total price label
        totalPriceLabel.setText("Prix total: " + totalAmount + " TND");
    }


    @FXML
    private void returnToProduitFront(ActionEvent event) {
        try {
            // Charger la vue CartView.fxml dans un Parent
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Produitfront.fxml"));
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
    private Button payButton;


    private double calculateTotalAmount() {
        // Obtenez la liste des produits du panier
        List<Produit> cartItemsList = Cart.getCartItems();

        // Initialiser le montant total à zéro
        double totalAmount = 0.0;

        // Parcourir les produits du panier et additionner leurs prix
        for (Produit produit : cartItemsList) {
            totalAmount += produit.getPrice();
        }

        return totalAmount;
    }


    @FXML
    void removeButtonOnClick(ActionEvent event) {
        // Get the selected product from the table view
        Produit selectedProduit = cartTableView.getSelectionModel().getSelectedItem();

        if (selectedProduit != null) {
            // Ask for confirmation before deleting the product
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Product");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Are you sure you want to delete this product?");
            confirmation.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        int index = cartTableView.getSelectionModel().getSelectedIndex();
                        items.remove(index);


                        // Refresh the table view after deletion
                        cartTableView.refresh();
                        // Show a success message
                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        success.setTitle("Success");
                        success.setHeaderText(null);
                        success.setContentText("Product deleted successfully!");
                        success.showAndWait();
                    });
        } else {
            // If no product is selected, show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a product to delete.");
            alert.showAndWait();
        }
    }
   /* private void refreshCartTableView() {
        // Get the updated list of cart items
        List<Produit> cartItemsList = Cart.getCartItems();

        // Update the table view with the updated list
        ObservableList<Produit> items = FXCollections.observableArrayList(cartItemsList);
        cartTableView.setItems(items);

        // Update the total price label
        updateTotalPriceLabel();
    }*/

    @FXML
    private void payment(ActionEvent event) {


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Payment.fxml"));
            Parent root = loader.load();

            PaymentController controller = loader.getController();
            controller.initData(calculateTotalAmount());

            Stage Stage = new Stage();
            Stage.setScene(new Scene(root,600,400));
            Stage.show();

            // Charger la vue CartView.fxml dans un Parent
            /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/Payment.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle à partir de l'événement
            Scene currentScene = ((Node) event.getSource()).getScene();

            // Remplacer le contenu de la racine de la scène actuelle par le contenu du panier
            currentScene.setRoot(root);
*/
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*public void processPayment(ActionEvent event) {
        // Initialize Stripe with your API key
        Stripe.apiKey = "sk_test_51OrmdaL5srMxpXNr8VVGkg3c3sKmpyUtCjal5z1m37VzmVj8OHnP5pYvUThi8OWWeKnIuvu4pcvWmdmUN8Kl3Hu200zabG10FE";

        // Get card details from user input
        String cardNumber = cardNumberField.getText();
        Integer expMonth = Integer.parseInt(expMonthField.getText());
        Integer expYear = Integer.parseInt(expYearField.getText());
        String cvc = cvcField.getText();

        // Calculate total amount
        double totalAmount = calculateTotalAmount(); // Assuming this method calculates the total amount

        // Create a Map to represent the charge parameters
        Map<String, Object> params = new HashMap<>();
        params.put("amount", (int) (totalAmount)); // Amount in cents
        params.put("currency", "usd");
        params.put("source", "tok_visa"); // Use a test token (replace with actual token in production)
        params.put("description", "Example charge");
        params.put("metadata", Map.of("total_amount", totalAmount)); // Store total amount in metadata

        // Attempt to charge the card
        try {
            Charge charge = Charge.create(params);
            // Payment successful, show a success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Payment Successful");
            alert.setHeaderText(null);
            alert.setContentText("Payment successful! Charge ID: " + charge.getId());
            alert.showAndWait();
            // Redirection vers Produitdront.fxml après le paiement réussi
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Produitfront.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (StripeException | IOException e) {
            // Afficher un message d'erreur en cas d'échec de paiement ou de redirection
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred. " + e.getMessage());
            alert.showAndWait();
        }
    }*/
}
