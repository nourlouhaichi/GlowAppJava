package Controllers;

import Entities.User;
import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class profiledetailsController implements Initializable {

    @FXML
    private ImageView profilePicture;
    @FXML
    private Button cancelButton;
    @FXML
    private Button banButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label createdAtLabel;
    @FXML
    private TextField cinTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField banTextField;
    @FXML
    private TextField dobTextField;
    @FXML
    private TextField genderTextField;
    @FXML
    private TextField verifyTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File profilePictureFile = new File("images/Mprofile.png");
        Image profilePictureImage = new Image(profilePictureFile.toURI().toString());
        profilePicture.setImage(profilePictureImage);
    }

    public void cancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void banButtonAction(ActionEvent event) {
        UserService us = new UserService();
        try {
            if (Objects.equals(banButton.getText(), "Ban")) {
                us.banUser(cinTextField.getText());
                banTextField.setText("true");
                banButton.setText("Unban");
                banButton.setStyle("-fx-background-color:   #81c408;");
            }
            else {
                us.unbanUser(cinTextField.getText());
                banTextField.setText("false");
                banButton.setText("Ban");
                banButton.setStyle("-fx-background-color:  #ff3547;");
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void initData(User user) {
        usernameLabel.setText(user.getFirstname() + " " + user.getLastname());
        roleLabel.setText(user.getRoles());
        createdAtLabel.setText(user.getCreated_at().toString());

        cinTextField.setText(user.getCin());
        cinTextField.setDisable(true);

        lastnameTextField.setText(user.getLastname());
        lastnameTextField.setDisable(true);

        firstnameTextField.setText(user.getFirstname());
        firstnameTextField.setDisable(true);

        emailTextField.setText(user.getEmail());
        emailTextField.setDisable(true);

        phoneTextField.setText(user.getPhone());
        phoneTextField.setDisable(true);

        genderTextField.setText(user.getGender());
        genderTextField.setDisable(true);

        dobTextField.setText(user.getDatebirth().toString());
        dobTextField.setDisable(true);

        banTextField.setText(user.getIs_banned().toString());
        banTextField.setDisable(true);

        if (banTextField.getText().equals("true")) {
            banButton.setText("Unban");
            banButton.setStyle("-fx-background-color:   #81c408;");
        }
        else {
            banButton.setText("Ban");
            banButton.setStyle("-fx-background-color:  #ff3547;");
        }

        verifyTextField.setText(user.getIs_verified().toString());
        verifyTextField.setDisable(true);
    }

}
