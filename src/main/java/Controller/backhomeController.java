package Controller;

import Entities.Publication;
import Services.ServicePublication;
import Services.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class backhomeController {

    @FXML
    private Button PublicationButton;

    @FXML
    private Button frontButton;

    @FXML
    private Button homeButton;

    @FXML
    private BarChart<?, ?> home_chart;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Label home_totalEmployees;

    @FXML
    private Label home_totalInactiveEm;

    @FXML
    private Label home_totalPresents;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Button logoutButton;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button profileButton;

    @FXML
    private Button userButton;

    @FXML
    private Label usernameLabel;

    @FXML
    void PublicationButtonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/tableview.fxml"));
            Parent newPage = loader.load();
            tableviewController controller = loader.getController();
            AnchorPane.setTopAnchor(newPage, 10.0);
            AnchorPane.setLeftAnchor(newPage, 40.0);
            AnchorPane.setRightAnchor(newPage, 10.0);
            AnchorPane.setBottomAnchor(newPage, 10.0);
            home_form.getChildren().setAll(newPage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
    private String convertPublicationListToHtml(List<Publication> pubList) {
        StringBuilder htmlBuilder = new StringBuilder();

        // Begin HTML
        htmlBuilder.append("<html><body>");
        htmlBuilder.append("<h1>Programme Report</h1>");
        htmlBuilder.append("<table border='1'><tr><th>Title</th><th>Plan</th><th>Available Places</th></tr>");

        // Add rows for each Programme
        for (Publication pub : pubList) {
            htmlBuilder.append("<tr>");
            htmlBuilder.append("<td>").append(pub.getTitrep()).append("</td>");
            htmlBuilder.append("<td>").append(pub.getContentp()).append("</td>");
            htmlBuilder.append("<td>").append(pub.getTypep()).append("</td>");
            htmlBuilder.append("</tr>");
        }

        // End HTML
        htmlBuilder.append("</table></body></html>");

        return htmlBuilder.toString();
    }

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
        Session session = Session.getInstance();
        Map<String, Object> userSession = session.getUserSession();
        usernameLabel.setText(userSession.get("email").toString());

        File logoFile = new File("images/logoglowapp.png");
        javafx.scene.image.Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);
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
        if(searchText.isEmpty()){
            updateTableView();
        }

    }


    public void generatePDFReport(ActionEvent actionEvent) {
        ServicePublication serviceProgramme = new ServicePublication();
        try {
            List<Publication> pubList = serviceProgramme.afficher();
            String htmlContent = convertPublicationListToHtml(pubList);
            String apiEndpoint = "https://pdf-api.co/pdf";
            String apiKey = "007AAFF6403C0D554DE694FA59030F057C94";
            String requestBody = String.format("{\"apiKey\": \"%s\", \"html\": \"%s\"}", apiKey, htmlContent);
            HttpResponse<byte[]> response = Unirest.post(apiEndpoint)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .asBytes();

            if (response.getStatus() == 200 && "application/pdf".equals(response.getHeaders().getFirst("Content-Type"))) {
                Path path = Paths.get("ProgramReport.pdf");
                Files.write(path, response.getBody());
                System.out.println("PDF Generated at: " + path.toAbsolutePath());

                if (Desktop.isDesktopSupported()) {
                    try {
                        File pdfFile = new File(path.toString());
                        Desktop.getDesktop().open(pdfFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        System.err.println("Unable to open the PDF file. Error: " + ex.getMessage());
                    }
                } else {
                    System.err.println("Desktop operations not supported on the current platform. Cannot open the PDF file automatically.");
                }
            } else {
                System.err.println("Failed to generate PDF: " + response.getStatusText());
                System.err.println("Status Code: " + response.getStatus());
                System.err.println("Response Headers: " + response.getHeaders());
                String responseBody = new String(response.getBody(), StandardCharsets.UTF_8);
                System.err.println("Response Body: " + responseBody);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    public void fronthome(ActionEvent actionEvent) throws IOException {
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
    public void addcommentonclick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/AddComment.fxml"));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    public void viewcommentonclick(ActionEvent actionEvent) {
        Publication selectedPublication = pubtable.getSelectionModel().getSelectedItem();
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

    public void addpubonclick(ActionEvent actionEvent) {
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

    public void editpubonclick(ActionEvent actionEvent) throws IOException {
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
        }
    }

    public void deletepubonclick(ActionEvent actionEvent) {
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

    public void profilaction(ActionEvent actionEvent) {
    }
}