package Controller;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import Entities.Publication;
import Services.ServicePublication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EditPublicationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addbutton;

    @FXML
    private TextField contentfx;

    @FXML
    private AnchorPane editscene;

    @FXML
    private TextField titlefx;

    @FXML
    private TextField typefx;


    @FXML
    private TextField idfx;

    @FXML
    private TableView<Publication> pubtable;

    ServicePublication Servicepublication = new ServicePublication();
    @FXML
    void editonclick(ActionEvent event) throws SQLException {
            Publication publication = new Publication();
            publication.setId(Integer.parseInt(idfx.getText()));
            publication.setTitrep(titlefx.getText());
            publication.setTypep(typefx.getText());
            publication.setContentp(contentfx.getText());
            //publication.setDatecrp(Date.from(datefx.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            Servicepublication.modifier(publication);

    }


    @FXML
    void initialize() {
        assert contentfx != null : "fx:id=\"contentfx\" was not injected: check your FXML file 'EditPublication.fxml'.";
        assert editscene != null : "fx:id=\"editscene\" was not injected: check your FXML file 'EditPublication.fxml'.";
        assert titlefx != null : "fx:id=\"titlefx\" was not injected: check your FXML file 'EditPublication.fxml'.";
        assert typefx != null : "fx:id=\"typefx\" was not injected: check your FXML file 'EditPublication.fxml'.";

    }

    public void initData(Publication publication) {
        String idText = String.valueOf(publication.getId());
        idfx.setText(idText);
        titlefx.setText(publication.getTitrep());
        typefx.setText(publication.getTypep());
        contentfx.setText(publication.getContentp());
    }
}



