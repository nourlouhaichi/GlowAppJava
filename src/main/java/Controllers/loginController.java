package Controllers;

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

import Utils.MyDatabase;
import java.sql.*;
import java.util.ResourceBundle;
import java.net.URL;
import java.io.File;

import javafx.stage.StageStyle;
import org.mindrot.jbcrypt.BCrypt;

public class loginController implements Initializable {
    @FXML
    private Button exitButton;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("images/loginpic.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File logoFile = new File("images/logoglowapp.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);
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

    public void registerButtonOnAction(ActionEvent event) {
        createAccountForm();
    }

    public void validateLogin() {
        try {
            MyDatabase bd = MyDatabase.getInstance();
            Connection connectDB = bd.getConnection();

            String query = "SELECT password FROM User WHERE email = ?";
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, emailTextField.getText());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                if (BCrypt.checkpw(enterPasswordTextField.getText(), hashedPassword)) {
                    loginMessageLabel.setText("Welcome!");
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
}
