package Services;

import Entities.Publication;
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
        String req="insert into publication(titre_p,type_p,contenue_p)"+"values('"+Publication.gettitrep()+"','"+Publication.gettypep()+"',"+Publication.getcontentp()+")";
        Statement statement=connection.createStatement();
        statement.executeUpdate(req);
        System.out.println("personne ajoute");
    }

    @Override
    public void modifier(Publication Publication) throws SQLException {
        String req="update publication set titre_p=?,type_p=?,content_p=?  where id=?";
        PreparedStatement preparedStatement= connection.prepareStatement(req);
        preparedStatement.setString(1,Publication.gettitrep());
        preparedStatement.setString(2,Publication.gettypep());
        preparedStatement.setString(3,Publication.getcontentp());
        preparedStatement.setInt(4,Publication.getId());
        preparedStatement.executeUpdate();

        throw new UnsupportedOperationException("Unimplemented method 'modifier'");
    }

    @Override
    public void supprimer(Publication publication) throws SQLException {

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM publication WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        int rowsDeleted = preparedStatement.executeUpdate();

        if (rowsDeleted > 0) {
            System.out.println("Publication with ID " + id + " deleted successfully.");
        } else {
            System.out.println("No publication found with ID " + id + ".");
        }

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
            Publication.setcontent(rs.getString("content_p"));

            publications.add(Publication);
        }
        throw new UnsupportedOperationException("Unimplemented method 'afficher'");
    }

    @Override
    public Publication afficher(Publication publication) throws SQLException {
        return null;
    }

}
