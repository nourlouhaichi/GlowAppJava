package Controllers;

import Entities.Produit;
import Services.ProduitService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StatController {

    @FXML
    private Button CategorieButton;

    @FXML
    private Button ProduitButton;

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
    private AreaChart<Number, Number> priceDistributionChart;

    @FXML
    private Button profileButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;




    public void initialize() {
        try {
            // Retrieve product data from the database
            ProduitService productService = new ProduitService();
            List<Produit> productList = productService.getAllProduits();

            // Calculate product prices
            List<Double> productPrices = productList.stream()
                    .map(Produit::getPrice)
                    .collect(Collectors.toList());

            // Determine price range
            double minPrice = Collections.min(productPrices);
            double maxPrice = Collections.max(productPrices);
            double range = maxPrice - minPrice;

            // Determine number of bins (or intervals) for price distribution
            int numBins = 10; // You can adjust this value based on your data

            // Calculate bin width
            double binWidth = range / numBins;

            // Calculate frequency of products in each bin
            int[] binCounts = new int[numBins];
            for (Double price : productPrices) {
                int binIndex = (int) ((price - minPrice) / binWidth);
                if (binIndex >= numBins) {
                    binIndex = numBins - 1; // Adjust for the last bin
                }
                binCounts[binIndex]++;
            }

            // Create data for the area chart
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            for (int i = 0; i < numBins; i++) {
                double binStart = minPrice + i * binWidth;
                double binEnd = minPrice + (i + 1) * binWidth;
                double binMidPoint = (binStart + binEnd) / 2.0; // Calculate the midpoint of the bin
                series.getData().add(new XYChart.Data<>(binMidPoint, binCounts[i]));
            }

            // Set data to the chart
            priceDistributionChart.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to retrieve product data");
        }
    }

    // Method to show an alert dialog
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void ProduitButtonOnAction(ActionEvent event) {

    }

    @FXML
    void categorieButton(ActionEvent event) {

    }

    @FXML
    void frontButtonOnAction(ActionEvent event) {

    }

    @FXML
    void homeButtonOnAction(ActionEvent event) {

    }

    @FXML
    void logoutButtonOnAction(ActionEvent event) {

    }

    @FXML
    void profileButtonOnAction(ActionEvent event) {

    }

}
