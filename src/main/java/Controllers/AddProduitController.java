package Controllers;

import java.net.URL;

import java.util.ResourceBundle;
import Entities.CategorieProd;
import Entities.Produit;
import Services.CategorieService;
import Services.ProduitService;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.SpinnerValueFactory ;
import javafx.scene.control.Spinner;
import java.sql.SQLException;
import java.util.List;

public class AddProduitController {

    @FXML
    private Button addProduitButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TextArea descriptiontextfield;

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
    private TextField namePTextField;

    @FXML
    private TextField photoPtextfield;

    @FXML
    private TextField pricePTextField;

    @FXML
    private TextField quantitytextfield;

    @FXML
    private Button produitbutton;


    @FXML
    private Label usernameLabel;

    private final CategorieService catus = new CategorieService();

    @FXML
    void addProduitButtonOnAction(ActionEvent event) {
        try {
            // Retrieve data from the UI controls
            String name = namePTextField.getText();
            String description = descriptiontextfield.getText();
            String photoPath = photoPtextfield.getText();
            double price = Double.parseDouble(pricePTextField.getText());
            int quantity;

            if (name.isEmpty() || description.isEmpty() || photoPath.isEmpty() || price <= 0 || categoryComboBox.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
                return;
            }
            try {
                quantity = Integer.parseInt(quantitytextfield.getText());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid quantity.");
                return;
            }

            // Get selected category from ComboBox
            String selectedCategoryNom = categoryComboBox.getValue();
            // Get the CategorieProd object from the database using the category name
            CategorieProd selectedCategory = catus.getCategoryByTitle(selectedCategoryNom);

            if (selectedCategory == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Selected category not found.");
                return;
            }

            // Create a Produit object with the retrieved data
            Produit produitToAdd = new Produit();
            produitToAdd.setName(name);
            produitToAdd.setDescription(description);
            produitToAdd.setImage(photoPath);
            produitToAdd.setPrice(price);
            produitToAdd.setQuantity(quantity);
            produitToAdd.setCategorie(selectedCategory); // Set the category

            // Call the add method to add the product
            ProduitService produitService = new ProduitService();
            produitService.add(produitToAdd);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Product added successfully!");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the product.");
        }
    }

    @FXML
    void initialize() {
        populateCategoryComboBox();
    }
    // Helper method to show alert
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
    void cancelButtonOnAction(ActionEvent event) {

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

        }









    @FXML
    void homeButtonOnAction(ActionEvent event) {

    }

    @FXML
    void logoutButtonOnAction(ActionEvent event) {

    }

}


