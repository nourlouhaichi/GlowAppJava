package FrontController;

import Entities.Publication;
import Services.ServiceComment;
import Services.ServicePublication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public class ShowPublicationController {

    @FXML
    private Label contentfx;

    @FXML
    private ImageView imagefx;

    @FXML
    private Label titlefx;


    @FXML
    private Label commentCountLabel;

    private Publication selectedPublication;

    ListPublicationsController listPublicationsController;

    public void setSelectedPublication(Publication publication) {
        this.selectedPublication = publication;
    }
    ServicePublication servicePublication= new ServicePublication();


    public void initialize(Publication publication) throws SQLException, FileNotFoundException {
        contentfx.setText(publication.getContentp());
        titlefx.setText(publication.getTitrep());
        String imagePath = publication.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image(new FileInputStream(imagePath));
            imagefx.setImage(image);
            ServiceComment serviceComment = new ServiceComment();
            int commentCount = serviceComment.getCommentCount(publication.getId());
            commentCountLabel.setText(String.valueOf(commentCount));
        }
    }


    public void showdetails(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front/ShowDetailsPub.fxml"));
            Parent root = loader.load();
            ShowDetailsPubController controller = loader.getController();
            controller.setSelectedPublication(selectedPublication);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteonclick(MouseEvent mouseEvent) throws SQLException {

        servicePublication.supprimer(selectedPublication);
        listPublicationsController.loadPublications();

    }
}
