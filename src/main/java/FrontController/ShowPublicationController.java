package FrontController;

import Entities.Publication;
import Services.ServiceComment;
import Services.ServicePublication;
import Services.Session;
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
import java.util.Map;

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

    @FXML
    private ImageView deletebutton;

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
            Session session = Session.getInstance();
            Map<String, Object> userSession = session.getUserSession(); // Assuming you have a method to get the current logged-in user
            String roles = userSession.get("roles").toString();
            roles = roles.replace("[", "").replace("]", "").replace("\"", "");
            if ("ROLE_USER".equals(roles)) {
                deletebutton.setVisible(false);
            }
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
    public void updatePublicationListAfterDeletion() {
        listPublicationsController.loadPublications();
    }

    public void deleteonclick(MouseEvent mouseEvent) throws SQLException {

        servicePublication.supprimer(selectedPublication);
        if (listPublicationsController != null) {
            listPublicationsController.loadPublications();
        } else {
            System.out.println("listPublicationsController is null");
        }

    }
}
