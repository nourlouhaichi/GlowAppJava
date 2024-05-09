package Controller;

import Entities.Publication;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import Services.ServicePublication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;
import javafx.scene.control.TextField;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

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

    }


    public void editonclick(MouseEvent mouseEvent) throws IOException {
        }
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


}

    public void commetonclick(MouseEvent mouseEvent) {
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



}
