package Controller;

import Entities.Publication;

import java.awt.event.ActionEvent;
import java.sql.*;

import Services.ServicePublication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class tableviewController {

    @FXML
    private TableView<Publication> pubtable;



    @FXML
    private TableColumn<Publication, String> titlecol;
    @FXML
    private TableColumn<Publication, Integer> idcol;
    @FXML
    private TableColumn<Publication, String> typecol;
    @FXML
    private TableColumn<Publication, String> contentcol;

    private List<Publication> retrieveDataFromDatabase() {
        ServicePublication servicePublication = new ServicePublication();
        List<Publication> publications = null;
        try {
            publications = servicePublication.afficher();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publications;
    }

    public void showonaction(javafx.event.ActionEvent actionEvent) {
        titlecol.setCellValueFactory(new PropertyValueFactory<>("titrep"));
        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typecol.setCellValueFactory(new PropertyValueFactory<>("typep"));
        contentcol.setCellValueFactory(new PropertyValueFactory<>("contentp"));
        List<Publication> publications = retrieveDataFromDatabase();
        pubtable.getItems().addAll(publications);
    }

}