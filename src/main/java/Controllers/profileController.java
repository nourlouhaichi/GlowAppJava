package Controllers;

import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import Services.Session;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Entities.User;
import javax.swing.*;
import at.favre.lib.crypto.bcrypt.BCrypt;

public class profileController implements Initializable {

    @FXML
    private ImageView profilePicture;
    @FXML
    private Button cancelButton;
    @FXML
    private Button modifyButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label createdAtLabel;
    @FXML
    private Label errorLabel;
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
    private TextField newPasswordTextField;
    @FXML
    private TextField confirmPasswordTextField;

    String url = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Session session = Session.getInstance();
        Map<String, Object> userSession = session.getUserSession();

        profilePicture.setFitWidth(200);
        profilePicture.setFitHeight(200);

        File pictureFile = new File("images/" + userSession.get("profile_picture").toString());
        Image profilePictureImage = new Image(pictureFile.toURI().toString());
        profilePicture.setImage(profilePictureImage);


        usernameLabel.setText(userSession.get("firstname") + " " + userSession.get("lastname"));

        cinTextField.setText(userSession.get("cin").toString());
        cinTextField.setStyle("-fx-text-fill: #808080; -fx-border-color: #ffb524;");
        cinTextField.setDisable(true);

        lastnameTextField.setText(userSession.get("lastname").toString());
        lastnameTextField.setStyle("-fx-text-fill: #808080; -fx-border-color: #ffb524;");

        firstnameTextField.setText(userSession.get("firstname").toString());
        firstnameTextField.setStyle("-fx-text-fill: #808080; -fx-border-color: #ffb524;");

        roleLabel.setText(userSession.get("roles").toString());

        createdAtLabel.setText(userSession.get("created_at").toString());

        emailTextField.setText(userSession.get("email").toString());
        emailTextField.setStyle("-fx-text-fill: #808080; -fx-border-color: #ffb524;");
        emailTextField.setDisable(true);

