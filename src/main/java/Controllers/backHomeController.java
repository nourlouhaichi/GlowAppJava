package Controllers;

import Services.Session;
import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.Node;
import java.io.IOException;

public class backHomeController implements Initializable {
    @FXML
    private Label usernameLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Button frontButton;
    @FXML
    private Button userButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button eventButton;
    @FXML
    private ImageView logoImageView;
    @FXML
    private PieChart genderPie;
    @FXML
    private PieChart banPie;
    @FXML
    private PieChart rolePie;
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("images/logoglowapp.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);

        Session session = Session.getInstance();
        Map<String, Object> userSession = session.getUserSession();
        usernameLabel.setText(userSession.get("email").toString());

        UserService us = new UserService();
        try {

            //Stat1
            int female = us.countGender("female");
            int male = us.countGender("male");
            int total = female + male;

            double femalePercentage = (double) female / total * 100;
            double malePercentage = (double) male / total * 100;

            ObservableList<PieChart.Data> pieChartData1 =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Females (" + String.format("%.2f", femalePercentage) + "%)", female),
                            new PieChart.Data("Males (" + String.format("%.2f", malePercentage) + "%)", male));


            genderPie.setMaxSize(300, 300);
            genderPie.getData().addAll(pieChartData1);

            //Stat 2
            int ban = us.countBans(true);
            int unban = us.countBans(false);
            int total2 = ban + unban;

            double banPercentage = (double) ban / total2 * 100;
            double unbanPercentage = (double) unban / total2 * 100;

            ObservableList<PieChart.Data> pieChartData2 =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Banned (" + String.format("%.2f", banPercentage) + "%)", ban),
                            new PieChart.Data("Unbanned (" + String.format("%.2f", unbanPercentage) + "%)", unban));


            banPie.setMaxSize(300, 300);
            banPie.getData().addAll(pieChartData2);

            //Stat 3
            int admin = us.countRole("[\"ROLE_ADMIN\"]");
            int user = us.countRole("[\"ROLE_USER\"]");
            int coach = us.countRole("[\"ROLE_COACH\"]");
            int nut = us.countRole("[\"ROLE_NUTRITIONIST\"]");
            int total3 = admin + user + coach + nut;

            double adminPercentage = (double) admin / total3 * 100;
            double userPercentage = (double) user / total3 * 100;
            double coachPercentage = (double) coach / total3 * 100;
            double nutPercentage = (double) nut / total3 * 100;

            ObservableList<PieChart.Data> pieChartData3 =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Admin (" + String.format("%.2f", adminPercentage) + "%)", admin),
                            new PieChart.Data("Member (" + String.format("%.2f", userPercentage) + "%)", user),
                            new PieChart.Data("Coach (" + String.format("%.2f", coachPercentage) + "%)", coach),
                            new PieChart.Data("nutritionist (" + String.format("%.2f", nutPercentage) + "%)", nut));

            rolePie.setMaxSize(300, 300);
            rolePie.getData().addAll(pieChartData3);

            //Stat4
            Map<String, Integer> userEvolutionDataMap = us.getUserEvolutionData();

            xAxis.setLabel("Date");
            yAxis.setLabel("User Count");
            lineChart.setTitle("Users Evolution");

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (Map.Entry<String, Integer> entry : userEvolutionDataMap.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            lineChart.getData().add(series);

        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    @FXML
    void publicationButtonOnAction(ActionEvent event) {

        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/tableview.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Programme.fxml"));
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
