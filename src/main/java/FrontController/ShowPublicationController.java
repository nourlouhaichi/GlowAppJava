package FrontController;

import Entities.Publication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;

public class ShowPublicationController {

    @FXML
    private Label contentfx;

    @FXML
    private ImageView imagefx;

    @FXML
    private Label titlefx;

    public void initialize(Publication publication) throws SQLException, FileNotFoundException {
        contentfx.setText(publication.getContentp());
        titlefx.setText(publication.getTitrep());
        String imagePath = publication.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image(new FileInputStream(imagePath));
            imagefx.setImage(image);
        }
    }
}
