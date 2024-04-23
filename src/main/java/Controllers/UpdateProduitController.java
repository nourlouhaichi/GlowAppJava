package Controllers;

import Entities.CategorieProd;
import Entities.Produit;
import Services.CategorieService;
import Services.ProduitService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class UpdateProduitController {

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TextArea descriptiontextfield;

    @FXML
    private AnchorPane home_form;

    @FXML
    private ImageView logoImageView;

    @FXML
    private AnchorPane main_form;

    @FXML
    private TextField namePTextField;

    @FXML
    private TextField photoPtextfield;

    @FXML
    private TextField pricePTextField;

    @FXML
    private TextField quantitytextfield;

    @FXML
    private Button updateProduitButton;

    private Produit selectedProduit;
    private final CategorieService catus = new CategorieService();

    @FXML
    void initialize() {
        populateCategoryComboBox();

    }

    public void initData(Produit produit) {
        selectedProduit = produit;
        // Set the fields with the information of the selected product
        namePTextField.setText(selectedProduit.getName());
        descriptiontextfield.setText(selectedProduit.getDescription());
        photoPtextfield.setText(selectedProduit.getImage());
        pricePTextField.setText(String.valueOf(selectedProduit.getPrice()));
        quantitytextfield.setText(String.valueOf(selectedProduit.getQuantity()));
        // Set the selected category in the combo box (if applicable)
        categoryComboBox.setValue(selectedProduit.getCategorie().getNom_ca());
    }

    private void populateCategoryComboBox() {
        try {
            List<CategorieProd> categories = catus.show();
            ObservableList<String> categoryNames = FXCollections.observableArrayList();

            // Extract category names and add them to the categoryNames list
            for (CategorieProd category : categories) {
                String categoryName = category.getNom_ca();
                categoryNames.add(categoryName);
            }

            categoryComboBox.setItems(categoryNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Afficher une boîte de dialogue avec un message



    @FXML
    void UpdateProduitButtonOnAction(ActionEvent event) {
        // Check if any of the fields are empty
        if (namePTextField.getText().isEmpty() || descriptiontextfield.getText().isEmpty() || photoPtextfield.getText().isEmpty()
                || pricePTextField.getText().isEmpty() || quantitytextfield.getText().isEmpty() || categoryComboBox.getValue() == null) {
            // Display an alert informing the user that all fields are required
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required!");
            return; // Exit the method without updating the product
        }

        // Check if the price is a valid number
        try {
            double price = Double.parseDouble(pricePTextField.getText());
            if (price <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            // Display an alert informing the user that the price must be a positive number
            showAlert(Alert.AlertType.ERROR, "Error", "Price must be a positive number!");
            return; // Exit the method without updating the product
        }

        // Check if the quantity is a valid integer
        try {
            int quantity = Integer.parseInt(quantitytextfield.getText());
            if (quantity <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            // Display an alert informing the user that the quantity must be a positive integer
            showAlert(Alert.AlertType.ERROR, "Error", "Quantity must be a positive integer!");
            return; // Exit the method without updating the product
        }

        // Update the product information
        selectedProduit.setName(namePTextField.getText());
        selectedProduit.setDescription(descriptiontextfield.getText());
        selectedProduit.setImage(photoPtextfield.getText());
        selectedProduit.setPrice(Double.parseDouble(pricePTextField.getText()));
        selectedProduit.setQuantity(Integer.parseInt(quantitytextfield.getText()));

        // Update the category information (if applicable)
        CategorieService categorieService = new CategorieService();
        try {
            CategorieProd selectedCategory = categorieService.getCategoryByTitle(categoryComboBox.getValue());
            selectedProduit.setCategorie(selectedCategory);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }

        try {
            // Call the update method to update the product
            ProduitService produitService = new ProduitService();
            produitService.update(selectedProduit);

            // Show a success message to the user
            showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully!");
            // Close the window
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

}
