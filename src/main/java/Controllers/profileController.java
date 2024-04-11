package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import Services.Session;
import javafx.stage.Stage;

public class profileController implements Initializable {

    @FXML
    private ImageView profilePicture;
    @FXML
    private Button cancelButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label createdAtLabel;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField phoneTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File profilePictureFile = new File("images/Mprofile.png");
        Image profilePictureImage = new Image(profilePictureFile.toURI().toString());
        profilePicture.setImage(profilePictureImage);

        Session session = Session.getInstance();
        Map<String, Object> userSession = session.getUserSession();
        usernameLabel.setText(userSession.get("firstname") + " " + userSession.get("lastname"));

        lastnameTextField.setText(userSession.get("lastname").toString());
        lastnameTextField.setStyle("-fx-text-fill: #808080; -fx-border-color: #ffb524;");

        firstnameTextField.setText(userSession.get("firstname").toString());
        firstnameTextField.setStyle("-fx-text-fill: #808080; -fx-border-color: #ffb524;");

        roleLabel.setText(userSession.get("roles").toString());
        roleLabel.setStyle("-fx-text-fill: #ffffff");

        createdAtLabel.setText(userSession.get("created_at").toString());

        emailTextField.setText(userSession.get("email").toString());
        emailTextField.setStyle("-fx-text-fill: #808080; -fx-border-color: #ffb524;");

        phoneTextField.setText(userSession.get("phone").toString());
        phoneTextField.setStyle("-fx-text-fill: #808080; -fx-border-color: #ffb524;");

    }

    public void cancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
