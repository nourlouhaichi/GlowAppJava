package Controllers;

import Entities.CategorieProd;
import Entities.Produit;
import Services.CategorieService;
import Services.EventServices;
import Services.ProduitService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
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
    private String selectedImagePath;

    @FXML
    private Button imageSelectButton;
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
        //photoPtextfield.setText(selectedProduit.getImage());
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
        if (namePTextField.getText().isEmpty() || descriptiontextfield.getText().isEmpty() ||
                pricePTextField.getText().isEmpty() || quantitytextfield.getText().isEmpty() || categoryComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required!");
            return;
        }

        // Validate price
        double price;
        try {
            price = Double.parseDouble(pricePTextField.getText());
            if (price <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Price must be a positive number!");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid price format!");
            return;
        }

        // Validate quantity
        int quantity;
        try {
            quantity = Integer.parseInt(quantitytextfield.getText());
            if (quantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Quantity must be a positive integer!");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid quantity format!");
            return;
        }

        // Update the product information
        selectedProduit.setName(namePTextField.getText());
        selectedProduit.setDescription(descriptiontextfield.getText());
        selectedProduit.setPrice(price);
        selectedProduit.setQuantity(quantity);

        // Handle the image path if a new one was selected
        String imagePath = getSelectedImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            selectedProduit.setImage(imagePath);
        }

        // Update the category information
        CategorieService categorieService = new CategorieService();
        try {
            CategorieProd selectedCategory = categorieService.getCategoryByTitle(categoryComboBox.getValue());
            selectedProduit.setCategorie(selectedCategory);

            // Call the update method to update the product
            ProduitService produitService = new ProduitService();
            produitService.update(selectedProduit);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully!");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update product: " + e.getMessage());
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

    private String getPhotoPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(main_form.getScene().getWindow());
        if (selectedFile != null) {
            return selectedFile.toURI().toString(); // Return URI for compatibility
        } else {
            return null;
        }
    }

    @FXML
    void updatePhoto(ActionEvent event) {
        String photoPath = getPhotoPath();
        if (photoPath != null) { // Check if a new photo was chosen
            selectedProduit.setImage(photoPath);
            ProduitService produitService = new ProduitService();
            try {
                produitService.update(selectedProduit); // Save changes
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update product photo!");
            }
        }
    }
    public String getSelectedImagePath() {
        return selectedImagePath;
    }

}