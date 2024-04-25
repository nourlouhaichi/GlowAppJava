package Services;

import Entities.Programme;
import Utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProgramme implements IServicesProgramme<Programme> {
    Connection connection;
    public ServiceProgramme(){
        connection= MyDatabase.getInstance().getConnection();
    }
    @Override
    public void ajouter(Programme Programme) throws SQLException {
        String sql = "INSERT INTO programme(titre_pro, plan_pro, place_dispo, date_pro) VALUES (?, ? , ? , ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, Programme.getTitrepro());
        statement.setString(2, Programme.getPlanpro());
        statement.setInt(3, Programme.getPlacedispo());
        statement.setDate(4, Programme.getDatepro());
        statement.executeUpdate();
        System.out.println("Program Added");
    }


    @Override
    public void modifier(Programme Programme) throws SQLException {
        String req = "UPDATE programme SET titre_pro=?, plan_pro=?,place_dispo=?,date_pro=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setString(1, Programme.getTitrepro());
        preparedStatement.setString(2, Programme.getPlanpro());
        preparedStatement.setInt(3, Programme.getPlacedispo());
        preparedStatement.setDate(4, Programme.getDatepro());
        preparedStatement.setInt(5, Programme.getId());
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
        String query = "SELECT id, titre_pro, plan_pro, place_dispo, date_pro FROM programme";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Programme programme = new Programme(
                        rs.getInt("id"),
                        rs.getString("titre_pro"),
                        rs.getString("plan_pro"),
                        rs.getInt("place_dispo"),
                        rs.getDate("date_pro")
                );
                programmes.add(programme);
            }
        }
        return programmes;
    }




}