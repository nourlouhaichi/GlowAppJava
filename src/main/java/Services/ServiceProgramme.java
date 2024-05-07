package Services;

import Entities.Objectif;
import Entities.Programme;
import Utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class ServiceProgramme implements IServices<Programme> {
    private Connection connection;

    public ServiceProgramme() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Programme Programme) throws SQLException {
        String sql = "INSERT INTO programme(titre_pro, plan_pro, place_dispo, date_pro, imagePath) VALUES (?, ? , ? , ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, Programme.getTitrepro());
        statement.setString(2, Programme.getPlanpro());
        statement.setInt(3, Programme.getPlacedispo());
        statement.setDate(4, Programme.getDatepro());
        statement.setString(5, Programme.getImagePath());

        statement.executeUpdate();
        System.out.println("Program Added");
    }


    @Override
    public void modifier(Programme Programme) throws SQLException {
        String req = "UPDATE programme SET titre_pro=?, plan_pro=?,place_dispo=?,date_pro=?, imagePath=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, Programme.getTitrepro());
        preparedStatement.setString(2, Programme.getPlanpro());
        preparedStatement.setInt(3, Programme.getPlacedispo());
        preparedStatement.setDate(4, Programme.getDatepro());
        preparedStatement.setString(5, Programme.getImagePath());
        preparedStatement.setInt(6, Programme.getId());
        preparedStatement.executeUpdate();
        System.out.println("Program modified");
    }


    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM programme WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        System.out.println("Program deleted");
    }

    @Override
    public List<Programme> afficher() throws SQLException {
        List<Programme> programmes = new ArrayList<>();
        String query = "SELECT id, titre_pro, plan_pro, place_dispo, date_pro , imagePath FROM programme";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Programme programme = new Programme(
                        rs.getInt("id"),
                        rs.getString("titre_pro"),
                        rs.getString("plan_pro"),
                        rs.getInt("place_dispo"),
                        rs.getDate("date_pro"),
                        rs.getString("imagePath")
                );
                List<Objectif> objectifs = getObjectifListByProgID(programme.getId());
                programme.setObjectifs(objectifs);


                programmes.add(programme);
            }
        }
        return programmes;
    }

    public List<Objectif> getObjectifListByProgID(int programme_id) throws SQLException {
        List<Objectif> objectifs = new ArrayList<>();
        String query = "SELECT * FROM Objectif WHERE programme_id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, programme_id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Objectif objectif = new Objectif();
                    objectif.setId(rs.getInt("id"));
                    objectif.setProgramme_id(rs.getInt("programme_id"));
                    objectif.setObjectifO(rs.getString("objectif_o"));
                    objectif.setDescriptionO(rs.getString("description_o"));
                    objectif.setPoidO(rs.getFloat("poid_o"));
                    objectif.setTailleO(rs.getFloat("taille_o"));
                    objectifs.add(objectif);
                }
            }
        }
        return objectifs;
    }


    public List<Programme> search(String query) throws SQLException {
        List<Programme> searchResults = new ArrayList<>();
        String sql = "SELECT * FROM programme WHERE titre_pro LIKE ? OR plan_pro LIKE ?";
        try (
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            // Using LIKE with wildcards to search for titles or plans containing the query
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Programme prog = new Programme();
                    prog.setId(resultSet.getInt("id"));
                    prog.setTitrepro(resultSet.getString("titre_pro"));
                    prog.setPlanpro(resultSet.getString("plan_pro"));
                    // Set other attributes if necessary
                    searchResults.add(prog);
                }
            }
        }
        return searchResults;
    }

    @Override
    public void add(Programme t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public void update(Programme t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Programme t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public List<Programme> show() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'show'");
    }

    @Override
    public Programme afficher(Programme t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afficher'");
    }

    @Override
    public void supprimer(Programme t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'supprimer'");
    }
}




