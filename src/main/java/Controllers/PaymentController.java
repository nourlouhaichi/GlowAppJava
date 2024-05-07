package Controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PaymentController {


    @FXML
    private TextField cardNumberField;


    @FXML
    private TextField cvcField;

    @FXML
    private TextField expMonthField;

    @FXML
    private TextField expYearField;
    @FXML
    private Label Label;
    @FXML
    private Button paybutton;
    @FXML
    private Label totalPriceLabel;

    public void initData(Double total){


        Label.setText(total.toString());

    }
    public void processPayment(ActionEvent event) {
        // Check if any of the text fields are empty
        if (cardNumberField.getText().isEmpty() || expMonthField.getText().isEmpty() ||
                expYearField.getText().isEmpty() || cvcField.getText().isEmpty()) {
            // Display an alert message indicating that all fields are required
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("All fields are required. Please enter valid card details.");
            alert.showAndWait();
            return; // Stop processing payment if any field is empty
        }

        // Continue with payment processing if all fields are filled
        // Initialize Stripe with your API key
        Stripe.apiKey = "sk_test_51OrmdaL5srMxpXNr8VVGkg3c3sKmpyUtCjal5z1m37VzmVj8OHnP5pYvUThi8OWWeKnIuvu4pcvWmdmUN8Kl3Hu200zabG10FE";

        // Get card details from user input
        String cardNumber = cardNumberField.getText();
        Integer expMonth = Integer.parseInt(expMonthField.getText());
        Integer expYear = Integer.parseInt(expYearField.getText());
        String cvc = cvcField.getText();

        // Calculate total amount
        double totalAmount = Double.parseDouble(Label.getText());
        // Assuming this method calculates the total amount

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
        /* loader = new FXMLLoader(getClass().getResource("/Produitfront.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();*/
        } catch (StripeException e) {
            // Afficher un message d'erreur en cas d'échec de paiement ou de redirection
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred. " + e.getMessage());
            alert.showAndWait();

            Stage stage = (Stage) paybutton.getScene().getWindow();
            stage.close();
        }
    }
}

