package Controllers;

import Services.GMailer;
import Services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;

import java.sql.*;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;
import java.net.URL;
import java.io.File;
import Utils.MyDatabase;
import de.svws_nrw.ext.jbcrypt.BCrypt;
import Services.Session;


public class loginController implements Initializable {
    @FXML
    private Button exitButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView logoImageView;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField enterPasswordTextField;
    private Session session;
    String code = generateRandomCode();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("images/loginpic.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File logoFile = new File("images/logoglowapp.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);

        session = Session.getInstance();
    }

    public void loginButtonOnAction(ActionEvent event) {
        if (!emailTextField.getText().isBlank() && !enterPasswordTextField.getText().isBlank()) {
            validateLogin();
        } else {
            loginMessageLabel.setText("Enter email and password!");
        }
    }

    public void exitButtonAction(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void resetButtonAction(ActionEvent event) {
        if (!emailTextField.getText().isBlank()) {
            try {
                MyDatabase bd = MyDatabase.getInstance();
                Connection connectDB = bd.getConnection();

                String query = "SELECT * FROM User WHERE email = ?";
                PreparedStatement statement = connectDB.prepareStatement(query);
                statement.setString(1, emailTextField.getText());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String username = resultSet.getString("lastname") + " " + resultSet.getString("firstname");
                    UserService us = new UserService();
                    us.resetPassword(emailTextField.getText(),BCrypt.hashpw(code, BCrypt.gensalt(13)));
                    GMailer mail = new GMailer();
                    mail.sendHtmlMail("New Password", """
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
                                <p>Please find below your new password:</p>
                                <p>
                                 """ + code + """
                                </p>
                                <p>Thank you!</p>
                            </div>
                        </body>
                        </html>
                """);

                } else {
                    loginMessageLabel.setText("User not found!");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else {
            loginMessageLabel.setText("Enter email!");
        }
    }

    public static String generateRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 9;
        Random random = new Random();
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            code.append(chars.charAt(index));
        }
        return code.toString();
    }

    public void registerButtonOnAction(ActionEvent event) {
        createAccountForm();
    }

    public void validateLogin() {
        try {
            MyDatabase bd = MyDatabase.getInstance();
            Connection connectDB = bd.getConnection();

            String query = "SELECT * FROM User WHERE email = ?";
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, emailTextField.getText());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                if (BCrypt.checkpw(enterPasswordTextField.getText(), hashedPassword)) {

                    session.getUserSession().put("cin", resultSet.getString("cin"));
                    session.getUserSession().put("email",resultSet.getString("email"));
                    session.getUserSession().put("roles", resultSet.getString("roles"));
                    session.getUserSession().put("password", resultSet.getString("password"));
                    session.getUserSession().put("lastname", resultSet.getString("lastname"));
                    session.getUserSession().put("firstname", resultSet.getString("firstname"));
                    session.getUserSession().put("gender", resultSet.getString("gender"));
                    session.getUserSession().put("datebirth",  resultSet.getDate("datebirth"));
                    session.getUserSession().put("phone", resultSet.getString("phone"));
                    session.getUserSession().put("created_at", resultSet.getTimestamp("created_at"));
                    session.getUserSession().put("is_banned", resultSet.getBoolean("is_banned"));
                    session.getUserSession().put("profile_picture", resultSet.getString("profile_picture"));
                    session.getUserSession().put("is_verified", resultSet.getBoolean("is_verified"));
                    session.getUserSession().put("auth_code", resultSet.getString("auth_code"));

                    if (!Objects.equals(resultSet.getString("roles"),"[\"ROLE_ADMIN\"]")) {
                        if (Objects.equals(resultSet.getString("is_banned"), "1")) {
                            loginMessageLabel.setText("You are banned!");
                        } else if (Objects.equals(resultSet.getString("is_verified"), "0")){
                            loadVerificationScene(resultSet.getString("cin"));
                        } else {
                            loadHomeScene();
                        }
                    } else {
                        loadAdminHomeScene();
                    }


                } else {
                    loginMessageLabel.setText("Invalid Password!");
                }
            } else {
                loginMessageLabel.setText("User not found!");
            }

        } catch (SQLException e) {
            // Handle database errors
            e.printStackTrace();
        }
    }

    public void createAccountForm() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/registerGUI.fxml"));
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(new Scene(root,550,600));
            registerStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadHomeScene() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/homeGUI.fxml"));
            Stage homeStage = new Stage();
            homeStage.initStyle(StageStyle.UNDECORATED);
            homeStage.setScene(new Scene(root,1024,576));
            homeStage.show();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAdminHomeScene() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/backHomeGUI.fxml"));
            Stage backStage = new Stage();
            backStage.initStyle(StageStyle.UNDECORATED);
            backStage.setScene(new Scene(root,1100,600));
            backStage.show();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadVerificationScene(String cin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/authCodeGUI.fxml"));
            Parent root = loader.load();

            authCodeController controller = loader.getController();
            controller.getCin(cin);

            Stage codeStage = new Stage();
            codeStage.initStyle(StageStyle.UNDECORATED);
            codeStage.setScene(new Scene(root,500,150));
            codeStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
