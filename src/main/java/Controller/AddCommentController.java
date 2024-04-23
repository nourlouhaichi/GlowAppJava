package Controller;

import Entities.Comment;
import Entities.Publication;
import Services.ServiceComment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AddCommentController {

    @FXML
    private Button addonclick;

    @FXML
    private TextField contfx;

    @FXML
    private TextField idfx;

    private Publication selectedPublication; // Define the selectedPublication field

    private ServiceComment serviceComment;

    public void initialize() {
        serviceComment = new ServiceComment();
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
        if (contfx.getText().isEmpty()) {
            contfx.setStyle("-fx-border-color: red");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter your comment.");
            alert.showAndWait();
        } else {
            if (selectedPublication != null) { // Ensure a publication is selected
                Comment comment = new Comment();
                comment.setPublication_id(selectedPublication.getId()); // Set the publication_id
                comment.setContenue(contfx.getText());
                serviceComment.ajouter(comment);
            } else {
                System.out.println("No publication selected.");
            }
    }
}
}
