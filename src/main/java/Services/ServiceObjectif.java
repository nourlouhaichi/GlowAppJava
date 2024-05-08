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
    public void ajouter(Objectif objectif) throws SQLException {
        String sql = "INSERT INTO Objectif(objectif_o, description_o, poid_o, taille_o, user_cin, programme_id) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, objectif.getObjectifO());
        statement.setString(2, objectif.getDescriptionO());
        statement.setFloat(3, objectif.getPoidO());
        statement.setFloat(4, objectif.getTailleO());
        statement.setInt(5, Integer.parseInt(objectif.getUser().getCin()));
        statement.setInt(6, objectif.getProgramme_id());
        statement.executeUpdate();
        System.out.println("Objectif Added with Program ID: " + objectif.getProgramme_id());
    }

//    @Override
//    public void ajouter(Objectif objectif) throws SQLException {
//        String sql = "INSERT INTO Objectif(objectif_o, description_o, poid_o, taille_o, user_cin) VALUES (?, ?, ?, ?, ?)";
//        PreparedStatement statement = connection.prepareStatement(sql);
//        statement.setString(1, objectif.getObjectifO());
//        statement.setString(2, objectif.getDescriptionO());
//        statement.setFloat(3, objectif.getPoidO());
//        statement.setFloat(4, objectif.getTailleO());
//        statement.setInt(5, Integer.parseInt(objectif.getUser().getCin()));
//        statement.executeUpdate();
//        System.out.println("Objectif Added");
//    }

    @Override
    public void modifier(Objectif objectif) throws SQLException {
        String req = "UPDATE Objectif SET objectif_o=?, description_o=?, poid_o=?, taille_o=?, programme_id=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, objectif.getObjectifO());
        preparedStatement.setString(2, objectif.getDescriptionO());
        preparedStatement.setFloat(3, objectif.getPoidO());
        preparedStatement.setFloat(4, objectif.getTailleO());
        preparedStatement.setInt(5, objectif.getProgramme_id());
        preparedStatement.setInt(6, objectif.getId());
        preparedStatement.executeUpdate();
        System.out.println("Objectif modified with Program ID: " + objectif.getProgramme_id());
    }

//    @Override
//    public void modifier(Objectif Objectif) throws SQLException {
//        String req = "UPDATE Objectif SET objectif_o=?, description_o=?,poid_o=?,taille_o=? WHERE id=?";
//        PreparedStatement preparedStatement = connection.prepareStatement(req);
//        preparedStatement.setString(1, Objectif.getObjectifO());
//        preparedStatement.setString(2, Objectif.getDescriptionO());
//        preparedStatement.setFloat(3, Objectif.getPoidO());
//        preparedStatement.setFloat(4, Objectif.getTailleO());
//        preparedStatement.setInt(5, Objectif.getId());
//        preparedStatement.executeUpdate();
//        System.out.println("Objectif modified");
//    }




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
        String query = "SELECT id, objectif_o, description_o, poid_o, taille_o, programme_id FROM objectif";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Objectif objectif = new Objectif(
                        rs.getInt("id"),
                        rs.getString("objectif_o"),
                        rs.getString("description_o"),
                        rs.getFloat("poid_o"),
                        rs.getFloat("taille_o"),
                        rs.getInt("programme_id")
                );
                objectifs.add(objectif);
            }
        }
        return objectifs;
    }


    //    @Override
//    public List<Objectif> afficher() throws SQLException {
//        List<Objectif> objectifs = new ArrayList<>();
//        String query = "SELECT id, objectif_o , description_o , poid_o,  taille_o FROM objectif";
//        try (Statement stmt = connection.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//            while (rs.next()) {
//                Objectif objectif = new Objectif(
//                   rs.getInt("id"),
//                   rs.getString("objectif_o"),
//                   rs.getString("description_o"),
//                        rs.getFloat("poid_o"),
//                        rs.getFloat("taille_o")
//                );
//                objectifs.add(objectif);
//            }
//        }
//        return objectifs;
//    }
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
