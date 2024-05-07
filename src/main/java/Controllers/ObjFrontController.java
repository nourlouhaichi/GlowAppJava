package Controllers;

import Entities.Objectif;
import Entities.Programme;
import Services.ServiceObjectif;
import Services.ServiceProgramme;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class ObjFrontController {
    @FXML
    private Label currentCalorieIntakeLabel;
    @FXML
    private Label calorieIntakeGoalLabel;
    @FXML
    private TilePane cardContainer;
    @FXML
    private Label currentWaterIntakeLabel; // Reference to the label displaying current water intake
    private int waterIntakeGoal = 8;
    private int currentCalorieIntake;
    private int calorieIntakeGoal;


    private int currentWaterIntake = 0; // Variable to track current water intakea
    private LocalDateTime lastResetTime;

    private ServiceObjectif serviceObjectif = new ServiceObjectif();

    public ObjFrontController() {
        currentCalorieIntake = 0;
        calorieIntakeGoal = 2500;
        lastResetTime = LocalDateTime.now();
    }

    @FXML
    public void initialize() {
        loadObjCards();
        updateCalorieIntakeUI();

    }
    @FXML
    private void eatFood(javafx.event.ActionEvent  event) {
        // Increment calorie intake
        currentCalorieIntake += getCaloriesFromFood(); // You'll need to implement this method

        // Update UI
        updateCalorieIntakeUI();

        if (currentCalorieIntake >= calorieIntakeGoal) {
            showAlert("Congratulations! You've reached your calorie intake goal for today!");
        }
    }
    private void updateCalorieIntakeUI() {
        // Update labels to display current calorie intake and goal
        currentCalorieIntakeLabel.setText("Current Calorie Intake: " + currentCalorieIntake);
        calorieIntakeGoalLabel.setText("Calorie Intake Goal: " + calorieIntakeGoal);
    }
    @FXML
    private void checkAndResetCalorieIntake(javafx.event.ActionEvent  event) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(lastResetTime, now);
        if (duration.toHours() >= 24) {
            // Reset calorie intake count and update last reset time
            currentCalorieIntake = 0;
            lastResetTime = now;
            updateCalorieIntakeUI();
        }
    }


    private int getCaloriesFromFood() {
        // Simulate different food items with varying calorie contents
        String[] foodItems = {"Apple", "Banana", "Orange", "Pizza", "Burger", "Salad"};
        Random random = new Random();
        int randomIndex = random.nextInt(foodItems.length);
        int calories;

        switch (foodItems[randomIndex]) {
            case "Apple":
                calories = random.nextInt(50) + 50; // Random value between 50 and 100 calories
                break;
            case "Banana":
                calories = random.nextInt(100) + 80; // Random value between 80 and 180 calories
                break;
            case "Orange":
                calories = random.nextInt(60) + 40; // Random value between 40 and 100 calories
                break;
            case "Pizza":
                calories = random.nextInt(400) + 200; // Random value between 200 and 600 calories
                break;
            case "Burger":
                calories = random.nextInt(500) + 300; // Random value between 300 and 800 calories
                break;
            case "Salad":
                calories = random.nextInt(200) + 50; // Random value between 50 and 250 calories
                break;
            default:
                calories = random.nextInt(150) + 50; // Random value between 50 and 200 calories for unknown items
                break;
        }

        return calories;
    }


    private void loadObjCards() {
        try {
            for (Objectif obj : serviceObjectif.afficher()) {
                VBox card = new VBox(10);
                card.setPadding(new Insets(10));
                card.setStyle("-fx-border-color: black; -fx-border-width: 2;");

                Label objectifLabel = new Label("Objectif : " + obj.getObjectifO());
                Label descriptionLabel = new Label("Description: " + obj.getDescriptionO());
                Button detailsButton = new Button("View Details");
                detailsButton.setOnAction(e -> showObjDetails(obj));

                card.getChildren().addAll(objectifLabel, descriptionLabel, detailsButton);
                cardContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showObjDetails(Objectif objectif) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ObjDetail.fxml"));
            Parent root = loader.load();

            ObjDetailController controller = loader.getController();
            controller.setObj(objectif);

            Stage stage = new Stage();
            stage.setTitle("Objectif Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void backHome(javafx.event.ActionEvent event) {
        try {

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void ObjsFront(javafx.event.ActionEvent event) {
        try {

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ObjFront.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void ProgsFront(javafx.event.ActionEvent event) {
        try {

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProgFront.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Congratulations!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void drinkWater(javafx.event.ActionEvent event) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(lastResetTime, now);
        if (duration.toHours() >= 24) {
            // Reset the water intake count and update the last reset time
            currentWaterIntake = 0;
            lastResetTime = now;
        }
        // Increment water intake
        currentWaterIntake++;

        // Update the label to display the new water intake
        currentWaterIntakeLabel.setText("Current Water Intake: " + currentWaterIntake + " glasses");

        if (currentWaterIntake >= waterIntakeGoal) {
            showAlert("Congratulations! You've reached your water intake goal for today!");
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

    public void eventButtonOnAction(ActionEvent event) {
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

    public void publicationButtonOnAction(ActionEvent event) {
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

    public void programButtonOnAction(ActionEvent event) {
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

    public void objectiveButtonOnAction(ActionEvent event) {
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
    }

    // Define additional methods and logic as needed











