package Services;

import Entities.Comment;
import Entities.Publication;
import Entities.User;
import Utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePublication implements IServicePub<Publication> {
    Connection connection;

    public ServicePublication() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Publication Publication) throws SQLException {
        String sql = "INSERT INTO publication(titre_p, type_p,contenue_p) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, Publication.getTitrep());
        statement.setString(2, Publication.getTypep());
        statement.setString(3, Publication.getContentp());
        //statement.setDate(4, Publication.getDatecrp());
        statement.executeUpdate();
        System.out.println("publication ajoutée");
    }

    @Override
    public void modifier(Publication Publication) throws SQLException {
        String req = "UPDATE publication SET titre_p=?, type_p=? , contenue_p=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, Publication.getTitrep());
        preparedStatement.setString(2, Publication.getTypep());
        preparedStatement.setString(3, Publication.getContentp());
        preparedStatement.setInt(4, Publication.getId());

        preparedStatement.executeUpdate();
        System.out.println("Publication modifiée");
    }


    @Override
    public void ajouter(Comment comment) throws SQLException {

    }

    @Override
    public void modifier(Comment comment) throws SQLException {

    }

    @Override
    public void supprimer(User user) throws SQLException {

    }

    @Override
    public void supprimer(int id) throws SQLException {

    }

    @Override
    public void supprimer(Publication publication) throws SQLException {
        String req = "DELETE " +
                "FROM publication WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, publication.getId());
        preparedStatement.executeUpdate();
        System.out.println("Publication supprimée");
    }

    @Override
    public void supprimer(Comment comment) throws SQLException {

    }

    @Override
    public List<Publication> afficher() throws SQLException {
        List<Publication> publications = new ArrayList<>();
        String req = "SELECT * FROM publication";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(req)) {
            while (rs.next()) {
                Publication publication = new Publication();
                publication.setId(rs.getInt("id"));
                publication.setTitrep(rs.getString("titre_p"));
                publication.setTypep(rs.getString("type_p"));
                publication.setContentp(rs.getString("contenue_p"));
                publication.setImage(rs.getString("image_name"));
                publications.add(publication);
            }
        }
        return publications;
    }

    @Override
    public List<Publication> affiche(int publication_id) throws SQLException {
        return List.of();
    }

    @Override
    public Comment afficher(Comment comment) throws SQLException {
        return null;
    }

    @Override
    public Publication afficher(Publication publication) throws SQLException {
        return null;
    }

    @Override
    public List<Comment> afficher(int id) throws SQLException {
        String req = "SELECT * FROM publication WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            Publication publication = new Publication();
            publication.setId(rs.getInt("id"));
            publication.setTitrep(rs.getString("titre_p"));
            publication.setTypep(rs.getString("type_p"));
            publication.setContentp(rs.getString("contenue_p"));
            return (List<Comment>) publication;
        } else {
            return null; // Publication not found
        }

    }

    @Override
    public void ajouterWithImage(Publication publication, byte[] imageData) throws SQLException {

    }

    public void create(Publication publication) throws SQLException {
        String req = "INSERT INTO publication(titre_p, type_p, contenue_p, image_name) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, publication.getTitrep());
            ps.setString(2, publication.getTypep());
            ps.setString(3, publication.getContentp());;
            ps.setString(4, publication.getImage());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                publication.setId(rs.getInt(1));
            }

            rs.close();
        }
        System.out.println("publication ajoutée");
    }
    public List<Publication> searchPublications(String keyword) throws SQLException {
        List<Publication> filteredPublications = new ArrayList<>();
        String sql = "SELECT * FROM publication WHERE titre_p LIKE ? OR contenue_p LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Publication publication = new Publication();
                publication.setId(rs.getInt("id"));
                publication.setTitrep(rs.getString("titre_p"));
                publication.setTypep(rs.getString("type_p"));
                publication.setContentp(rs.getString("contenue_p"));
                publication.setImage(rs.getString("image_name"));
                filteredPublications.add(publication);
            }
            rs.close();
        }
        return filteredPublications;
    }
}git