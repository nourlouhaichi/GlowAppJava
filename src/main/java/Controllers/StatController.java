package Controllers;

import Entities.Produit;
import Services.ProduitService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Button profileButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private PieChart productPieChart;

    @FXML
    private AreaChart<Number, Number> priceDistributionChart;


    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;


    @FXML
    private Button returnbutton;





    @FXML
        public void initialize() {
            try {

                //Stat 1
                // Retrieve product data from the database
                ProduitService productService = new ProduitService();
                List<Produit> productList = productService.getAllProduits();

                // Calculate product counts by category
                Map<String, Integer> categoryCounts = new HashMap<>();
                for (Produit product : productList) {
                    String categorie = product.getCategorie().getNom_ca();
                    categoryCounts.put(categorie, categoryCounts.getOrDefault(categorie, 0) + 1);
                }

                // Create pie chart data
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
                for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
                    pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
                }

                // Set pie chart data
                productPieChart.setData(pieChartData);




                //stat2
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
            }
        }


   /* @FXML
    void ProduitButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListProduit.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


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

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    }*/

    @FXML
    void returnbuttonOnClick(ActionEvent event) {


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();


    }

}
