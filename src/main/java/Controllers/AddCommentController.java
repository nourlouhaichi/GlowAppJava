package Controllers;

import Entities.Comment;
import Entities.Publication;
import Services.ServiceComment;
import Services.ServicePublication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;

public class AddCommentController {

    @FXML
    private Button addonclick;

    @FXML
    private TextField contfx;
    @FXML
    private TextField pubidfx;

    private Publication selectedPublication; // Define the selectedPublication field

    private ServiceComment serviceComment;
    private ServicePublication servicePublication;

    public void initialize() {
        serviceComment = new ServiceComment();
        servicePublication = new ServicePublication();
    }

    public void initData(Publication publication) {
        this.selectedPublication = publication;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void Addonclick(ActionEvent actionEvent) throws SQLException {
        if (pubidfx.getText().isEmpty() || contfx.getText().isEmpty()) {
            if (pubidfx.getText().isEmpty()) {
                pubidfx.setStyle("-fx-border-color: red");
            }
            if (contfx.getText().isEmpty()) {
                contfx.setStyle("-fx-border-color: red");
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter both publication ID and comment.");
            alert.showAndWait();
        } else {
            int publicationId = Integer.parseInt(pubidfx.getText());
            String commentText = contfx.getText();
            boolean publicationExists = false;
            try {
                List<Publication> publications = servicePublication.afficher();
                for (Publication publication : publications) {
                    if (publication.getId() == publicationId) {
                        publicationExists = true;
                        break;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (publicationExists) {
                Comment comment = new Comment();
                comment.setPublication_id(publicationId);
                comment.setContenue(commentText);
                serviceComment.ajouter(comment);
                showAlert("Success", "Comment added successfully.");
            } else {
                showAlert("Error", "The publication ID does not exist.");
            }
        }}
}
