package Controllers;

import Entities.User;
import Services.GMailer;
import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class authCodeController implements Initializable {
    @FXML
    private TextField codeTextField;
    @FXML
    private Button submitButton;
    @FXML
    private Label errorLabel;
    @FXML
    private Label cinLabel;
    @FXML
    private Button exitButton;

    String code = generateRandomCode();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void submitButtonOnAction(ActionEvent event) {
        if (!codeTextField.getText().isBlank() && codeTextField.getText().matches("\\d{6}")) {
            if ((Objects.equals(codeTextField.getText(), code))) {
                UserService us = new UserService();
                try {
                    us.verifyUser(cinLabel.getText(),code);

                    try {
                        GMailer mail = new GMailer();
                        mail.sendHtmlMail("Welcome To GlowApp", """
                            <!DOCTYPE html>
                                   <html lang="en">
                                   <head>
                                       <meta charset="UTF-8">
                                       <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                       <title>Welcome to GlowApp!</title>
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
                                               color: #81C408;
                                           }
                                           p {
                                               color: #FFB524;
                                           }
                                       </style>
                                   </head>
                                   <body>
                                       <div class="container">
                                           <img src="https://i.ibb.co/FqtPjbH/logoglowapp.png" style="max-width: 300px; height: auto;" alt="GlowApp Logo">
                                           <h1>Welcome to our website GlowApp!</h1>
                                           <p>You are successfully registered.</p>
                                           <p class="black-text">Explore our features and start enjoying the benefits!</p>
                                           <img src="https://i.ibb.co/dL4QbrY/arrangement-vue-dessus-halteres-espace-copie.jpg" style="max-width: 500px; height: auto;" alt="GlowApp Logo">
                                       </div>
                                   </body>
                                   </html>
                                    """,us.getUser(cinLabel.getText()).getEmail());

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Stage stage = (Stage) submitButton.getScene().getWindow();
                stage.close();
            }
            else {
                errorLabel.setText("Incorrect code!");
            }
        } else {
            errorLabel.setText("Enter code with 6 digits!");
        }
    }

    public void exitButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public static String generateRandomCode() {
        Random random = new Random();
        int randomInt = random.nextInt(900000) + 100000;
        return String.valueOf(randomInt);
    }

    public void getCin(String cin) {
        cinLabel.setText(cin);

        try {
            UserService us = new UserService();
            User user = us.getUser(cinLabel.getText());
            String username = user.getLastname() + " " + user.getFirstname();
            GMailer mail = new GMailer();
            mail.sendHtmlMail("Verification Code", """
                <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Verification Code</title>
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
                                    color: #FFB524;
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
                                <h1>Dear 
                                """ + username + """ 
                                </h1>
                                <p>Please find below your verification code:</p>
                                <p>
                                 """ + code + """
                                </p>
                                <p>Thank you!</p>
                            </div>
                        </body>
                        </html>
                """,user.getEmail());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
