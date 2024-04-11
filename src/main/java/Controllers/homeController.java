package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import Services.Session;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Map;

public class homeController implements Initializable {
    @FXML
    private Label userLabel;
    @FXML
    private Button logoutButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Session session = Session.getInstance();
        Map<String, Object> userSession = session.getUserSession();
        StringBuilder userDetailsText = new StringBuilder();
        for (Map.Entry<String, Object> entry : userSession.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            userDetailsText.append(key).append(": ").append(value).append("\n");
        }

        userLabel.setText(userDetailsText.toString());
    }

    public void logoutButtonOnAction(ActionEvent event) {
        Session.getInstance().logout();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
    }
}
