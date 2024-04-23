package Controller;

import Entities.Comment;
import Services.ServiceComment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
        if(contentfx.getText().equals("")) {
            contentfx.setStyle("-fx-border-color: red");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
        }else {
            Comment comment = new Comment();
            comment.setId(Integer.parseInt(idfx.getText()));
            comment.setContenue(contentfx.getText());
            serviceComment.modifier(comment);
        }
    }
    public void initData(Comment comment) throws SQLException {
        String idText = String.valueOf(comment.getId());
        idfx.setText(idText);
        contentfx.setText(comment.getContenue());
    }

}
