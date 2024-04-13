package Controllers;

import Entities.User;
import Services.Session;
import Services.UserService;
import Utils.MyDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

import org.mindrot.jbcrypt.BCrypt;
import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class backUserController implements Initializable {

    @FXML
    private Label usernameLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Button userButton;
    @FXML
    private Button frontButton;
    @FXML
    private Button homeButton;
    @FXML
    private ImageView logoImageView;
    @FXML
    private TextField cinTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private RadioButton adminRadioButton;
    @FXML
    private RadioButton userRadioButton;
    @FXML
    private RadioButton coachRadioButton;
    @FXML
    private RadioButton nutRadioButton;
    @FXML
    private TextField lastnameTextField;
    @FXML
    private TextField firstnameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private DatePicker dobDatePicker;
    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Boolean> bannedColumn;
    @FXML
    private TableColumn<User, String> cinColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> firstnameColumn;
    @FXML
    private TableColumn<User, String> genderColumn;
    @FXML
    private TableColumn<User, String> lastnameColumn;
    @FXML
    private TableColumn<User, Boolean> verifiedColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("images/logoglowapp.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);

        Session session = Session.getInstance();
        Map<String, Object> userSession = session.getUserSession();
        usernameLabel.setText(userSession.get("firstname") + " " + userSession.get("lastname"));

        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        bannedColumn.setCellValueFactory(new PropertyValueFactory<>("is_banned"));
        verifiedColumn.setCellValueFactory(new PropertyValueFactory<>("is_verified"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));

        UserService us = new UserService();
        try {
            List<User> users = us.afficherBack(userSession.get("cin").toString());
            userTable.getItems().addAll(users);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logoutButtonOnAction(ActionEvent event) {
        Session.getInstance().logout();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/loginGUI.fxml"));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(new Scene(root,520,400));
            loginStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void frontButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) frontButton.getScene().getWindow();
        stage.close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/homeGUI.fxml"));
            Stage homeStage = new Stage();
            homeStage.initStyle(StageStyle.UNDECORATED);
            homeStage.setScene(new Scene(root,1024,576));
            homeStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void userButtonOnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/backUserGUI.fxml"));
            Stage userStage = new Stage();
            userStage.initStyle(StageStyle.UNDECORATED);
            userStage.setScene(new Scene(root,1100,600));
            userStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) userButton.getScene().getWindow();
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

    public void homeButtonOnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/backHomeGUI.fxml"));
            Stage homeStage = new Stage();
            homeStage.initStyle(StageStyle.UNDECORATED);
            homeStage.setScene(new Scene(root,1100,600));
            homeStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) homeButton.getScene().getWindow();
        stage.close();
    }

    public void addButtonOnAction(ActionEvent event) {
        if (!cinTextField.getText().isBlank()
                && !lastnameTextField.getText().isBlank()
                && !firstnameTextField.getText().isBlank()
                && !phoneTextField.getText().isBlank()
                && !emailTextField.getText().isBlank()
                && !passwordTextField.getText().isBlank()
                && (maleRadioButton.isSelected() || femaleRadioButton.isSelected())
                && (adminRadioButton.isSelected() || userRadioButton.isSelected() || coachRadioButton.isSelected() || nutRadioButton.isSelected())
                && dobDatePicker.getValue() != null) {
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
                    JOptionPane.showMessageDialog(null, "Email already exists!", "Error", JOptionPane.PLAIN_MESSAGE);
                } else if (cinResultSet.next()) {
                    JOptionPane.showMessageDialog(null, "CIN already exists!", "Error", JOptionPane.PLAIN_MESSAGE);
                } else if (phoneResultSet.next()) {
                    JOptionPane.showMessageDialog(null, "Phone number already exists!", "Error", JOptionPane.PLAIN_MESSAGE);
                }
                else {
                    adduser();
                }
            } catch (SQLException e) {
                // Handle database errors
                e.printStackTrace();
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Enter all fields!!", "Error", JOptionPane.PLAIN_MESSAGE);
        }

    }

    public void deleteButtonOnAction(ActionEvent event) {

    }

    public void updateButtonOnAction(ActionEvent event) {

    }

    public void adduser() {
        UserService us = new UserService();

        String gender = null;
        if ((maleRadioButton.isSelected()) && (!femaleRadioButton.isSelected())) {
            gender = "male";
        } else if ((!maleRadioButton.isSelected()) && (femaleRadioButton.isSelected())){
            gender = "female";
        }

        String role = "USER";
        if ((adminRadioButton.isSelected())
                && (!userRadioButton.isSelected())
                && (!coachRadioButton.isSelected())
                && (!nutRadioButton.isSelected())) {
            role = "ADMIN";
        } else if ((!adminRadioButton.isSelected())
                && (userRadioButton.isSelected())
                && (!coachRadioButton.isSelected())
                && (!nutRadioButton.isSelected())) {
            role = "USER";
        }else if ((!adminRadioButton.isSelected())
                && (!userRadioButton.isSelected())
                && (coachRadioButton.isSelected())
                && (!nutRadioButton.isSelected())) {
            role = "COACH";
        }else if ((!adminRadioButton.isSelected())
                && (!userRadioButton.isSelected())
                && (!coachRadioButton.isSelected())
                && (nutRadioButton.isSelected())) {
            role = "NUTRITIONIST";
        }

        User user = new User(
                emailTextField.getText(),
                role,
                BCrypt.hashpw(passwordTextField.getText(), BCrypt.gensalt()),
                cinTextField.getText(),
                lastnameTextField.getText(),
                firstnameTextField.getText(),
                gender,
                Date.valueOf(dobDatePicker.getValue()),
                phoneTextField.getText(),
                new Timestamp(System.currentTimeMillis()),
                false,
                null,
                false,
                null
        );

        Session session = Session.getInstance();
        Map<String, Object> userSession = session.getUserSession();
        try {
            us.ajouter(user);

            cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
            firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
            genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
            bannedColumn.setCellValueFactory(new PropertyValueFactory<>("is_banned"));
            verifiedColumn.setCellValueFactory(new PropertyValueFactory<>("is_verified"));
            roleColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));

            userTable.getItems().addAll(user);

        } catch (SQLException e) {
            // Handle database errors
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "User Added!", "Success", JOptionPane.PLAIN_MESSAGE);
        cinTextField.clear();
        emailTextField.clear();
        passwordTextField.clear();
        lastnameTextField.clear();
        firstnameTextField.clear();
        phoneTextField.clear();
        dobDatePicker.setValue(null);
        coachRadioButton.setSelected(false);
        nutRadioButton.setSelected(false);
        userRadioButton.setSelected(false);
        adminRadioButton.setSelected(false);
        maleRadioButton.setSelected(false);
        femaleRadioButton.setSelected(false);
    }
}
