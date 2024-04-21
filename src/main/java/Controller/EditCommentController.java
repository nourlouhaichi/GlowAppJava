package Controller;

import Entities.Comment;
import Services.ServiceComment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class EditCommentController {

    @FXML
    private TextField contentfx;

    @FXML
    private TextField idfx;

    ServiceComment serviceComment = new ServiceComment();

    @FXML
    void editonaction(ActionEvent event) throws SQLException {
        Comment comment = new Comment();
        comment.setId(Integer.parseInt(idfx.getText()));
        comment.setContenue(contentfx.getText());
        serviceComment.modifier(comment);
    }
    public void initData(Comment comment) throws SQLException {
        String idText = String.valueOf(comment.getId());
        idfx.setText(idText);
        contentfx.setText(comment.getContenue());
    }

}
