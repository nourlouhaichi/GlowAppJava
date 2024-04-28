package FrontController;

import Entities.Publication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ShowDetailsPubController {

    @FXML
    private Label commentfx;

    @FXML
    private Label contentpbfx;

    @FXML
    private Label titlefx;

    @FXML
    private ImageView imagefx;

    private Publication selectedPublication;
    public void setSelectedPublication(Publication publication) {
        this.selectedPublication = publication;
        // Populate the UI elements with the details of the selected publication
        if (selectedPublication != null) {
            titlefx.setText(selectedPublication.getTitrep());
            contentpbfx.setText(selectedPublication.getContentp());
            // Load image if available
            String imagePath = selectedPublication.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    Image image = new Image(new FileInputStream(imagePath));
                    imagefx.setImage(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            // Set comments if available
            // commentfx.setText(selectedPublication.getComments());
        }
    }

    public void initialize(){
        setSelectedPublication(selectedPublication);
        System.out.println("this is : "+selectedPublication);

    }

}
