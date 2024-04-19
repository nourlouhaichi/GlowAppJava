package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

import Entities.Publication;
import Services.ServicePublication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


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
    @FXML
    private AnchorPane rootPane;


    ServicePublication Servicepublication = new ServicePublication();


    @FXML
    void addpub(ActionEvent event) throws SQLException {
        Publication publication = new Publication();
        publication.setTitrep(titlefx.getText());
        publication.setTypep(typefx.getText());
        publication.setContentp(contentfx.getText());
        //publication.setDatecrp(Date.from(datefx.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Servicepublication.ajouter(publication);

    }

    @FXML
    void onedit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditPublication.fxml"));
        Parent root = loader.load();
        Stage window = (Stage) editbtn.getScene().getWindow();
        window.setScene(new Scene(root, 750, 500));
        window.show();
    }

    @FXML
    void initialize() {
        assert contentfx != null : "fx:id=\"contentfx\" was not injected: check your FXML file 'Publication.fxml'.";
        assert datefx != null : "fx:id=\"datefx\" was not injected: check your FXML file 'Publication.fxml'.";
        assert titlefx != null : "fx:id=\"titlefx\" was not injected: check your FXML file 'Publication.fxml'.";
        assert editbtn != null : "fx:id=\"editbtn\" was not injected: check your FXML file 'Publication.fxml'.";
        assert typefx != null : "fx:id=\"typefx\" was not injected: check your FXML file 'Publication.fxml'.";

    }

    public void exitbutton(ActionEvent actionEvent) {

    }
}
