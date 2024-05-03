package FrontController;

import Entities.Comment;
import Entities.Publication;
import Services.ServiceComment;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.awt.Desktop;
import java.net.URI;


public class ShowDetailsPubController {

    @FXML
    private Label contentpbfx;

    @FXML
    private Label titlefx;

    @FXML
    private ImageView imagefx;

    @FXML
    private VBox commentsContainer;

    @FXML
    private TextField newCommentField;




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
        if (selectedPublication != null) {
            try {
                List<Comment> comments = serviceComment.affiche(selectedPublication.getId());
                commentsContainer.getChildren().clear();
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

    @FXML
    private void handleAddComment() {
        String commentContent = newCommentField.getText().trim();
        if (commentContent.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Comment cannot be empty!");
            alert.showAndWait();
        } else {
            try {
                Comment newComment = new Comment();
                newComment.setPublication_id(selectedPublication.getId());
                newComment.setContenue(commentContent);
                serviceComment.ajouter(newComment);
                newCommentField.setText("");
                loadComments();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error adding comment: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    public void initialize(){

            setSelectedPublication(selectedPublication);


    }



    public void handleShare(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            try {
                String encodedTitle = URLEncoder.encode("Interesting Publication: " + selectedPublication.getTitrep(), "UTF-8");
                String encodedContent = URLEncoder.encode(selectedPublication.getContentp() + " - Check it out here!", "UTF-8");

                // Email sharing
                String mailto = "mailto:?subject=" + encodedTitle + "&body=" + encodedContent;
                Desktop.getDesktop().browse(new URI(mailto));

                // Twitter sharing
                String tweetUrl = "https://twitter.com/intent/tweet?text=" + encodedContent;
                Desktop.getDesktop().browse(new URI(tweetUrl));
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open external application.");
                alert.showAndWait();
            }
        });
    }
}
