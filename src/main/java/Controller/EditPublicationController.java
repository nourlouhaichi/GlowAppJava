package Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Entities.Publication;
import Services.ServicePublication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class EditPublicationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField contentfx;

    @FXML
    private AnchorPane editscene;

    @FXML
    private TextField idfx;

    @FXML
    private TextField titlefx;

    @FXML
    private TextField typefx;
    ServicePublication Servicepublication = new ServicePublication();
    @FXML
    void editonclick(ActionEvent event) throws SQLException {
        Publication publication = new Publication();
        publication.settitrep(titlefx.getText());
        publication.settypep(typefx.getText());
        publication.setId(Integer.parseInt(idfx.getText()));
        //publication.setcontent(contentf.getText());
        //publication.setDatecrp(Date.from(datefx.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Servicepublication.modifier(publication);

    }
    @FXML
    void deleteonclick(ActionEvent event) throws SQLException {
        Publication publication = new Publication();
        publication.setId(Integer.parseInt(idfx.getText()));
        Servicepublication.supprimer(publication);

    }

    @FXML
    void initialize() {
        assert contentfx != null : "fx:id=\"contentfx\" was not injected: check your FXML file 'EditPublication.fxml'.";
        assert editscene != null : "fx:id=\"editscene\" was not injected: check your FXML file 'EditPublication.fxml'.";
        assert idfx != null : "fx:id=\"idfx\" was not injected: check your FXML file 'EditPublication.fxml'.";
        assert titlefx != null : "fx:id=\"titlefx\" was not injected: check your FXML file 'EditPublication.fxml'.";
        assert typefx != null : "fx:id=\"typefx\" was not injected: check your FXML file 'EditPublication.fxml'.";

    }

}