        phoneTextField.setText(userSession.get("phone").toString());
        phoneTextField.setStyle("-fx-text-fill: #808080; -fx-border-color: #ffb524;");

    }

    public void cancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void modifyButtonAction(ActionEvent event) {

        if (!lastnameTextField.getText().isBlank()
                && !firstnameTextField.getText().isBlank()
                && !emailTextField.getText().isBlank()
                && !phoneTextField.getText().isBlank()) {

            if (newPasswordTextField.getText().isBlank() && confirmPasswordTextField.getText().isBlank()) {
                if (verifyInputsWithoutPassword()) {
                    userUpdate();
                    JOptionPane.showMessageDialog(null, "Profile updated!", "Success", JOptionPane.PLAIN_MESSAGE);
                    Stage stage = (Stage) modifyButton.getScene().getWindow();
                    stage.close();
                }
            }
            else {
                if (Objects.equals(newPasswordTextField.getText(), confirmPasswordTextField.getText()))  {
                    if (verifyInputsWithPassword()) {
                        userUpdatePassword();
                        JOptionPane.showMessageDialog(null, "Profile updated!", "Success", JOptionPane.PLAIN_MESSAGE);
                        Stage stage = (Stage) modifyButton.getScene().getWindow();
                        stage.close();
                    }
                }
                else {
                    errorLabel.setText("Passwords are not the same!");
                }
            }
        }
        else {
            errorLabel.setText("Enter all fields!");
        }
    }

    public void userUpdate() {
        UserService us = new UserService();
        Session session = Session.getInstance();

        try {
            User user = us.getUser(cinTextField.getText());
            user.setLastname(lastnameTextField.getText());
            user.setFirstname(firstnameTextField.getText());
            user.setEmail(emailTextField.getText());
            user.setPhone(phoneTextField.getText());
            user.setProfile_picture(url);

            session.getUserSession().put("lastname", lastnameTextField.getText());
            session.getUserSession().put("firstname", firstnameTextField.getText());
            session.getUserSession().put("email", emailTextField.getText());
            session.getUserSession().put("phone", phoneTextField.getText());
            if (url != null) {
                session.getUserSession().put("profile_picture",url);
                user.setProfile_picture(url);
            }


            us.modifierSansMdp(user);

        } catch (SQLException e) {
            // Handle database errors
            e.printStackTrace();
        }
    }

    public void userUpdatePassword() {
        UserService us = new UserService();
        Session session = Session.getInstance();

        try {
            User user = us.getUser(cinTextField.getText());
            user.setLastname(lastnameTextField.getText());
            user.setFirstname(firstnameTextField.getText());
            user.setEmail(emailTextField.getText());
            user.setPhone(phoneTextField.getText());
            user.setPassword(BCrypt.withDefaults().hashToString(13,newPasswordTextField.getText().toCharArray()));

            session.getUserSession().put("lastname", lastnameTextField.getText());
            session.getUserSession().put("firstname", firstnameTextField.getText());
            session.getUserSession().put("email", emailTextField.getText());
            session.getUserSession().put("phone", phoneTextField.getText());
            session.getUserSession().put("password", BCrypt.withDefaults().hashToString(13,newPasswordTextField.getText().toCharArray()));
            if (url != null) {
                session.getUserSession().put("profile_picture",url);
                user.setProfile_picture(url);
            }

            us.modifier(user);

        } catch (SQLException e) {
            // Handle database errors
            e.printStackTrace();
        }
    }

    public boolean verifyInputsWithPassword() {
        Pattern digitsPattern = Pattern.compile("\\d{8}");
        Pattern lettersPattern = Pattern.compile("\\p{L}+");
        Pattern passwordPattern = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}");

        if (!lettersPattern.matcher(lastnameTextField.getText()).matches()) {
            errorLabel.setText("Lastname must contain only letters!");
            return false;
        } else if (!lettersPattern.matcher(firstnameTextField.getText()).matches()) {
            errorLabel.setText("Firstname must contain only letters!");
            return false;
        } else if (!digitsPattern.matcher(phoneTextField.getText()).matches()) {
            errorLabel.setText("Phone must contain only 8 digits!");
            return false;
        } else if (!passwordPattern.matcher(newPasswordTextField.getText()).matches()) {
            errorLabel.setText("Password must contain digits, uppercase and lowercase letters, and be at least 8 characters long!");
            return false;
        } else {
            return true;
        }
    }

    public boolean verifyInputsWithoutPassword() {
        Pattern digitsPattern = Pattern.compile("\\d{8}");
        Pattern lettersPattern = Pattern.compile("\\p{L}+");

        if (!lettersPattern.matcher(lastnameTextField.getText()).matches()) {
            errorLabel.setText("Lastname must contain only letters!");
            return false;
        } else if (!lettersPattern.matcher(firstnameTextField.getText()).matches()) {
            errorLabel.setText("Firstname must contain only letters!");
            return false;
        } else if (!digitsPattern.matcher(phoneTextField.getText()).matches()) {
            errorLabel.setText("Phone must contain only 8 digits!");
            return false;
        } else {
            return true;
        }
    }

    public void pictureButtonOnAction(ActionEvent event) {
        url = getProfilePicturePath();
        File pictureFile = new File("images/" + url);
        Image profilePictureImage = new Image(pictureFile.toURI().toString());
        profilePicture.setFitWidth(200);
        profilePicture.setFitHeight(200);
        profilePicture.setImage(profilePictureImage);
    }

    private String getProfilePicturePath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your profile picture");

        File initialDirectory = new File("images");
        fileChooser.setInitialDirectory(initialDirectory);

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                Files.copy(selectedFile.toPath(), new File(initialDirectory, selectedFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                String url = initialDirectory.toURI().resolve(selectedFile.getName()).toString();
                String motCle = "images/";
                int debutIndex = url.indexOf(motCle);
                debutIndex += motCle.length();
                return url.substring(debutIndex);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
