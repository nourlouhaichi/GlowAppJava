package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class backhomeController {

    @FXML
    private Button PublicationButton;

    @FXML
    private Button frontButton;

    @FXML
    private Button homeButton;

    @FXML
    private BarChart<?, ?> home_chart;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Label home_totalEmployees;

    @FXML
    private Label home_totalInactiveEm;

    @FXML
    private Label home_totalPresents;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Button logoutButton;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button profileButton;

    @FXML
    private Button userButton;

    @FXML
    private Label usernameLabel;

    @FXML
    void PublicationButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tableview.fxml"));
            Parent newPage = loader.load();
            tableviewController controller = loader.getController();
            AnchorPane.setTopAnchor(newPage, 10.0);
            AnchorPane.setLeftAnchor(newPage, 40.0);
            AnchorPane.setRightAnchor(newPage, 10.0);
            AnchorPane.setBottomAnchor(newPage, 10.0);
            home_form.getChildren().setAll(newPage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }




