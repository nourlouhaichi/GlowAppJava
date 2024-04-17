package Services;

import Entities.Publication;
import Entities.User;
import Utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePublication implements IServices<Publication> {
    Connection connection;
    public ServicePublication(){
        connection= MyDatabase.getInstance().getConnection();
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
                // You might need to set other properties too
                publications.add(publication);
            }
        }
        return publications;
    }

    @Override
    public Publication afficher(Publication publication) throws SQLException {
        return null;
    }

    @Override
    public Publication afficher(int id) throws SQLException {
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
            return publication;
        } else {
            return null; // Publication not found
        }

}
}
