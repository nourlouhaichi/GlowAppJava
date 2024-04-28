package FrontController;

import Entities.Comment;
import Entities.Publication;
import Services.ServiceComment;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

public class ShowDetailsPubController {

    @FXML
    private Label commentfx;

    @FXML
    private Label contentpbfx;

    @FXML
    private Label titlefx;

    @FXML
    private ImageView imagefx;

    @FXML
    private VBox commentsContainer;

    private ServiceComment serviceComment = new ServiceComment();

    private Publication selectedPublication;
    public void setSelectedPublication(Publication publication) {
        this.selectedPublication = publication;
        if (selectedPublication != null) {
            titlefx.setText(selectedPublication.getTitrep());
            contentpbfx.setText(selectedPublication.getContentp());
            String imagePath = selectedPublication.getImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    Image image = new Image(new FileInputStream(imagePath));
                    imagefx.setImage(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            loadComments();
        }
    }
    private void loadComments() {
        if (selectedPublication != null) {  // Check to ensure that selectedPublication is not null
            try {
                List<Comment> comments = serviceComment.affiche(selectedPublication.getId());
                commentsContainer.getChildren().clear(); // Clear existing comments
                for (Comment comment : comments) {
                    Label commentLabel = new Label(comment.getContenue());
                    commentLabel.getStyleClass().add("comment-style");
                    commentsContainer.getChildren().add(commentLabel);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error loading comments: " + e.getMessage());
            }
        }
    }


    public void initialize(){
        setSelectedPublication(selectedPublication);
        System.out.println("this is : "+selectedPublication);

    }

}
