package Controllers;

import Entities.User;
import Services.Session;
import Services.UserService;
import Utils.MyDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.Node;

import de.svws_nrw.ext.jbcrypt.BCrypt;
import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.io.IOException;
import java.util.regex.Pattern;

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
    private TextField searchTextField;
    @FXML
    private DatePicker dobDatePicker;
    @FXML
    private TableView<User> userTable;
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

    private ObservableList<User> userObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File logoFile = new File("images/logoglowapp.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);

        Session session = Session.getInstance();
        Map<String, Object> userSession = session.getUserSession();
        usernameLabel.setText(userSession.get("email").toString());

        cinColumn.setCellValueFactory(new PropertyValueFactory<>("cin"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        verifiedColumn.setCellValueFactory(new PropertyValueFactory<>("is_verified"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));

        UserService us = new UserService();
        try {
            List<User> users = us.afficherBack(userSession.get("cin").toString());
            userObservableList.addAll(users);
            userTable.setItems(userObservableList);

            FilteredList<User> filteredData = new FilteredList<>(userObservableList, b -> true);
            SortedList<User> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(userTable.comparatorProperty());
            userTable.setItems(sortedData);

            searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(user -> {
                    if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();
                    return user.getCin().toLowerCase().contains(searchKeyword)
                            || user.getEmail().toLowerCase().contains(searchKeyword)
                            || user.getLastname().toLowerCase().contains(searchKeyword)
                            || user.getFirstname().toLowerCase().contains(searchKeyword)
                            || user.getGender().toLowerCase().contains(searchKeyword)
                            || String.valueOf(user.getIs_verified()).toLowerCase().contains(searchKeyword)
                            || user.getRoles().toLowerCase().contains(searchKeyword);
                });
            });
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
            //userStage.initStyle(StageStyle.UNDECORATED);
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
            //homeStage.initStyle(StageStyle.UNDECORATED);
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
                        JOptionPane.showMessageDialog(null, "Email already exists!", "Error", JOptionPane.PLAIN_MESSAGE);
                    } else if (cinResultSet.next()) {
                        JOptionPane.showMessageDialog(null, "CIN already exists!", "Error", JOptionPane.PLAIN_MESSAGE);
                    } else if (phoneResultSet.next()) {
                        JOptionPane.showMessageDialog(null, "Phone number already exists!", "Error", JOptionPane.PLAIN_MESSAGE);
                    }
                    else {
                        addUser();
                    }
                } catch (SQLException e) {
                    // Handle database errors
                    e.printStackTrace();
                }
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Enter all fields!!", "Error", JOptionPane.PLAIN_MESSAGE);
        }

    }

    public void deleteButtonOnAction(ActionEvent event) {
        UserService us = new UserService();
        if (!cinTextField.getText().isBlank()
                && !lastnameTextField.getText().isBlank()
                && !firstnameTextField.getText().isBlank()
                && !phoneTextField.getText().isBlank()
                && !emailTextField.getText().isBlank()
                && (maleRadioButton.isSelected() || femaleRadioButton.isSelected())
                && (adminRadioButton.isSelected() || userRadioButton.isSelected() || coachRadioButton.isSelected() || nutRadioButton.isSelected())
                && dobDatePicker.getValue() != null) {
            try {
                User user = us.getUser(cinTextField.getText());
                us.supprimer(user);
                int index = userTable.getSelectionModel().getSelectedIndex();
                userObservableList.remove(index);
                JOptionPane.showMessageDialog(null, "User removed!", "Success", JOptionPane.PLAIN_MESSAGE);
                clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Select User!", "Success", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void showButtonOnAction(ActionEvent event) {
        UserService us = new UserService();
        if (!cinTextField.getText().isBlank()
                && !lastnameTextField.getText().isBlank()
                && !firstnameTextField.getText().isBlank()
                && !phoneTextField.getText().isBlank()
                && !emailTextField.getText().isBlank()
                && (maleRadioButton.isSelected() || femaleRadioButton.isSelected())
                && (adminRadioButton.isSelected() || userRadioButton.isSelected() || coachRadioButton.isSelected() || nutRadioButton.isSelected())
                && dobDatePicker.getValue() != null) {
            try {
                User user = us.getUser(cinTextField.getText());

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/profiledetailsGUI.fxml"));
                Parent root = loader.load();

                profiledetailsController controller = loader.getController();
                controller.initData(user);

                Stage profileStage = new Stage();
                profileStage.initStyle(StageStyle.UNDECORATED);
                profileStage.setScene(new Scene(root,800,540));
                profileStage.show();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Select User!", "Success", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void updateButtonOnAction(ActionEvent event) {
        if (cinTextField.isDisable()
            && passwordTextField.isDisable()
            && emailTextField.isDisable()
            && lastnameTextField.isDisable()
            && firstnameTextField.isDisable()
            && phoneTextField.isDisable()
            && dobDatePicker.isDisable()) {
                updateUser();
        }
        else {
            JOptionPane.showMessageDialog(null, "Enter all fields!!", "Error", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void updateUser() {
        UserService us = new UserService();

        String jsonRoles = "[\"ROLE_USER\"]";
        if ((adminRadioButton.isSelected())
                && (!userRadioButton.isSelected())
                && (!coachRadioButton.isSelected())
                && (!nutRadioButton.isSelected())) {
            jsonRoles = "[\"ROLE_ADMIN\"]";
        } else if ((!adminRadioButton.isSelected())
                && (userRadioButton.isSelected())
                && (!coachRadioButton.isSelected())
                && (!nutRadioButton.isSelected())) {
            jsonRoles = "[\"ROLE_USER\"]";
        }else if ((!adminRadioButton.isSelected())
                && (!userRadioButton.isSelected())
                && (coachRadioButton.isSelected())
                && (!nutRadioButton.isSelected())) {
            jsonRoles = "[\"ROLE_COACH\"]";
        }else if ((!adminRadioButton.isSelected())
                && (!userRadioButton.isSelected())
                && (!coachRadioButton.isSelected())
                && (nutRadioButton.isSelected())) {
            jsonRoles = "[\"ROLE_NUTRITIONIST\"]";
        }

        try {
            User user = userTable.getSelectionModel().getSelectedItem();
            user.setRoles(jsonRoles);
            us.modifierBack(user);
            JOptionPane.showMessageDialog(null, "User Updated!", "Success", JOptionPane.PLAIN_MESSAGE);

        } catch (SQLException e) {
            // Handle database errors
            e.printStackTrace();
        }

        userTable.refresh();
        clear();
    }

    public void addUser() {
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
        if ((adminRadioButton.isSelected())
                && (!userRadioButton.isSelected())
                && (!coachRadioButton.isSelected())
                && (!nutRadioButton.isSelected())) {
            jsonRoles = "[\"ROLE_ADMIN\"]";
        } else if ((!adminRadioButton.isSelected())
                && (userRadioButton.isSelected())
                && (!coachRadioButton.isSelected())
                && (!nutRadioButton.isSelected())) {
            jsonRoles = "[\"ROLE_USER\"]";
        }else if ((!adminRadioButton.isSelected())
                && (!userRadioButton.isSelected())
                && (coachRadioButton.isSelected())
                && (!nutRadioButton.isSelected())) {
            jsonRoles = "[\"ROLE_COACH\"]";
        }else if ((!adminRadioButton.isSelected())
                && (!userRadioButton.isSelected())
                && (!coachRadioButton.isSelected())
                && (nutRadioButton.isSelected())) {
            jsonRoles = "[\"ROLE_NUTRITIONIST\"]";
        }

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
            userObservableList.addAll(user);

        } catch (SQLException e) {
            // Handle database errors
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "User Added!", "Success", JOptionPane.PLAIN_MESSAGE);
        clear();
    }

    public void clearButtonOnAction(ActionEvent event) {
        clear();
    }

    public void clear() {
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
        cinTextField.setDisable(false);
        passwordTextField.setDisable(false);
        emailTextField.setDisable(false);
        lastnameTextField.setDisable(false);
        firstnameTextField.setDisable(false);
        phoneTextField.setDisable(false);
        dobDatePicker.setDisable(false);
        userTable.refresh();
    }

    public void getUser() {
        User user = userTable.getSelectionModel().getSelectedItem();
        if (user != null) {
            cinTextField.setText(user.getCin());
            emailTextField.setText(user.getEmail());
            lastnameTextField.setText(user.getLastname());
            firstnameTextField.setText(user.getFirstname());

            if (Objects.equals(user.getRoles(), "[\"ROLE_ADMIN\"]")) {
                adminRadioButton.setSelected(true);
                coachRadioButton.setSelected(false);
                nutRadioButton.setSelected(false);
                userRadioButton.setSelected(false);
            } else if (Objects.equals(user.getRoles(), "[\"ROLE_USER\"]")) {
                userRadioButton.setSelected(true);
                coachRadioButton.setSelected(false);
                nutRadioButton.setSelected(false);
                adminRadioButton.setSelected(false);
            } else if (Objects.equals(user.getRoles(), "[\"ROLE_COACH\"]")) {
                coachRadioButton.setSelected(true);
                nutRadioButton.setSelected(false);
                userRadioButton.setSelected(false);
                adminRadioButton.setSelected(false);
            } else if (Objects.equals(user.getRoles(), "[\"ROLE_NUTRITIONIST\"]")) {
                nutRadioButton.setSelected(true);
                coachRadioButton.setSelected(false);
                userRadioButton.setSelected(false);
                adminRadioButton.setSelected(false);
            } else {
                userRadioButton.setSelected(true);
                coachRadioButton.setSelected(false);
                nutRadioButton.setSelected(false);
                adminRadioButton.setSelected(false);
            }

            if (Objects.equals(user.getGender(), "male")) {
                maleRadioButton.setSelected(true);
                femaleRadioButton.setSelected(false);
            } else if (Objects.equals(user.getGender(), "female")) {
                maleRadioButton.setSelected(false);
                femaleRadioButton.setSelected(true);
            }

            UserService us = new UserService();
            try {
                User user2 = us.getUser(user.getCin());
                phoneTextField.setText(user2.getPhone());
                dobDatePicker.setValue(user2.getDatebirth().toLocalDate());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            cinTextField.setDisable(true);
            passwordTextField.setDisable(true);
            emailTextField.setDisable(true);
            lastnameTextField.setDisable(true);
            firstnameTextField.setDisable(true);
            phoneTextField.setDisable(true);
            dobDatePicker.setDisable(true);
        }
    }

    public boolean verifyInputs() {
        Pattern digitsPattern = Pattern.compile("\\d{8}");
        Pattern lettersPattern = Pattern.compile("\\p{L}+");
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        Pattern passwordPattern = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}");

        if (!digitsPattern.matcher(cinTextField.getText()).matches()) {
            JOptionPane.showMessageDialog(null, "CIN must contain only 8 digits!", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!emailPattern.matcher(emailTextField.getText()).matches()) {
            JOptionPane.showMessageDialog(null, "Invalid email address!", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!passwordPattern.matcher(passwordTextField.getText()).matches()) {
            JOptionPane.showMessageDialog(null, "Password must contain digits, uppercase and lowercase letters, and be at least 8 characters long!", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!lettersPattern.matcher(lastnameTextField.getText()).matches()) {
            JOptionPane.showMessageDialog(null, "Lastname must contain only letters!", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!lettersPattern.matcher(firstnameTextField.getText()).matches()) {
            JOptionPane.showMessageDialog(null, "Firstname must contain only letters!", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (!digitsPattern.matcher(phoneTextField.getText()).matches()) {
            JOptionPane.showMessageDialog(null, "Phone must contain only 8 digits!", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        } else if (dobDatePicker.getValue().isAfter(LocalDate.now().minusYears(18))) {
            JOptionPane.showMessageDialog(null, "Person must be over 18 years old!", "Error", JOptionPane.PLAIN_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    @FXML
    void ProduitButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListProduit.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage1.close();

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void categorieButton(ActionEvent event) {

        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListCateforieP.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage1.close();

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void eventButtonOnAction(ActionEvent event) {
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Events.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage1.close();

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void publicationButtonOnAction(ActionEvent event) {

        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/backhome.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage1.close();

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void programButtonOnAction(ActionEvent event) {

        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Programme.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage1.close();

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
