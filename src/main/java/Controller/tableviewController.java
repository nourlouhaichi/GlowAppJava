package Controller;

import Entities.Publication;

import java.io.IOException;

import Services.ServicePublication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

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
        // Get the selected publication
        Publication selectedPublication = pubtable.getSelectionModel().getSelectedItem();

        // Check if a publication is selected
        if (selectedPublication != null) {
            try {
                // Remove the selected publication from the TableView
                pubtable.getItems().remove(selectedPublication);

                // Delete the selected publication from the database
                ServicePublication servicePublication = new ServicePublication();
                servicePublication.supprimer(selectedPublication);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No publication selected.");
        }
    }


    public void editonclick(MouseEvent mouseEvent) throws IOException {
        Publication selectedPublication = pubtable.getSelectionModel().getSelectedItem();
        if (selectedPublication != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/EditPublication.fxml"));
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
            publications = servicePublication.afficher();
        } catch (SQLException e) {
            e.printStackTrace();

        }

        // Repopulate TableView with updated data
        if (publications != null) {
            pubtable.getItems().addAll(publications);
        }
    }

    public void addonclick(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Back/Publication.fxml"));
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root, 400, 400));
            newStage.show();
            newStage.setOnCloseRequest(event -> updateTableView());

        } catch (IOException e) {
            e.printStackTrace();
    }

}

    public void commetonclick(MouseEvent mouseEvent) {  Publication selectedPublication = pubtable.getSelectionModel().getSelectedItem();
        if (selectedPublication != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/Commenttableview.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            CommenttableviewController controller = loader.getController();
            controller.setSelectedPublication(selectedPublication);
            controller.initialize();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } else {
            System.out.println("No publication selected.");
        }
    }
}