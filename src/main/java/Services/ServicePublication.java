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
        String sql = "INSERT INTO publication(titre_p, type_p) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, Publication.gettitrep());
        statement.setString(2, Publication.gettypep());
        //statement.setDate(4, Publication.getDatecrp());
        statement.executeUpdate();
        System.out.println("publication ajoutée");
    }

    @Override
    public void modifier(Publication Publication) throws SQLException {
        String req = "UPDATE publication SET titre_p=?, type_p=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, Publication.gettitrep());
        preparedStatement.setString(2, Publication.gettypep());
        preparedStatement.setInt(3, Publication.getId());
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
        String req = "DELETE FROM publication WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, publication.getId());
        preparedStatement.executeUpdate();
        System.out.println("Publication supprimée");
    }

    @Override
    public List<Publication> afficher() throws SQLException {
        List<Publication> publications= new ArrayList<Publication>();
        String req="select * from publication";
        Statement statement= connection.createStatement();

        ResultSet rs= statement.executeQuery(req);
        while (rs.next()){
            Publication Publication= new Publication();
            Publication.settitrep(rs.getString("titre_p"));
            Publication.settypep(rs.getString("type_p"));
            Publication.setId(rs.getInt("id"));
            Publication.setcontent(rs.getString("contenue_p"));

            publications.add(Publication);
        }
        throw new UnsupportedOperationException("Unimplemented method 'afficher'");
    }

    @Override
    public Publication afficher(Publication publication) throws SQLException {
        return null;
    }

}
