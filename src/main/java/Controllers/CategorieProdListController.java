package Controllers;

import Entities.CategorieProd;
import Entities.Produit;
import Services.CategorieService;
import Services.Session;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CategorieProdListController {

    @FXML
    private TableView<CategorieProd> CategoriePTable;
    @FXML
    private TableColumn<CategorieProd, String> DateCatColumn;

    @FXML
    private TableColumn<CategorieProd, String> DescriptionCatPColumn;

    @FXML
    private TableColumn<CategorieProd, String> namePColumn;
    @FXML
    private TableColumn<CategorieProd, Integer> idColumn;


    @FXML
    private Button CategoryButton;

    @FXML
    private Label CreationDateButton;



    @FXML
    private DatePicker createdateField;
    @FXML
    private TextArea DescripTextfield;

    @FXML
    private TextField nomCatTextfield;

    @FXML
    private Label DescriptionButton;

    @FXML
    private Button ProduitButton;

    @FXML
    private Button addButton;


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
    private Label nomCatButton;



    @FXML
    private Button profileButton;

    @FXML
    private Button updateButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button userButton;


    @FXML
    void initialize() {
        File logoFile = new File("images/logoglowapp.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);

        Session session = Session.getInstance();
        Map<String, Object> userSession = session.getUserSession();
        usernameLabel.setText(userSession.get("email").toString());

        // Initialize the table columns
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        namePColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom_ca()));
        DescriptionCatPColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription_cat()));
        DateCatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreate_date_ca().toString()));

        // Populate the table
        populateCategorieTable();

        //refresh table
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> populateCategorieTable()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void populateCategorieTable() {
        try {
            // Retrieve categories from the database using CategorieService
            CategorieService categorieService = new CategorieService();

            List<CategorieProd> categories = categorieService.show();

            // Display categories in the table
            CategoriePTable.getItems().setAll(categories);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

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
    void deleteButtonOnAction(ActionEvent event) {
        // Get the selected category from the table
        CategorieProd selectedCategory = CategoriePTable.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            // Ask for confirmation before deleting the category
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Category");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Are you sure you want to delete this category?");
            confirmation.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        try {
                            // Call the delete method from CategorieService to delete the category
                            CategorieService categorieService = new CategorieService();
                            categorieService.delete(selectedCategory);

                            // Refresh the table view after deletion
                            refreshTable();

                            // Show a success message
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Category deleted successfully!");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            // Handle the exception appropriately
                        }
                    });
        } else {
            // If no category is selected, show an error message
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a category to delete.");
        }
    }
    private void refreshTable() {
        try {
            // Retrieve categories from the database using CategorieService
            CategorieService categorieService = new CategorieService();
            List<CategorieProd> categories = categorieService.show();

            // Display categories in the table
            CategoriePTable.getItems().setAll(categories);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }
    @FXML
    void addButtonOnAction(ActionEvent event) {

        try {
            // Retrieve data from the UI controls
            String nomCat= nomCatTextfield.getText();
            String descriptionCat = DescripTextfield.getText();
            LocalDate selectedDate = createdateField.getValue();



            if (nomCat.isEmpty() || descriptionCat.isEmpty() ) {
                showAlert(Alert.AlertType.ERROR, "Error", "All fields are required.");
                return;
            }

            // Check if date is in the past
            if (selectedDate.isBefore(LocalDate.now())) {
                // Display error alert for past date
                showAlert(Alert.AlertType.ERROR, "Error", "Date cannot be in the past.");
                return;
            }

            // Create a categorie object with the retrieved data
            CategorieProd categorieToAdd = new CategorieProd();
            categorieToAdd.setNom_ca(nomCat);
            categorieToAdd.setDescription_cat(descriptionCat);
            categorieToAdd.setCreate_date_ca(selectedDate.atTime(0, 0)); // Set time component to midnight



            // Call the add method to add the product
            CategorieService categorieService= new CategorieService();
            categorieService.add(categorieToAdd);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Category  added successfully!");
            //Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            //stage.close();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the category.");
        }

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    @FXML
    void getCategorieProd(MouseEvent event) {

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
    void updateButtonOnAction(ActionEvent event) {
        // Get the selected produit from the table view
        CategorieProd selectedCategorieProd = CategoriePTable.getSelectionModel().getSelectedItem();

        if (selectedCategorieProd != null) {
            try {
                // Load the new FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/cat_update.fxml"));
                Parent root = loader.load();

                // Get the controller of the update produit interface
                UpdateCategorieController updateCategorieController = loader.getController();

                // Initialize the update interface with the selected produit information
                updateCategorieController.initData(selectedCategorieProd);

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
}