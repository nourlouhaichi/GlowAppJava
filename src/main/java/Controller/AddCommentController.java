package Controller;

import Entities.Comment;
import Entities.Publication;
import Services.ServiceComment;
import Services.ServicePublication;
import com.almasb.fxgl.physics.box2d.dynamics.contacts.Contact;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class AddCommentController {

    @FXML
    private Button addonclick;

    @FXML
    private TextField contfx;

    @FXML
    private TextField pubidfx;

    @FXML
    private TextField idfx;

    ServiceComment serviceComment = new ServiceComment();


    public void Addonclick(javafx.event.ActionEvent actionEvent) throws SQLException {
        Comment comment = new Comment();
        comment.setPublication_id(Integer.parseInt(pubidfx.getText()));
        comment.setContenue(contfx.getText());
        serviceComment.ajouter(comment);
    }

    public void deleteonaction(ActionEvent actionEvent) {
    }

    public void editonclick(ActionEvent actionEvent) throws SQLException {
        Comment comment = new Comment();
        comment.setId(Integer.parseInt(idfx.getText()));
        comment.setContenue(contfx.getText());
        comment.setPublication_id(Integer.parseInt(pubidfx.getText()));
        serviceComment.modifier(comment);
    }
}
