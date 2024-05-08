package Controller;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Entities.Publication;
import Services.ServicePublication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class PublicationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField contentfx;

    @FXML
    private DatePicker datefx;

    @FXML
    private TextField titlefx;

    @FXML
    private TextField typefx;
    @FXML
    private Button editbtn;

    private String selectedImagePath;
    @FXML
    private Button imageSelectButton;


    ServicePublication Servicepublication = new ServicePublication();


    @FXML
    void addpub(ActionEvent event) throws SQLException, IOException {
        Publication publication = new Publication();
        publication.setTitrep(titlefx.getText());
        publication.setTypep(typefx.getText());
        publication.setContentp(contentfx.getText());

    }



    @FXML
    void initialize() {
        assert contentfx != null : "fx:id=\"contentfx\" was not injected: check your FXML file 'Publication.fxml'.";
        assert datefx != null : "fx:id=\"datefx\" was not injected: check your FXML file 'Publication.fxml'.";
        assert titlefx != null : "fx:id=\"titlefx\" was not injected: check your FXML file 'Publication.fxml'.";
        assert editbtn != null : "fx:id=\"editbtn\" was not injected: check your FXML file 'Publication.fxml'.";
        assert typefx != null : "fx:id=\"typefx\" was not injected: check your FXML file 'Publication.fxml'.";

    }



    public void addpubonclick(ActionEvent actionEvent) throws SQLException {
        if(titlefx.getText().isEmpty() || typefx.getText().isEmpty() || contentfx.getText().isEmpty()) {
            titlefx.setStyle("-fx-border-color: red");
            typefx.setStyle("-fx-border-color: red");
            contentfx.setStyle("-fx-border-color: red");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
        }else {
            Publication publication = new Publication();
            publication.setTitrep(titlefx.getText());
            publication.setTypep(typefx.getText());
            publication.setContentp(contentfx.getText());
            publication.setImage(getSelectedImagePath());
            //publication.setDatecrp(Date.from(datefx.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            Servicepublication.create(publication);
            Notifications notifications = Notifications.create();
            notifications.text("Publication added successfully !");
            notifications.title("Successful");
            notifications.hideAfter(Duration.seconds(6));
            notifications.show();
        }
    }
    public void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            selectedImagePath = selectedFile.toURI().getPath(); // Store the selected image path without the file:\ prefix
        }
        System.out.println("Selected Image Path: " + selectedImagePath);
    }
    @FXML
    void selectImage(ActionEvent event) {
        selectImage(); // Call the selectImage method
    }
    public String getSelectedImagePath() {
        return selectedImagePath;
    }
}
