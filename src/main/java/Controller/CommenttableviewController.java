package Controller;

import Entities.Comment;
import Entities.Publication;
import Services.ServiceComment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CommenttableviewController {

    @FXML
    private TableColumn<Comment, String> contcol;

    @FXML
    private TableColumn<Comment, Integer> idcol;

    @FXML
    private TableView<Comment> commtab;


    private Publication selectedPublication;


    public void setSelectedPublication(Publication publication) {
        this.selectedPublication = publication;
    }

    private List<Comment> retrieveCommentsForPublication(int publicationId) {
        ServiceComment serviceComment = new ServiceComment();
        List<Comment> comments = null;
        try {
            comments = serviceComment.affiche(publicationId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public void initialize() {
        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        contcol.setCellValueFactory(new PropertyValueFactory<>("contenue"));

        if (selectedPublication != null) {
            List<Comment> comments = retrieveCommentsForPublication(selectedPublication.getId());

            if (comments != null) {

                commtab.getItems().addAll(comments);
            } else {

                System.out.println("No comments available for the selected publication.");
            }
        } else {

            System.out.println("No publication selected.");
        }
    }

    public void editonclick(MouseEvent mouseEvent) throws IOException, SQLException {
        Comment selectedcomment = commtab.getSelectionModel().getSelectedItem();
        if (selectedcomment != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/EditComment.fxml"));
            Parent root = loader.load();
            EditCommentController controller = loader.getController();
            controller.initData(selectedcomment);
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } else {
            System.out.println("No Comment selected.");
        }}



    public void deleteonclick(MouseEvent mouseEvent) throws SQLException {
        int idselected = commtab.getSelectionModel().getSelectedIndex();
        commtab.getItems().remove(idselected);
        ServiceComment serviceComment = new ServiceComment();
        serviceComment.supprimer(idselected);
    }


    public void addcommonclick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back/AddComment.fxml"));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }
}
