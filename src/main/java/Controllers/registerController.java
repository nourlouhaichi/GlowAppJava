package Controllers;

import Services.GMailer;
import Utils.MyDatabase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.DatePicker;

import java.sql.*;
import java.util.ResourceBundle;
import java.net.URL;
import java.io.File;
import java.sql.Timestamp;
import Services.UserService;
import Entities.User;
import de.svws_nrw.ext.jbcrypt.BCrypt;
import javax.swing.JOptionPane;
import java.util.regex.Pattern;
import java.time.LocalDate;

public class registerController implements Initializable {

    @FXML
    private ImageView brandingImageView;
    @FXML
    private Button cancelButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label registerMessageLabel;
    @FXML
    private TextField cinTextField;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private DatePicker dobDatePicker;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("images/registerpic.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);
    }

    public void cancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void registerButtonOnAction(ActionEvent event) {
        if (!cinTextField.getText().isBlank()
                && !lastnameTextField.getText().isBlank()
                && !firstnameTextField.getText().isBlank()
                && !phoneTextField.getText().isBlank()
                && !emailTextField.getText().isBlank()
                && !passwordTextField.getText().isBlank()
                && (maleRadioButton.isSelected() || femaleRadioButton.isSelected())
                && dobDatePicker.getValue() != null) {
            if (verifyInputs()) {
                try {
                    MyDatabase bd = MyDatabase.getInstance();
                    Connection connectDB = bd.getConnection();

                    // Check if the email already exists
                    String emailQuery = "SELECT email FROM User WHERE email = ?";
                    PreparedStatement emailStatement = connectDB.prepareStatement(emailQuery);
                    emailStatement.setString(1, emailTextField.getText());
                    ResultSet emailResultSet = emailStatement.executeQuery();

                    // Check if the cin already exists
                    String cinQuery = "SELECT cin FROM User WHERE cin = ?";
                    PreparedStatement cinStatement = connectDB.prepareStatement(cinQuery);
                    cinStatement.setString(1, cinTextField.getText());
                    ResultSet cinResultSet = cinStatement.executeQuery();

                    // Check if the phone already exists
                    String phoneQuery = "SELECT phone FROM User WHERE phone = ?";
                    PreparedStatement phoneStatement = connectDB.prepareStatement(phoneQuery);
                    phoneStatement.setString(1, phoneTextField.getText());
                    ResultSet phoneResultSet = phoneStatement.executeQuery();

                    if (emailResultSet.next()) {
                        registerMessageLabel.setText("Email already exists!");
                    } else if (cinResultSet.next()) {
                        registerMessageLabel.setText("CIN already exists!");
                    } else if (phoneResultSet.next()) {
                        registerMessageLabel.setText("Phone number already exists!");
                    }
                    else {
                        registerUser();
                        JOptionPane.showMessageDialog(null, "You are registered!", "Success", JOptionPane.PLAIN_MESSAGE);
                        Stage stage = (Stage) registerButton.getScene().getWindow();
                        stage.close();

                    }
                } catch (SQLException e) {
                    // Handle database errors
                    e.printStackTrace();
                }
            }
        }
        else {
            registerMessageLabel.setText("Enter all fields!");
        }
    }

    public void registerUser() {
        UserService us = new UserService();
        String gender = "male";
        String image = "Mprofile.png";

        if ((maleRadioButton.isSelected()) && (!femaleRadioButton.isSelected())) {
            gender = "male";
            image = "Mprofile.png";

        } else if ((!maleRadioButton.isSelected()) && (femaleRadioButton.isSelected())){
            gender = "female";
            image = "Fprofile.png";
        }

        String jsonRoles = "[\"ROLE_USER\"]";
        User user = new User(
                emailTextField.getText(),
                jsonRoles,
                BCrypt.hashpw(passwordTextField.getText(), BCrypt.gensalt(13)),
                cinTextField.getText(),
                lastnameTextField.getText(),
                firstnameTextField.getText(),
                gender,
                Date.valueOf(dobDatePicker.getValue()),
                phoneTextField.getText(),
                new Timestamp(System.currentTimeMillis()),
                false,
                image,
                false,
                null
        );
        try {
            us.ajouter(user);
        } catch (SQLException e) {
            // Handle database errors
            e.printStackTrace();
        }
    }

    public boolean verifyInputs() {
        Pattern digitsPattern = Pattern.compile("\\d{8}");
        Pattern lettersPattern = Pattern.compile("\\p{L}+");
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        Pattern passwordPattern = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}");

        if (!digitsPattern.matcher(cinTextField.getText()).matches()) {
            registerMessageLabel.setText("CIN must contain only 8 digits!");
            return false;
        } else if (!lettersPattern.matcher(lastnameTextField.getText()).matches()) {
            registerMessageLabel.setText("Lastname must contain only letters!");
            return false;
        } else if (!lettersPattern.matcher(firstnameTextField.getText()).matches()) {
            registerMessageLabel.setText("Firstname must contain only letters!");
            return false;
        } else if (dobDatePicker.getValue().isAfter(LocalDate.now().minusYears(18))) {
            registerMessageLabel.setText("You must be over 18 years old!");
            return false;
        } else if (!digitsPattern.matcher(phoneTextField.getText()).matches()) {
            registerMessageLabel.setText("Phone must contain only 8 digits!");
            return false;
        } else if (!emailPattern.matcher(emailTextField.getText()).matches()) {
            registerMessageLabel.setText("Invalid email address!");
            return false;
        } else if (!passwordPattern.matcher(passwordTextField.getText()).matches()) {
            registerMessageLabel.setText("Password must contain digits, uppercase and lowercase letters, and be at least 8 characters long!");
            return false;
        } else {
            return true;
        }
    }

}