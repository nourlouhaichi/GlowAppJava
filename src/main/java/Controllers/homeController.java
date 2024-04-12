package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import Services.Session;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Map;

public class homeController implements Initializable {
    @FXML
    private Label userLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private ImageView logoImageView;
    @FXML
    private ImageView homeImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*Session session = Session.getInstance();
        Map<String, Object> userSession = session.getUserSession();
        StringBuilder userDetailsText = new StringBuilder();
        for (Map.Entry<String, Object> entry : userSession.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            userDetailsText.append(key).append(": ").append(value).append(" ");
        }

        userLabel.setText(userDetailsText.toString());*/

        File homeFile = new File("images/home.png");
        Image homeImage = new Image(homeFile.toURI().toString());
        homeImageView.setImage(homeImage);

        File logoFile = new File("images/logoglowapp.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);
    }

    public void logoutButtonOnAction(ActionEvent event) {
        Session.getInstance().logout();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
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
}
