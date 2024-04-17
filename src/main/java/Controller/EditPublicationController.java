package Controller;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Entities.Publication;
import Services.ServicePublication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private TextField idfx;

    @FXML
    private TextField titlefx;

    @FXML
    private TextField typefx;
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
    void deleteonclick(ActionEvent event) throws SQLException {
        Publication publication = new Publication();
        publication.setId(Integer.parseInt(idfx.getText()));
        Servicepublication.supprimer(publication);

    }
    @FXML
    void addonclick(ActionEvent event) {
        Stage stage=(Stage) addbutton.getScene().getWindow();
        stage.close();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Publication.fxml"));
            Stage homeStage = new Stage();
            homeStage.initStyle(StageStyle.UNDECORATED);
            homeStage.setScene(new Scene(root,1024,576));
            homeStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void initialize() {
        assert contentfx != null : "fx:id=\"contentfx\" was not injected: check your FXML file 'EditPublication.fxml'.";
        assert editscene != null : "fx:id=\"editscene\" was not injected: check your FXML file 'EditPublication.fxml'.";
        assert idfx != null : "fx:id=\"idfx\" was not injected: check your FXML file 'EditPublication.fxml'.";
        assert titlefx != null : "fx:id=\"titlefx\" was not injected: check your FXML file 'EditPublication.fxml'.";
        assert typefx != null : "fx:id=\"typefx\" was not injected: check your FXML file 'EditPublication.fxml'.";

    }

    public void showonaction(javafx.scene.input.MouseEvent mouseEvent) {
        Stage stage=(Stage) addbutton.getScene().getWindow();
        stage.close();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/tableview.fxml"));
            Stage homeStage = new Stage();
            homeStage.initStyle(StageStyle.UNDECORATED);
            homeStage.setScene(new Scene(root,1024,576));
            homeStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}



