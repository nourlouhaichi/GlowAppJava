package Services;

import Entities.Objectif;
import Utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceObjectif implements IServices<Objectif>{
    Connection connection;
    public ServiceObjectif(){connection= MyDatabase.getInstance().getConnection();
    }
    @Override
    public void ajouter(Objectif Objectif) throws SQLException {
        String sql = "INSERT INTO Objectif(objectif_o, description_o, poid_o, taille_o) VALUES (?, ? , ? , ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, Objectif.getObjectifO());
        statement.setString(2, Objectif.getDescriptionO());
        statement.setFloat(3, Objectif.getPoidO());
        statement.setFloat(4, Objectif.getTailleO());
        statement.executeUpdate();
        System.out.println("Objectif Added");
    }


    @Override
    public void modifier(Objectif Objectif) throws SQLException {
        String req = "UPDATE Objectif SET objectif_o=?, description_o=?,poid_o=?,taille_o=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, Objectif.getObjectifO());
        preparedStatement.setString(2, Objectif.getDescriptionO());
        preparedStatement.setFloat(3, Objectif.getPoidO());
        preparedStatement.setFloat(4, Objectif.getTailleO());
        preparedStatement.setInt(5, Objectif.getId());
        preparedStatement.executeUpdate();
        System.out.println("Objectif modified");
    }




    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM objectif WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        System.out.println("Objectif deleted");
    }

    @Override
    public List<Objectif> afficher() throws SQLException {
        List<Objectif> objectifs = new ArrayList<>();
        String query = "SELECT id, objectif_o , description_o , poid_o,  taille_o FROM objectif";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Objectif objectif = new Objectif(
                   rs.getInt("id"),
                   rs.getString("objectif_o"),
                   rs.getString("description_o"),
                        rs.getFloat("poid_o"),
                        rs.getFloat("taille_o")
                );
                objectifs.add(objectif);
            }
        }
        return objectifs;
    }
    @Override
    public void add(Objectif t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }
    @Override
    public void update(Objectif t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    @Override
    public void delete(Objectif t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    @Override
    public List<Objectif> show() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }
    @Override
    public Objectif afficher(Objectif t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afficher'");
    }
    @Override
    public void supprimer(Objectif t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'supprimer'");
    }





}
