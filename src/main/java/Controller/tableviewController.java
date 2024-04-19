package Controller;

import Entities.Publication;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.*;

import Services.ServicePublication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    public void initialize() {
        titlecol.setCellValueFactory(new PropertyValueFactory<>("titrep"));
        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typecol.setCellValueFactory(new PropertyValueFactory<>("typep"));
        contentcol.setCellValueFactory(new PropertyValueFactory<>("contentp"));
        List<Publication> publications = retrieveDataFromDatabase();
        pubtable.getItems().addAll(publications);
    }


    public void deleteonclick(javafx.scene.input.MouseEvent mouseEvent) {
        int idselected = pubtable.getSelectionModel().getSelectedIndex();
        pubtable.getItems().remove(idselected);

    }

    public void editonclick(MouseEvent mouseEvent) throws IOException {
        Publication selectedPublication = pubtable.getSelectionModel().getSelectedItem();
        if (selectedPublication != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Editpublication.fxml"));
            Parent root = loader.load();
            EditPublicationController controller = loader.getController();
            controller.initData(selectedPublication);
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
            newStage.setOnCloseRequest(event -> updateTableView());
        } else {
            System.out.println("No publication selected.");
        }}
    private void updateTableView() {
        ServicePublication servicePublication = new ServicePublication();
        // Clear TableView
        pubtable.getItems().clear();

        // Fetch updated data from the database
        List<Publication> publications = null;
        try {
            publications = servicePublication.afficher(); // Replace with your method for fetching data from the database
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        }

        // Repopulate TableView with updated data
        if (publications != null) {
            pubtable.getItems().addAll(publications);
        }
    }

    public void addonclick(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Publication.fxml"));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root, 400, 400));
            newStage.show();
            newStage.setOnCloseRequest(event -> updateTableView());

        } catch (IOException e) {
            e.printStackTrace();
    }

}

}