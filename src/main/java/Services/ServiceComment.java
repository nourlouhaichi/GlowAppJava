package Services;

import Entities.Comment;
import Entities.Publication;
import Entities.User;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceComment implements IServices<Comment> {
    private Connection connection;

    public ServiceComment() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Comment comment) throws SQLException {
        String sql = "INSERT INTO comment(publication_id,contenue) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, String.valueOf(comment.getPublication_id()));
        statement.setString(2, comment.getContenue());
        statement.executeUpdate();
        System.out.println("comment ajoutée");
    }

    @Override
    public void modifier(Comment comment) throws SQLException {
        String req = "UPDATE comment SET  contenue=?  WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, comment.getContenue());
        preparedStatement.setInt(2, comment.getId());
        preparedStatement.executeUpdate();
        System.out.println("Comment modifiée");
    }

    @Override
    public void supprimer(User user) throws SQLException {

    }

    @Override
    public void supprimer(int id) throws SQLException {

    }

    @Override
    public void supprimer(Publication publication) throws SQLException {

    }

    @Override
    public void supprimer(Comment comment) throws SQLException {
        String req = "DELETE FROM comment WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, comment.getId());
            preparedStatement.executeUpdate();
            System.out.println("Comment supprimé");
        }
    }

    @Override
    public List<Comment> afficher() throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String req = "SELECT * FROM comment";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(req)) {
            while (rs.next()) {
                Comment comment = new Comment();
                comment.setId(rs.getInt("id"));
                comment.setPublication_id(rs.getInt("publication_id")); // Assuming publication_id is the foreign key referencing the publication
                comment.setContenue(rs.getString("contenue"));
                comments.add(comment);
            }
        }
        return comments;
    }

    @Override
    public List<Comment> affiche(int publication_id) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String req = "SELECT * FROM comment WHERE publication_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, publication_id); 
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment();
                    comment.setId(rs.getInt("id"));
                    comment.setPublication_id(rs.getInt("publication_id"));
                    comment.setContenue(rs.getString("contenue"));
                    comments.add(comment);
                }
            }
        }
        return comments;
        }


    @Override
    public Comment afficher(Comment comment) throws SQLException {
        return null;
    }

    @Override
    public List<Comment> afficher(int id) throws SQLException {
        return List.of();
    }

    @Override
    public void ajouterWithImage(Publication publication, byte[] imageData) throws SQLException {

    }


}
