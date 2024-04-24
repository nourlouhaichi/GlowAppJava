package Controller;

import Entities.Publication;

import java.awt.*;
import java.io.IOException;

import Services.ServicePublication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;
import javafx.scene.control.TextField;

public class tableviewController {

    @FXML
    private TableView<Publication> pubtable;
    @FXML
    public TextField search;



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
        search.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                searchFilter();
            }});
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
        ObservableList<Publication> items = FXCollections.observableArrayList(); // Create a new ObservableList

        // Fetch updated data from the database
        List<Publication> publications = null;
        try {
            publications = servicePublication.afficher();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add the fetched publications to the new ObservableList
        if (publications != null) {
            items.addAll(publications);
        }

        // Set the new ObservableList as the items of the TableView
        pubtable.setItems(items);
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

    public void addcomm(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/AddComment.fxml"));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }
    public void searchFilter() {
        // Get the text from the search filter TextField
        String searchText = search.getText().toLowerCase();

        // Create a Predicate to filter the TableView
        Predicate<Publication> filterByTitle = publication -> {
            // Convert the title to lowercase for case-insensitive search
            String title = publication.getTitrep().toLowerCase();
            // Return true if the title contains the search text
            return title.contains(searchText);
        };

        // Wrap the ObservableList of publications in a FilteredList
        FilteredList<Publication> filteredData = new FilteredList<>(pubtable.getItems());

        // Set the Predicate to the filteredData
        filteredData.setPredicate(filterByTitle);

        // Wrap the filteredData in a SortedList to enable sorting
        SortedList<Publication> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList to the TableView
        sortedData.comparatorProperty().bind(pubtable.comparatorProperty());
        pubtable.setItems(sortedData);

    }

    public void refreshonclick(MouseEvent mouseEvent) {
        updateTableView();
    }
}