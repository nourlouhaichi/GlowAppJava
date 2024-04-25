package Controllers;

import Entities.User;
import Services.GMailer;
import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
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

                try {
                    GMailer mail = new GMailer();
                    mail.sendHtmlMail("Banned from GlowApp", """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Banned from GlowApp</title>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                background-color: #f4f4f4;
                                margin: 0;
                                padding: 0;
                                display: flex;
                                justify-content: center;
                                align-items: center;
                                height: 100vh;
                                text-align: center;
                            }
                            .container {
                                background-color: #fff;
                                padding: 20px;
                                border-radius: 8px;
                                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                            }
                            h1 {
                                color: #FF0000;
                                margin-bottom: 20px;
                            }
                            p {
                                color: #333;
                                font-size: 16px;
                                line-height: 1.6;
                                margin-bottom: 20px;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <img src="https://i.ibb.co/FqtPjbH/logoglowapp.png" style="max-width: 300px; height: auto;" alt="GlowApp Logo">
                            <h1>You have been banned from GlowApp</h1>
                            <p> """ + lastnameTextField.getText() + " " + firstnameTextField.getText() + """
                            , we regret to inform you that your account has been banned due to inappropriate behavior.</p>
                            <p>If you believe this ban is a mistake, please contact our support team at <a href="mailto:support@glowapp.com">support@glowapp.com</a>.</p>
                        </div>
                    </body>
                    </html>
                    """);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
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

        profilePicture.setFitWidth(200);
        profilePicture.setFitHeight(200);

        File pictureFile = new File("images/" + user.getProfile_picture());
        Image profilePictureImage = new Image(pictureFile.toURI().toString());
        profilePicture.setImage(profilePictureImage);

    }

}
